#!/usr/bin/env python3
"""TCC0901-ReconhecedorAnBn.py | Aula 09
Reconhece aⁿbⁿ usando contador — mostra que NÃO é autômato finito.
"""
def reconhece_anbn(palavra: str) -> bool:
    i = 0
    while i < len(palavra) and palavra[i] == 'a':
        i += 1
    n_a = i
    while i < len(palavra) and palavra[i] == 'b':
        i += 1
    n_b = i - n_a
    return i == len(palavra) and n_a == n_b and n_a > 0

def derivar_glc(n: int):
    """Deriva aⁿbⁿ usando GLC: S → aSb | ε"""
    print(f"S", end="")
    for _ in range(n):
        print(f" ⇒ aSb", end="")
    print(f" ⇒ aⁿbⁿ" if n > 0 else " ⇒ ε")

if __name__ == "__main__":
    testes = ["ab", "aabb", "aaabbb", "aab", "abb", "aba"]
    print(f"{'Palavra':<12} {'Resultado'}")
    for w in testes:
        print(f"{w:<12} {'ACEITA' if reconhece_anbn(w) else 'REJEITADA'}")
    print("\nDerivação GLC para a³b³:")
    derivar_glc(3)
