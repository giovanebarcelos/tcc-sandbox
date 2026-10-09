#!/usr/bin/env python3
"""TCC0701-Determinizacao.py | Aula 07
Algoritmo de construção de subconjuntos: AFN → AFD.
"""
from collections import defaultdict

# AFN exemplo: termina com abb
afn = defaultdict(set, {
    ("q0", "a"): {"q0", "q1"}, ("q0", "b"): {"q0"},
    ("q1", "b"): {"q2"},
    ("q2", "b"): {"q3"},
})
q0_afn = "q0"
finais_afn = {"q3"}
alfabeto = {"a", "b"}


def determinizar(afn_transicoes, q0, finais, alfabeto):
    """Constrói AFD equivalente por subconjuntos (BFS)."""
    afd_transicoes = {}
    afd_finais = set()
    estado_inicial = frozenset({q0})
    fila = [estado_inicial]
    visitados = [estado_inicial]  # lista: preserva a ordem de descoberta (BFS)

    while fila:
        C = fila.pop(0)
        if any(q in finais for q in C):
            afd_finais.add(C)
        for a in sorted(alfabeto):
            D = frozenset({p for q in C for p in afn_transicoes.get((q, a), set())})
            if not D:
                continue
            afd_transicoes[(C, a)] = D
            if D not in visitados:
                visitados.append(D)
                fila.append(D)

    return afd_transicoes, estado_inicial, afd_finais, visitados


def fmt(conjunto):
    """Conjunto em ordem alfabética: {q0, q1} (set do Python não tem ordem fixa)."""
    return "{" + ", ".join(sorted(conjunto)) + "}"


if __name__ == "__main__":
    trans, q0_afd, finais_afd, estados = determinizar(afn, q0_afn, finais_afn, alfabeto)
    print(f"AFD: {len(estados)} estados, {len(finais_afd)} finais")
    for C in estados:
        final = " (final)" if C in finais_afd else ""
        print(f"  {fmt(C)}{final}")
    print("\nTransições:")
    for (C, a), D in trans.items():
        print(f"  δ({fmt(C)}, {a}) = {fmt(D)}")
