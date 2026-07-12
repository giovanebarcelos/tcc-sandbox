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
    visitados = {estado_inicial}

    while fila:
        C = fila.pop(0)
        if any(q in finais for q in C):
            afd_finais.add(C)
        for a in alfabeto:
            D = frozenset({p for q in C for p in afn_transicoes.get((q, a), set())})
            if not D:
                continue
            afd_transicoes[(C, a)] = D
            if D not in visitados:
                visitados.add(D)
                fila.append(D)

    return afd_transicoes, estado_inicial, afd_finais, visitados


if __name__ == "__main__":
    trans, q0_afd, finais_afd, estados = determinizar(afn, q0_afn, finais_afn, alfabeto)
    print(f"AFD: {len(estados)} estados, {len(finais_afd)} finais")
    for C in sorted(estados, key=lambda s: ("" if any(q in finais_afn for q in C) else "z", sorted(C))):
        final = " (final)" if C in finais_afd else ""
        print(f"  {set(C)}{final}")
    print("\nTransições:")
    for (C, a), D in sorted(trans.items(), key=lambda x: (sorted(x[0][0]), x[0][1])):
        print(f"  δ({set(C)}, {a}) = {set(D)}")
