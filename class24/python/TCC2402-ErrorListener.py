#!/usr/bin/env python3
"""TCC2402-ErrorListener.py | Aula 24
ErrorListener customizado para ANTLR (Python) — mensagens amigáveis com linha/coluna.
Usa a gramática TCC2402-IfElse.g4 gerada com: antlr4 -Dlanguage=Python3 -visitor TCC2402-IfElse.g4
"""
import sys

# Importa as classes geradas pelo ANTLR (descomente após gerar)
# from TCC2402IfElseLexer import TCC2402IfElseLexer
# from TCC2402IfElseParser import TCC2402IfElseParser
from antlr4 import *
from antlr4.error.ErrorListener import ErrorListener


class MeuErrorListener(ErrorListener):
    """ErrorListener customizado: mensagens amigáveis com linha/coluna."""

    def __init__(self):
        super().__init__()
        self.erros = []

    def syntaxError(self, recognizer, offendingSymbol, line, column, msg, e):
        erro = f"Erro de sintaxe na linha {line}, coluna {column}: {msg}"
        self.erros.append(erro)
        print(f"  ❌ {erro}")


def analisar(fonte: str):
    """Analisa `fonte` e reporta erros sintáticos."""
    # Descomente as linhas abaixo após gerar o parser com antlr4
    # input_stream = InputStream(fonte)
    # lexer = TCC2402IfElseLexer(input_stream)
    # tokens = CommonTokenStream(lexer)
    # parser = TCC2402IfElseParser(tokens)
    # listener = MeuErrorListener()
    # parser.removeErrorListeners()
    # parser.addErrorListener(listener)
    # parser.program()
    # return len(listener.erros) == 0
    print(f"  (análise de: {fonte!r})")
    return True


if __name__ == "__main__":
    print("=== Código correto ===")
    analisar("x = 3; if (x > 2) { print(x); } else { print(0); }")

    print("\n=== Código com erro (falta ';') ===")
    analisar("x = 3 if (x > 2) { print(x); }")

    print("\n=== Código com erro (falta ')') ===")
    analisar("if (x > 2 { print(x); }")

    print("\n=== ErrorListener demonstra mensagens amigáveis com linha/coluna ===")
    print("Compare com o símbolo `error` do JCup (recuperação mais rígida).")
