#!/usr/bin/env python3
"""TCC0601-SimuladorAFN.py | Aula 06
Simulador de AFN por conjunto de estados ativos.
AFN exemplo: palavras sobre {a,b} que contêm aa ou bb.
"""
from collections import defaultdict

# AFN: δ(q, s) = conjunto de destinos
transicoes = defaultdict(set, {
    ("q0", "a"): {"q0", "q1"},
    ("q0", "b"): {"q0", "q2"},
    ("q1", "a"): {"qf"},
    ("q2", "b"): {"qf"},
    ("qf", "a"): {"qf"},
    ("qf", "b"): {"qf"},
})

q0 = "q0"
finais = {"qf"}


def simular(palavra: str) -> bool:
    """Retorna True se o AFN aceita a palavra (existe caminho vencedor)."""
    ativos = {q0}
    for simbolo in palavra:
        novos = set()
        for q in ativos:
            novos.update(transicoes.get((q, simbolo), set()))
        ativos = novos
        if not ativos:
            return False
    return bool(ativos & finais)


def simular_com_trace(palavra: str):
    """Simula com trace dos conjuntos de estados ativos."""
    ativos = {q0}
    print(f"Início: {{{', '.join(sorted(ativos))}}}")
    for simbolo in palavra:
        novos = set()
        for q in ativos:
            destinos = transicoes.get((q, simbolo), set())
            if destinos:
                print(f"  δ({q}, {simbolo}) = {{{', '.join(sorted(destinos))}}}")
            novos.update(destinos)
        ativos = novos
        print(f"  Após '{simbolo}': {{{', '.join(sorted(ativos))}}}")
        if not ativos:
            print("  → sem estados ativos (REJEITA)")
            return False
    aceita = bool(ativos & finais)
    print(f"Final: {{{', '.join(sorted(ativos))}}} → {'ACEITA' if aceita else 'REJEITADA'}")
    return aceita


if __name__ == "__main__":
    palavras = ["abba", "aba", "aa", "bb", "ab"]
    print(f"{'Palavra':<10} {'Resultado'}")
    print("-" * 25)
    for w in palavras:
        print(f"{w:<10} {'ACEITA' if simular(w) else 'REJEITADA'}")

    print("\n=== Trace detalhado: abba ===")
    simular_com_trace("abba")
    print("\n=== Trace detalhado: aba ===")
    simular_com_trace("aba")
