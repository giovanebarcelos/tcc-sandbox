#!/usr/bin/env python3
"""TCC0201-GeradorDePalavras.py | Aula 02
Gerador de palavras para a gramática dos números naturais:
  (1) N -> D
  (2) N -> DN
  (3) D -> 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9

Cada passo de derivacao e um par (variavel, indice_da_producao).
Para D, o indice_da_producao e o proprio digito (0..9).
Para N, indice 0 = producao (1) "D", indice 1 = producao (2) "DN".
"""

producoes = {
    "N": [["D"], ["D", "N"]],
    "D": [[c] for c in "0123456789"],
}


def deriva(forma, passos):
    """Aplica uma sequencia de (variavel, indice_da_producao) e imprime cada passo."""
    forma = list(forma)
    print("".join(forma))
    for var, regra in passos:
        i = forma.index(var)
        forma = forma[:i] + list(producoes[var][regra]) + forma[i + 1:]
        print("=>", "".join(forma))
    return "".join(forma)


def gerar_para_numero(numero: str):
    """Gera automaticamente a sequencia de passos que deriva a string `numero`."""
    passos = []
    for i, digito in enumerate(numero):
        ultimo = (i == len(numero) - 1)
        passos.append(("N", 0 if ultimo else 1))
        passos.append(("D", int(digito)))
    return passos


if __name__ == "__main__":
    print("=== Derivação de 243 (passos manuais) ===")
    resultado = deriva(["N"], [("N", 1), ("D", 2), ("N", 1), ("D", 4), ("N", 0), ("D", 3)])
    assert resultado == "243"

    print()
    print("=== Derivação automática de 77 ===")
    resultado77 = deriva(["N"], gerar_para_numero("77"))
    assert resultado77 == "77"

    print()
    print("=== Derivação automática de 5890 ===")
    resultado5890 = deriva(["N"], gerar_para_numero("5890"))
    assert resultado5890 == "5890"
