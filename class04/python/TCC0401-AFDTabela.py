#!/usr/bin/env python3
"""TCC0401-AFDTabela.py | Aula 04
Simulador de AFD por tabela de transição.
AFD: palavras sobre {a,b} que contêm aa ou bb como subpalavra.

M = ({a,b}, {q0,q1,q2,qf}, δ, q0, {qf})
"""

transicoes = {
    ("q0", "a"): "q1", ("q0", "b"): "q2",
    ("q1", "a"): "qf", ("q1", "b"): "q2",
    ("q2", "a"): "q1", ("q2", "b"): "qf",
    ("qf", "a"): "qf", ("qf", "b"): "qf",
}

estado_inicial = "q0"
estados_finais = {"qf"}


def aceita(palavra: str) -> bool:
    """Retorna True se a palavra é aceita pelo AFD."""
    estado = estado_inicial
    for simbolo in palavra:
        estado = transicoes.get((estado, simbolo))
        if estado is None:
            return False
    return estado in estados_finais


def testar(palavras):
    print(f"{'Palavra':<12} {'Resultado':<10} {'Estados'}")
    print("-" * 55)
    for w in palavras:
        estados = [estado_inicial]
        estado = estado_inicial
        for s in w:
            estado = transicoes.get((estado, s))
            if estado is None:
                break
            estados.append(estado)
        resultado = "ACEITA" if aceita(w) else "REJEITADA"
        trace = " → ".join(estados)
        print(f"{w:<12} {resultado:<10} {trace}")


if __name__ == "__main__":
    palavras = ["abba", "aba", "bab", "aabb", "abab",
                "baa", "bba", "aa", "bb", "ab"]
    testar(palavras)
