#!/usr/bin/env python3
"""TCC1601-ScannerManual.py | Aula 16
Scanner manual implementado como AFD programado.
Reconhece: NUM, ID, OP (+, -, *, /, =), PAREN, KEYWORD (if, else, while, print).
"""
from dataclasses import dataclass
from typing import List


@dataclass
class Token:
    tipo: str
    valor: str
    linha: int
    col: int

    def __repr__(self):
        return f"{self.tipo}({self.valor!r})@{self.linha}:{self.col}"


KEYWORDS = {"if", "else", "while", "print", "int", "float"}


def scanner(fonte: str) -> List[Token]:
    """Scanner manual — o AFD da análise léxica."""
    tokens: List[Token] = []
    i = 0
    linha = 1
    col = 1
    n = len(fonte)

    while i < n:
        c = fonte[i]

        # Espaços e quebras de linha
        if c == "\n":
            i += 1; linha += 1; col = 1; continue
        if c in " \t\r":
            i += 1; col += 1; continue

        # Comentário # até fim da linha
        if c == "#":
            while i < n and fonte[i] != "\n":
                i += 1; col += 1
            continue

        inicio_col = col

        # Números (NUM)
        if c.isdigit():
            j = i
            while j < n and fonte[j].isdigit():
                j += 1
            # parte decimal
            if j < n and fonte[j] == ".":
                j += 1
                while j < n and fonte[j].isdigit():
                    j += 1
            tokens.append(Token("NUM", fonte[i:j], linha, inicio_col))
            col += j - i
            i = j
            continue

        # Identificadores e palavras-chave
        if c.isalpha() or c == "_":
            j = i
            while j < n and (fonte[j].isalnum() or fonte[j] == "_"):
                j += 1
            lexema = fonte[i:j]
            tipo = "KEYWORD" if lexema in KEYWORDS else "ID"
            tokens.append(Token(tipo, lexema, linha, inicio_col))
            col += j - i
            i = j
            continue

        # Operadores
        if c in "+-*/=":
            # operador composto ==, <=, >=, != (simplificado: só ==)
            if c == "=" and i + 1 < n and fonte[i + 1] == "=":
                tokens.append(Token("OP", "==", linha, inicio_col))
                i += 2; col += 2; continue
            tokens.append(Token("OP", c, linha, inicio_col))
            i += 1; col += 1; continue

        # Parênteses e pontuação
        if c in "(){};":
            tokens.append(Token("PUNCT", c, linha, inicio_col))
            i += 1; col += 1; continue

        raise SyntaxError(f"Caractere inválido {c!r} na linha {linha}, coluna {col}")

    tokens.append(Token("EOF", "", linha, col))
    return tokens


if __name__ == "__main__":
    fonte = """int x = 42
if x == 42
    print(x)
else
    x = x + 1"""
    print(f"=== Fonte ===\n{fonte}\n")
    print("=== Tokens ===")
    for t in scanner(fonte):
        print(f"  {t}")
