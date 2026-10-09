#!/usr/bin/env python3
"""TCC2001-ItensLR0.py | Aula 20
Gera a coleção canônica de itens LR(0), calcula FIRST/FOLLOW, monta a tabela
SLR (ACTION + GOTO) e simula a análise de uma entrada.
Gramática exemplo (expressões):
  0: E' → E
  1: E  → E + T
  2: E  → T
  3: T  → T * F
  4: T  → F
  5: F  → ( E )
  6: F  → id
Uso: python3 TCC2001-ItensLR0.py
"""
from typing import Dict, FrozenSet, List, Set, Tuple

# Produções numeradas pelo índice da lista: (esquerda, direita)
producoes: List[Tuple[str, List[str]]] = [
    ("E'", ["E"]),
    ("E", ["E", "+", "T"]),
    ("E", ["T"]),
    ("T", ["T", "*", "F"]),
    ("T", ["F"]),
    ("F", ["(", "E", ")"]),
    ("F", ["id"]),
]

# Listas (e não sets) para a saída sair sempre na mesma ordem
nao_terminais = list(dict.fromkeys(esq for esq, _ in producoes))
terminais = list(dict.fromkeys(s for _, dir_ in producoes for s in dir_ if s not in nao_terminais))
terminais.append("$")

Item = Tuple[int, int]          # (número da produção, posição do ponto)
Estado = FrozenSet[Item]


def fecho(itens: Set[Item]) -> Estado:
    """Closure: para cada item A → α • B β, acrescenta B → • γ."""
    resultado = set(itens)
    fila = list(itens)
    while fila:
        prod_idx, dot = fila.pop(0)
        _, dir_ = producoes[prod_idx]
        if dot < len(dir_) and dir_[dot] in nao_terminais:
            for i, (esq, _) in enumerate(producoes):
                if esq == dir_[dot] and (i, 0) not in resultado:
                    resultado.add((i, 0))
                    fila.append((i, 0))
    return frozenset(resultado)


def goto(estado: Estado, simb: str) -> Estado:
    """Goto(I, X) = fecho dos itens de I com o ponto avançado sobre X."""
    movidos = {(p, d + 1) for p, d in estado
               if d < len(producoes[p][1]) and producoes[p][1][d] == simb}
    return fecho(movidos) if movidos else frozenset()


def colecao_canonica() -> Tuple[List[Estado], Dict[Tuple[int, str], int]]:
    """Gera os estados I0, I1, ... (BFS) e as transições do autômato LR(0)."""
    estados: List[Estado] = [fecho({(0, 0)})]
    transicoes: Dict[Tuple[int, str], int] = {}
    fila = [0]
    while fila:
        idx = fila.pop(0)
        # símbolos após o ponto, na ordem dos itens (numeração igual à do livro do Dragão)
        simbolos = dict.fromkeys(producoes[p][1][d] for p, d in sorted(estados[idx])
                                 if d < len(producoes[p][1]))
        for simb in simbolos:
            novo = goto(estados[idx], simb)
            if novo not in estados:
                estados.append(novo)
                fila.append(len(estados) - 1)
            transicoes[(idx, simb)] = estados.index(novo)
    return estados, transicoes


def first(simb: str) -> Set[str]:
    """FIRST de um símbolo (a gramática não tem produções ε)."""
    if simb not in nao_terminais:
        return {simb}
    resultado: Set[str] = set()
    for esq, dir_ in producoes:
        if esq == simb and dir_[0] != simb:      # ignora recursão à esquerda (E → E + T)
            resultado |= first(dir_[0])
    return resultado


def follow() -> Dict[str, Set[str]]:
    """FOLLOW por ponto fixo: repete até nenhum conjunto mudar."""
    fol: Dict[str, Set[str]] = {nt: set() for nt in nao_terminais}
    fol[nao_terminais[0]].add("$")
    mudou = True
    while mudou:
        mudou = False
        for esq, dir_ in producoes:
            for j, simb in enumerate(dir_):
                if simb not in nao_terminais:
                    continue
                novos = first(dir_[j + 1]) if j + 1 < len(dir_) else fol[esq]
                if not novos <= fol[simb]:
                    fol[simb] |= novos
                    mudou = True
    return fol


def tabela_slr(estados, transicoes, fol):
    """Regras SLR: shift (terminal após o ponto), reduce (item completo, para FOLLOW(A)), accept."""
    action: Dict[Tuple[int, str], str] = {}
    goto_tab: Dict[Tuple[int, str], int] = {}
    conflitos: List[str] = []

    def define(i, a, acao):
        if action.get((i, a), acao) != acao:
            conflitos.append(f"I{i}, {a}: {action[(i, a)]} / {acao}")
        action[(i, a)] = acao

    for (i, simb), j in transicoes.items():
        if simb in nao_terminais:
            goto_tab[(i, simb)] = j
        else:
            define(i, simb, f"s{j}")
    for i, estado in enumerate(estados):
        for p, d in sorted(estado):
            esq, dir_ = producoes[p]
            if d == len(dir_):
                if p == 0:
                    define(i, "$", "acc")
                else:
                    for a in terminais:
                        if a in fol[esq]:
                            define(i, a, f"r{p}")
    return action, goto_tab, conflitos


def simular(tokens: List[str], action, goto_tab) -> bool:
    """Analisador LR dirigido pela tabela: pilha de estados."""
    pilha = [0]
    entrada = tokens + ["$"]
    print(f"  {'pilha':<22}{'entrada':<22}ação")
    while True:
        estado, a = pilha[-1], entrada[0]
        acao = action.get((estado, a))
        print(f"  {' '.join(map(str, pilha)):<22}{' '.join(entrada):<22}{acao or 'ERRO'}")
        if acao is None:
            return False
        if acao == "acc":
            return True
        if acao[0] == "s":
            pilha.append(int(acao[1:]))
            entrada.pop(0)
        else:
            esq, dir_ = producoes[int(acao[1:])]
            del pilha[len(pilha) - len(dir_):]
            pilha.append(goto_tab[(pilha[-1], esq)])


def fmt(conjunto: Set[str]) -> str:
    return "{" + ", ".join(t for t in terminais if t in conjunto) + "}"


if __name__ == "__main__":
    estados, transicoes = colecao_canonica()

    print("=== Coleção Canônica de Itens LR(0) ===\n")
    for i, estado in enumerate(estados):
        print(f"I{i}:")
        for p, d in sorted(estado):
            esq, dir_ = producoes[p]
            print(f"  {esq} → {' '.join(dir_[:d] + ['•'] + dir_[d:])}")
        print()

    fol = follow()
    print("=== FIRST e FOLLOW ===")
    for nt in nao_terminais:
        print(f"  FIRST({nt}) = {fmt(first(nt))}   FOLLOW({nt}) = {fmt(fol[nt])}")

    action, goto_tab, conflitos = tabela_slr(estados, transicoes, fol)
    nts = nao_terminais[1:]
    print("\n=== Tabela SLR (ACTION | GOTO) ===")
    print("  est | " + "".join(f"{t:>5}" for t in terminais) + " | " + "".join(f"{n:>4}" for n in nts))
    for i in range(len(estados)):
        acoes = "".join(f"{action.get((i, t), ''):>5}" for t in terminais)
        gotos = "".join(f"{goto_tab.get((i, n), ''):>4}" for n in nts)
        print(f"  {i:>3} | {acoes} | {gotos}")
    print(f"  Conflitos: {', '.join(conflitos) if conflitos else 'nenhum (a gramática é SLR)'}")

    entrada = "id + id * id"
    print(f"\n=== Simulação: {entrada} ===")
    ok = simular(entrada.split(), action, goto_tab)
    print(f"  Resultado: {'ACEITA' if ok else 'REJEITA'}")
