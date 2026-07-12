#!/usr/bin/env python3
"""TCC1201-SimuladorMT.py | Aula 12
Simulador de Máquina de Turing (MT) de uma fita com trace.
Reconhece aⁿbⁿcⁿ (n>=1) — linguagem sensível ao contexto.
Fita: ...BBB a a a b b b c c c BBB...
Estados: q0 (lê a→X, vai q1), q1 (lê a/b→dir), q1 lê b→Y vai q2,
q2 (lê b/c→dir), q2 lê c→Z vai q3, q3 (volta até X), q3 lê X→dir q0,
q0 lê Y→dir q4, q4 (lê Y/Z→dir), q4 lê B→ACEITA.
"""
from typing import List, Tuple


def simular_mt(entrada: str) -> Tuple[bool, List[str]]:
    """Simula a MT. Retorna (aceita, trace)."""
    # Fita como lista; branco = 'B'
    fita: List[str] = list(entrada) if entrada else []
    fita.append("B")  # garante pelo menos um branco à direita
    cabeca = 0
    estado = "q0"
    trace: List[str] = []
    max_passos = 10000
    passos = 0

    def fita_str() -> str:
        return "".join(fita)

    trace.append(f"Início: estado={estado}, fita={fita_str()}, cabeça={cabeca}")

    while passos < max_passos:
        passos += 1
        simbolo = fita[cabeca] if cabeca < len(fita) else "B"
        if cabeca >= len(fita):
            fita.append("B")

        # q0: lê a → escreve X, vai q1, direita
        if estado == "q0" and simbolo == "a":
            fita[cabeca] = "X"
            estado = "q1"
            cabeca += 1
            trace.append(f"δ(q0,a)→(q1,X,R) | fita={fita_str()}")
        # q0: lê Y → direita, vai q4 (verificando que todos os a's foram consumidos)
        elif estado == "q0" and simbolo == "Y":
            estado = "q4"
            cabeca += 1
            trace.append(f"δ(q0,Y)→(q4,Y,R) | fita={fita_str()}")
        # q1: lê a ou b → direita
        elif estado == "q1" and simbolo in ("a", "b"):
            cabeca += 1
            trace.append(f"δ(q1,{simbolo})→(q1,{simbolo},R) | fita={fita_str()}")
        # q1: lê b → escreve Y, vai q2, direita
        elif estado == "q1" and simbolo == "b":
            fita[cabeca] = "Y"
            estado = "q2"
            cabeca += 1
            trace.append(f"δ(q1,b)→(q2,Y,R) | fita={fita_str()}")
        # q2: lê b ou c → direita
        elif estado == "q2" and simbolo in ("b", "c"):
            cabeca += 1
            trace.append(f"δ(q2,{simbolo})→(q2,{simbolo},R) | fita={fita_str()}")
        # q2: lê c → escreve Z, vai q3, direita
        elif estado == "q2" and simbolo == "c":
            fita[cabeca] = "Z"
            estado = "q3"
            cabeca += 1
            trace.append(f"δ(q2,c)→(q3,Z,R) | fita={fita_str()}")
        # q3: volta à esquerda até X
        elif estado == "q3" and simbolo in ("a", "b", "c", "Y", "Z"):
            cabeca -= 1
            trace.append(f"δ(q3,{simbolo})→(q3,{simbolo},L) | fita={fita_str()}")
        # q3: lê X → direita, volta q0
        elif estado == "q3" and simbolo == "X":
            estado = "q0"
            cabeca += 1
            trace.append(f"δ(q3,X)→(q0,X,R) | fita={fita_str()}")
        # q4: lê Y ou Z → direita
        elif estado == "q4" and simbolo in ("Y", "Z"):
            cabeca += 1
            trace.append(f"δ(q4,{simbolo})→(q4,{simbolo},R) | fita={fita_str()}")
        # q4: lê B → ACEITA
        elif estado == "q4" and simbolo == "B":
            trace.append(f"δ(q4,B)→ACEITA | fita={fita_str()}")
            return True, trace
        else:
            trace.append(f"SEM transição: estado={estado}, símbolo={simbolo} → REJEITA")
            return False, trace

    trace.append("Limite de passos excedido → REJEITA")
    return False, trace


if __name__ == "__main__":
    testes = ["abc", "aabbcc", "aaabbbccc", "aabbc", "abcc", "aabbc"]
    print(f"{'Palavra':<14} {'Resultado'}")
    print("-" * 30)
    for w in testes:
        ok, _ = simular_mt(w)
        print(f"{w:<14} {'ACEITA' if ok else 'REJEITA'}")

    print("\n=== Trace detalhado para 'abc' ===")
    _, trace = simular_mt("abc")
    for linha in trace:
        print(linha)
