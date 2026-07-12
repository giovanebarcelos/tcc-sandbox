#!/usr/bin/env python3
"""TCC0501-TestadorLote.py | Aula 05
Testador em lote: lê um arquivo de palavras e testa cada uma em um AFD.
AFD exemplo: palavras sobre {a,b} que contêm aa ou bb como subpalavra.
"""
import sys


# AFD: M = ({a,b}, {q0,q1,q2,qf}, δ, q0, {qf})
TRANSICOES = {
    ("q0", "a"): "q1", ("q0", "b"): "q2",
    ("q1", "a"): "qf", ("q1", "b"): "q2",
    ("q2", "a"): "q1", ("q2", "b"): "qf",
    ("qf", "a"): "qf", ("qf", "b"): "qf",
}
ESTADO_INICIAL = "q0"
ESTADOS_FINAIS = {"qf"}


def aceita(palavra: str) -> bool:
    estado = ESTADO_INICIAL
    for s in palavra:
        estado = TRANSICOES.get((estado, s))
        if estado is None:
            return False
    return estado in ESTADOS_FINAIS


def testar_arquivo(caminho: str):
    """Lê palavras de um arquivo (uma por linha) e testa cada uma."""
    with open(caminho) as f:
        for linha in f:
            palavra = linha.strip()
            if not palavra:
                continue
            resultado = "ACEITA" if aceita(palavra) else "REJEITADA"
            print(f"{palavra}: {resultado}")


if __name__ == "__main__":
    if len(sys.argv) > 1:
        testar_arquivo(sys.argv[1])
    else:
        # Modo demo com palavras embutidas
        print("=== Testador em Lote (demo) ===")
        palavras = ["abba", "aba", "bab", "aabb", "abab",
                    "baa", "bba", "aa", "bb", "ab"]
        print(f"{'Palavra':<12} {'Resultado'}")
        print("-" * 28)
        for w in palavras:
            print(f"{w:<12} {'ACEITA' if aceita(w) else 'REJEITADA'}")
        print()
        print("Uso: python3 TCC0501-TestadorLote.py <arquivo.txt>")
