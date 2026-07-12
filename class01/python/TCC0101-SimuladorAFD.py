#!/usr/bin/env python3
# TCC0101-SimuladorAFD.py
# Aula 01 - Teoria da Computação e Compiladores
# Simulador de Autômato Finito Determinístico (AFD).
# AFD de exemplo: aceita palavras sobre {0,1} com número PAR de '1's.
# Mesmo exemplo implementado em Java: TCC0101-SimuladorAFD.java

class SimuladorAFD:
    def __init__(self, estados, alfabeto, transicoes, inicial, finais):
        self.estados = estados          # conjunto de estados
        self.alfabeto = alfabeto        # símbolos válidos
        self.transicoes = transicoes    # dict: (estado, simbolo) -> estado
        self.inicial = inicial          # estado inicial
        self.finais = finais            # conjunto de estados finais

    def aceita(self, palavra):
        estado = self.inicial
        print(f"Palavra: '{palavra}'")
        for simbolo in palavra:
            if simbolo not in self.alfabeto:
                print(f"  símbolo '{simbolo}' não pertence ao alfabeto -> REJEITA")
                return False
            proximo = self.transicoes[(estado, simbolo)]
            print(f"  δ({estado}, {simbolo}) = {proximo}")
            estado = proximo
        aceita = estado in self.finais
        print(f"  estado final: {estado} -> {'ACEITA' if aceita else 'REJEITA'}")
        return aceita


def main():
    # AFD: número par de '1's  (q0 = par [final], q1 = ímpar)
    afd = SimuladorAFD(
        estados={"q0", "q1"},
        alfabeto={"0", "1"},
        transicoes={
            ("q0", "0"): "q0",
            ("q0", "1"): "q1",
            ("q1", "0"): "q1",
            ("q1", "1"): "q0",
        },
        inicial="q0",
        finais={"q0"},
    )

    for palavra in ["", "0", "1", "11", "1010", "10101"]:
        afd.aceita(palavra)
        print()


if __name__ == "__main__":
    main()
