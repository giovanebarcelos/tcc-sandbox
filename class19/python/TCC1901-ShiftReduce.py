#!/usr/bin/env python3
"""TCC1901-ShiftReduce.py | Aula 19
Simulador de análise sintática shift-reduce (bottom-up).
Gramática:
  E → E + E
  E → E * E
  E → ( E )
  E → id
"""
from typing import List, Tuple


# Produções (lado esquerdo, lado direito)
producoes = [
    ("E", ["E", "+", "E"]),
    ("E", ["E", "*", "E"]),
    ("E", ["(", "E", ")"]),
    ("E", ["id"]),
]


def reduzir(pilha: List[str]) -> bool:
    """Tenta reduzir o topo da pilha. Retorna True se reduziu."""
    for esq, dir_ in producoes:
        if len(pilha) >= len(dir_) and pilha[-len(dir_):] == dir_:
            del pilha[-len(dir_):]
            pilha.append(esq)
            return True
    return False


def shift_reduce(entrada: List[str]) -> Tuple[bool, List[str]]:
    """Executa análise shift-reduce. Retorna (aceita, trace)."""
    pilha: List[str] = []
    entrada = entrada + ["$"]
    i = 0
    trace: List[str] = []
    trace.append(f"Início: pilha={pilha}, entrada={entrada[i:]}")

    while True:
        # Tenta reduzir o máximo possível
        while reduzir(pilha):
            trace.append(f"Reduce: pilha={pilha}, entrada={entrada[i:]}")

        # Verifica aceitação: pilha = [E], entrada = [$]
        if pilha == ["E"] and entrada[i] == "$":
            trace.append("ACEITA")
            return True, trace

        # Shift
        if entrada[i] != "$":
            pilha.append(entrada[i])
            i += 1
            trace.append(f"Shift:  pilha={pilha}, entrada={entrada[i:]}")
        else:
            trace.append("REJEITA (entrada esgotada sem aceitação)")
            return False, trace


def tokenize(expr: str) -> List[str]:
    tokens = []
    i = 0
    while i < len(expr):
        if expr[i].isspace():
            i += 1
            continue
        if expr[i].isdigit():
            tokens.append("id")
            while i < len(expr) and expr[i].isdigit():
                i += 1
        elif expr[i] in "+*()":
            tokens.append(expr[i])
            i += 1
        else:
            raise SyntaxError(f"Caractere inválido: {expr[i]!r}")
    return tokens


if __name__ == "__main__":
    testes = ["id + id", "id * id + id", "( id + id ) * id", "id +"]
    for expr in testes:
        tokens = tokenize(expr)
        ok, trace = shift_reduce(tokens)
        print(f"{'ACEITA' if ok else 'REJEITA'}: {expr}")
        if expr == "id + id":
            for linha in trace:
                print(f"  {linha}")
