#!/usr/bin/env python3
"""TCC1101-SimuladorAP.py | Aula 11
Simulador de Autômato de Pilha (AP) com trace da pilha.
Reconhece aⁿbⁿ (n>=1) por estado final.
AP: M = ({q0,q1,q2}, {a,b}, {Z,A}, δ, q0, Z, {q2})
δ(q0,a,Z) = (q0,AZ)    -- empilha A
δ(q0,a,A) = (q0,AA)    -- empilha A
δ(q0,b,A) = (q1,A)     -- troca para q1 (consome A no topo lendo b)
δ(q1,b,A) = (q1,ε)     -- desempilha A
δ(q1,ε,Z) = (q2,Z)     -- aceita (pilha só com Z)
"""
from typing import List, Tuple


def simular_ap(palavra: str) -> Tuple[bool, List[str]]:
    """Simula o AP. Retorna (aceita, trace)."""
    trace: List[str] = []
    pilha: List[str] = ["Z"]  # fundo da pilha
    estado = "q0"
    i = 0
    trace.append(f"Início: estado={estado}, pilha={pilha}, entrada='{palavra}'")

    while i < len(palavra):
        c = palavra[i]
        topo = pilha[-1] if pilha else "ε"

        if estado == "q0" and c == "a" and topo == "Z":
            pilha.append("A")
            trace.append(f"δ(q0,a,Z)→(q0,AZ) | lê '{c}' | pilha={pilha}")
            i += 1
        elif estado == "q0" and c == "a" and topo == "A":
            pilha.append("A")
            trace.append(f"δ(q0,a,A)→(q0,AA) | lê '{c}' | pilha={pilha}")
            i += 1
        elif estado == "q0" and c == "b" and topo == "A":
            estado = "q1"
            trace.append(f"δ(q0,b,A)→(q1,A) | lê '{c}' | pilha={pilha}")
            i += 1
        elif estado == "q1" and c == "b" and topo == "A":
            pilha.pop()
            trace.append(f"δ(q1,b,A)→(q1,ε) | lê '{c}' | pilha={pilha}")
            i += 1
        else:
            trace.append(f"SEM transição: estado={estado}, c='{c}', topo={topo} → REJEITA")
            return False, trace

    # Transição ε para q2
    topo = pilha[-1] if pilha else "ε"
    if estado == "q1" and topo == "Z":
        estado = "q2"
        trace.append(f"δ(q1,ε,Z)→(q2,Z) | lê ε | pilha={pilha}")

    aceita = estado == "q2"
    trace.append(f"Fim: estado={estado} → {'ACEITA' if aceita else 'REJEITA'}")
    return aceita, trace


if __name__ == "__main__":
    testes = ["ab", "aabb", "aaabbb", "aab", "abb", "ba", ""]
    print(f"{'Palavra':<12} {'Resultado'}")
    print("-" * 30)
    for w in testes:
        ok, _ = simular_ap(w)
        print(f"{w or 'ε':<12} {'ACEITA' if ok else 'REJEITA'}")

    print("\n=== Trace detalhado para 'aabb' ===")
    _, trace = simular_ap("aabb")
    for linha in trace:
        print(linha)
