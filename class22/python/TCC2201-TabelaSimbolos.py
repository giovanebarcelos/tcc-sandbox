#!/usr/bin/env python3
"""TCC2201-TabelaSimbolos.py | Aula 22
Tabela de símbolos reutilizável para o compilador.
Armazena: nome, tipo, valor.
"""
from dataclasses import dataclass, field
from typing import Any, Dict, Optional


@dataclass
class Simbolo:
    nome: str
    tipo: str = "int"
    valor: Any = None

    def __repr__(self):
        return f"{self.nome}:{self.tipo}={self.valor}"


class TabelaSimbolos:
    def __init__(self):
        self._simbolos: Dict[str, Simbolo] = {}

    def declarar(self, nome: str, tipo: str = "int", valor: Any = None) -> Simbolo:
        if nome in self._simbolos:
            raise NameError(f"Variável '{nome}' já declarada")
        sim = Simbolo(nome, tipo, valor)
        self._simbolos[nome] = sim
        return sim

    def buscar(self, nome: str) -> Optional[Simbolo]:
        return self._simbolos.get(nome)

    def atribuir(self, nome: str, valor: Any) -> Simbolo:
        sim = self._simbolos.get(nome)
        if sim is None:
            raise NameError(f"Variável '{nome}' não declarada")
        sim.valor = valor
        return sim

    def __contains__(self, nome: str) -> bool:
        return nome in self._simbolos

    def __repr__(self):
        return "TabelaSimbolos(" + ", ".join(str(s) for s in self._simbolos.values()) + ")"


if __name__ == "__main__":
    ts = TabelaSimbolos()
    ts.declarar("x", "int", 42)
    ts.declarar("y", "float", 3.14)
    ts.declarar("nome", "string", "Giovane")

    print("=== Tabela após declarações ===")
    print(ts)

    print("\n=== Atribuições ===")
    ts.atribuir("x", 100)
    ts.atribuir("y", 2.71)
    print(ts)

    print("\n=== Buscas ===")
    print(f"  x = {ts.buscar('x')}")
    print(f"  y = {ts.buscar('y')}")

    print("\n=== Erros ===")
    try:
        ts.buscar("z").valor
    except AttributeError:
        print("  z não encontrado (None)")
    try:
        ts.declarar("x")
    except NameError as e:
        print(f"  {e}")
