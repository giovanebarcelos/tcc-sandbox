#!/usr/bin/env python3
"""TCC1801-FirstFollow.py | Aula 18
Calcula conjuntos First e Follow de uma GLC.
Gramática exemplo:
  E  → T E'
  E' → + T E' | ε
  T  → F T'
  T' → * F T' | ε
  F  → ( E ) | id
"""
from collections import defaultdict


# Gramática: não-terminal → lista de produções (cada produção é lista de símbolos)
gramatica = {
    "E":  [["T", "E'"]],
    "E'": [["+", "T", "E'"], ["ε"]],
    "T":  [["F", "T'"]],
    "T'": [["*", "F", "T'"], ["ε"]],
    "F":  [["(", "E", ")"], ["id"]],
}

nao_terminais = set(gramatica.keys())
terminais = set()
for prods in gramatica.values():
    for prod in prods:
        for s in prod:
            if s not in nao_terminais:
                terminais.add(s)
terminais.discard("ε")


def first_simbolo(s: str, first: dict, visitados: set) -> set:
    if s in terminais or s == "ε":
        return {s}
    if s in first:
        return first[s]
    if s in visitados:
        return set()
    visitados.add(s)
    resultado = set()
    for prod in gramatica[s]:
        for simb in prod:
            f = first_simbolo(simb, first, visitados)
            resultado |= f - {"ε"}
            if "ε" not in f:
                break
        else:
            resultado.add("ε")
    first[s] = resultado
    return resultado


def calcular_first() -> dict:
    first = {}
    for nt in nao_terminais:
        first_simbolo(nt, first, set())
    return first


def first_de_sequencia(seq: list, first: dict) -> set:
    resultado = set()
    for s in seq:
        f = first.get(s, {s})
        resultado |= f - {"ε"}
        if "ε" not in f:
            break
    else:
        resultado.add("ε")
    return resultado


def calcular_follow(first: dict) -> dict:
    follow = defaultdict(set)
    follow["E"].add("$")  # símbolo inicial
    mudou = True
    while mudou:
        mudou = False
        for A in nao_terminais:
            for prod in gramatica[A]:
                for i, B in enumerate(prod):
                    if B not in nao_terminais:
                        continue
                    resto = prod[i + 1:]
                    f_resto = first_de_sequencia(resto, first) if resto else {"ε"}
                    novo = follow[B] | (f_resto - {"ε"})
                    if "ε" in f_resto or not resto:
                        novo |= follow[A]
                    if novo != follow[B]:
                        follow[B] = novo
                        mudou = True
    return follow


if __name__ == "__main__":
    first = calcular_first()
    follow = calcular_follow(first)

    print("=== FIRST ===")
    for nt in sorted(nao_terminais):
        print(f"  First({nt}) = {sorted(first[nt])}")

    print("\n=== FOLLOW ===")
    for nt in sorted(nao_terminais):
        print(f"  Follow({nt}) = {sorted(follow[nt])}")

    print("\n=== Tabela LL(1) (parcial) ===")
    print(f"{'':>6}", end="")
    for t in sorted(terminais | {"$"}):
        print(f"{t:>12}", end="")
    print()
    for nt in sorted(nao_terminais):
        print(f"{nt:>6}", end="")
        for t in sorted(terminais | {"$"}):
            celula = ""
            for prod in gramatica[nt]:
                f = first_de_sequencia(prod, first)
                if t in f:
                    celula = " ".join(prod)
                    break
                if "ε" in f and t in follow[nt]:
                    celula = " ".join(prod)
                    break
            print(f"{celula:>12}", end="")
        print()
