#!/usr/bin/env python3
"""TCC1802-ParserRecursivo.py | Aula 18
Parser descendente recursivo para expressões.
Gramática LL(1):
  E  → T E'
  E' → + T E' | ε
  T  → F T'
  T' → * F T' | ε
  F  → ( E ) | NUM
"""
from typing import List, Optional


class ParserRecursivo:
    def __init__(self, tokens: List[str]):
        self.tokens = tokens + ["$"]
        self.pos = 0

    def peek(self) -> str:
        return self.tokens[self.pos]

    def consume(self) -> str:
        t = self.tokens[self.pos]
        self.pos += 1
        return t

    def parse(self) -> int:
        return self.E()

    def E(self) -> int:
        val = self.T()
        return self.E_linha(val)

    def E_linha(self, inherited: int) -> int:
        if self.peek() == "+":
            self.consume()
            val = self.T()
            return self.E_linha(inherited + val)
        return inherited  # ε

    def T(self) -> int:
        val = self.F()
        return self.T_linha(val)

    def T_linha(self, inherited: int) -> int:
        if self.peek() == "*":
            self.consume()
            val = self.F()
            return self.T_linha(inherited * val)
        return inherited  # ε

    def F(self) -> int:
        t = self.peek()
        if t == "(":
            self.consume()
            val = self.E()
            if self.peek() != ")":
                raise SyntaxError(f"Esperado ')', obtido {self.peek()!r}")
            self.consume()
            return val
        if t.isdigit() or (t and t[0].isdigit()):
            self.consume()
            return int(t)
        raise SyntaxError(f"Token inesperado em F: {t!r}")


def tokenize(expr: str) -> List[str]:
    """Tokeniza: NUM, +, *, (, )"""
    tokens = []
    i = 0
    while i < len(expr):
        if expr[i].isspace():
            i += 1
            continue
        if expr[i].isdigit():
            j = i
            while j < len(expr) and expr[j].isdigit():
                j += 1
            tokens.append(expr[i:j])
            i = j
        elif expr[i] in "+*()":
            tokens.append(expr[i])
            i += 1
        else:
            raise SyntaxError(f"Caractere inválido: {expr[i]!r}")
    return tokens


if __name__ == "__main__":
    testes = ["3 + 4 * 2", "(3 + 4) * 2", "1 + 2 + 3", "2 * 3 + 4 * 5", "((1 + 2) * 3)"]
    for expr in testes:
        tokens = tokenize(expr)
        resultado = ParserRecursivo(tokens).parse()
        print(f"{expr} = {resultado}")
