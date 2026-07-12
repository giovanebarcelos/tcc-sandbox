#!/usr/bin/env python3
"""TCC2001-ItensLR0.py | Aula 20
Gera a coleção canônica de itens LR(0) e a tabela SLR de uma gramática.
Gramática exemplo (expressões):
  0: E' → E
  1: E  → E + T
  2: E  → T
  3: T  → T * F
  4: T  → F
  5: F  → ( E )
  6: F  → id
"""
from collections import defaultdict
from typing import List, Set, Tuple, Dict


# Produções numeradas: (índice, esquerda, direita)
producoes = [
    ("E'", ["E"]),
    ("E", ["E", "+", "T"]),
    ("E", ["T"]),
    ("T", ["T", "*", "F"]),
    ("T", ["F"]),
    ("F", ["(", "E", ")"]),
    ("F", ["id"]),
]

nao_terminais = {p[0] for p in producoes}
terminais = set()
for _, _, dir_ in producoes:
    for s in dir_:
        if s not in nao_terminais:
            terminais.add(s)
terminais.add("$")


def fecho(itens: Set[Tuple[int, int]]) -> Set[Tuple[int, int]]:
    """Calcula o fecho de um conjunto de itens LR(0). Item = (num_produção, posição_do_ponto)."""
    resultado = set(itens)
    fila = list(itens)
    while fila:
        prod_idx, dot = fila.pop()
        _, esq, dir_ = producoes[prod_idx]
        if dot < len(dir_):
            simb = dir_[dot]
            if simb in nao_terminais:
                for i, (e, d) in enumerate(producoes):
                    if e == simb:
                        item = (i, 0)
                        if item not in resultado:
                            resultado.add(item)
                            fila.append(item)
    return resultado


def goto(itens: Set[Tuple[int, int]], simb: str) -> Set[Tuple[int, int]]:
    """Calcula GOTO(itens, simb)."""
    movidos = set()
    for prod_idx, dot in itens:
        _, _, dir_ = producoes[prod_idx]
        if dot < len(dir_) and dir_[dot] == simb:
            movidos.add((prod_idx, dot + 1))
    return fecho(movidos) if movidos else set()


def colecao_canonica() -> List[Set[Tuple[int, int]]]:
    """Gera a coleção canônica de estados LR(0)."""
    inicial = fecho({(0, 0)})
    estados = [inicial]
    transicoes: Dict[int, Dict[str, int]] = defaultdict(dict)
    fila = [0]
    while fila:
        idx = fila.pop(0)
        estado = estados[idx]
        simbolos = set()
        for prod_idx, dot in estado:
            _, _, dir_ = producoes[prod_idx]
            if dot < len(dir_):
                simbolos.add(dir_[dot])
        for simb in simbolos:
            novo = goto(estado, simb)
            if not novo:
                continue
            if novo not in estados:
                estados.append(novo)
                fila.append(len(estados) - 1)
            transicoes[idx][simb] = estados.index(novo)
    return estados


def first_nt(nt: str) -> Set[str]:
    """First simplificado (sem recursão profunda)."""
    resultado = set()
    for e, d in producoes:
        if e == nt and d:
            if d[0] in terminais:
                resultado.add(d[0])
            elif d[0] in nao_terminais and d[0] != nt:
                resultado |= first_nt(d[0])
    return resultado


def follow_nt(nt: str, follow: Dict[str, Set[str]]) -> Set[str]:
    """Follow simplificado."""
    resultado = follow.get(nt, set())
    for i, (e, d) in enumerate(producoes):
        for j, s in enumerate(d):
            if s == nt:
                resto = d[j + 1:]
                if resto:
                    if resto[0] in terminais:
                        resultado.add(resto[0])
                    else:
                        resultado |= first_nt(resto[0])
                else:
                    if e != nt:
                        resultado |= follow_nt(e, follow)
    return resultado


if __name__ == "__main__":
    estados = colecao_canonica()

    print("=== Coleção Canônica de Itens LR(0) ===\n")
    for i, estado in enumerate(estados):
        print(f"I{i}:")
        for prod_idx, dot in sorted(estado):
            _, esq, dir_ = producoes[prod_idx]
            dir_str = dir_[:dot] + ["•"] + dir_[dot:]
            print(f"  {esq} → {' '.join(dir_str)}")
        print()

    # Follow simplificado
    follow = {"E": {"$"}, "E'": {"$"}, "T": set(), "F": set()}
    follow["E"] |= {"+", "$"}
    follow["T"] |= {"*", "+", "$"}
    follow["F"] |= {"*", "+", ")", "$"}

    print("=== Follow (simplificado) ===")
    for nt in sorted(nao_terminais):
        print(f"  Follow({nt}) = {sorted(follow.get(nt, set()))}")
