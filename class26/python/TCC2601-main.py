#!/usr/bin/env python3
"""TCC2601-main.py | Aula 26
Driver Python para a gramática ANTLR TCC2601Expr.g4.
Avalia expressões usando Visitor — mesmo comportamento do driver Java.
Build: antlr4 -Dlanguage=Python3 -visitor TCC2601Expr.g4
       python3 TCC2601-main.py "3 + 4 * 2"
Instalação: pip install antlr4-python3-runtime==4.13.1
"""
import sys

from antlr4 import CommonTokenStream, InputStream

# Classes geradas pelo ANTLR (rode o comando de build acima antes)
from TCC2601ExprLexer import TCC2601ExprLexer
from TCC2601ExprParser import TCC2601ExprParser
from TCC2601ExprVisitor import TCC2601ExprVisitor


class EvalVisitor(TCC2601ExprVisitor):
    """Visitor que avalia expressões — espelho do driver Java."""

    def visitProg(self, ctx):
        return self.visit(ctx.expr())

    def visitAdd(self, ctx):
        return self.visit(ctx.expr(0)) + self.visit(ctx.expr(1))

    def visitMul(self, ctx):
        return self.visit(ctx.expr(0)) * self.visit(ctx.expr(1))

    def visitParens(self, ctx):
        return self.visit(ctx.expr())

    def visitNumber(self, ctx):
        return float(ctx.NUM().getText())


def main():
    entrada = sys.argv[1] if len(sys.argv) > 1 else "3 + 4 * 2"
    print("=== ANTLR Python Driver ===")
    print(f"Expressão: {entrada}")
    lexer = TCC2601ExprLexer(InputStream(entrada))
    parser = TCC2601ExprParser(CommonTokenStream(lexer))
    tree = parser.prog()
    print(f"Resultado: {EvalVisitor().visit(tree)}")
    print(f"Árvore: {tree.toStringTree(recog=parser)}")


if __name__ == "__main__":
    main()
