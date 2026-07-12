#!/usr/bin/env python3
"""TCC2502-Otimizador.py | Aula 25
Otimizador didático: constant folding e eliminação de código morto.
"""
from typing import List


def constant_folding(tac: List[str]) -> List[str]:
    """Constant folding: avalia subexpressões constantes no TAC."""
    valores = {}
    otimizado = []
    for linha in tac:
        parts = linha.split(" = ", 1)
        if len(parts) != 2:
            otimizado.append(linha)
            continue
        dst, expr = parts
        tokens = expr.split()
        # t1 = 3 + 4 → t1 = 7
        if len(tokens) == 3 and _eh_num(tokens[0]) and _eh_num(tokens[2]):
            a, op, b = float(tokens[0]), tokens[1], float(tokens[2])
            resultado = _aplicar_op(a, op, b)
            if resultado is not None:
                valores[dst] = resultado
                otimizado.append(f"{dst} = {resultado}  (constant folding)")
                continue
        # Substituir variáveis conhecidas
        for i, t in enumerate(tokens):
            if t in valores:
                tokens[i] = str(valores[t])
        otimizado.append(f"{dst} = {' '.join(tokens)}")
    return otimizado


def dead_code_elimination(tac: List[str]) -> List[str]:
    """Eliminação de código morto: remove atribuições cujo destino não é usado."""
    usados = set()
    for linha in tac:
        parts = linha.split(" = ", 1)
        if len(parts) == 2:
            for token in parts[1].split():
                if token.isidentifier() or token.startswith("t"):
                    usados.add(token)
    # Manter atribuições a variáveis usadas ou não-temporárias
    otimizado = []
    for linha in tac:
        parts = linha.split(" = ", 1)
        if len(parts) == 2:
            dst = parts[0]
            if dst.startswith("t") and dst not in usados:
                continue  # código morto
        otimizado.append(linha)
    return otimizado


def _eh_num(s: str) -> bool:
    try:
        float(s)
        return True
    except ValueError:
        return False


def _aplicar_op(a: float, op: str, b: float):
    ops = {"+": a + b, "-": a - b, "*": a * b, "/": a / b if b != 0 else None}
    return ops.get(op)


if __name__ == "__main__":
    tac = [
        "t1 = 3 + 4",
        "t2 = t1 * 2",
        "t3 = 10 - 5",
        "x = t2 + t3",
        "t4 = 100 * 0",  # código morto (x não usa t4)
    ]

    print("=== TAC Original ===")
    for linha in tac:
        print(f"  {linha}")

    print("\n=== Após Constant Folding ===")
    cf = constant_folding(tac)
    for linha in cf:
        print(f"  {linha}")

    print("\n=== Após Dead Code Elimination ===")
    dce = dead_code_elimination(cf)
    for linha in dce:
        print(f"  {linha}")
