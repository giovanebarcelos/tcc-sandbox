#!/usr/bin/env python3
"""TCC2601-main.py | Aula 26
Driver Python para a gramática ANTLR TCC2601-Expr.g4.
Avalia expressões usando Visitor — mesmo comportamento do driver Java.
Build: antlr4 -Dlanguage=Python3 -visitor TCC2601-Expr.g4
       python3 TCC2601-main.py "3 + 4 * 2"
"""
import sys
from antlr4 import *
# Descomente após gerar o parser com antlr4:
# from TCC2601ExprLexer import TCC2601ExprLexer
# from TCC2601ExprParser import TCC2601ExprParser
# from TCC2601ExprVisitor import TCC2601ExprVisitor


class EvalVisitor:  # (TCC2601ExprVisitor)
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

    def visit(self, ctx):
        """Despacho manual (em produção, herdar de TCC2601ExprVisitor)."""
        method = getattr(self, f"visit{type(ctx).__name__[:-7]}", None)
        if method:
            return method(ctx)
        return None


def main():
    entrada = sys.argv[1] if len(sys.argv) > 1 else "3 + 4 * 2"
    print("=== ANTLR Python Driver ===")
    print(f"Expressão: {entrada}")

    # Descomente após gerar o parser:
    # input_stream = InputStream(entrada)
    # lexer = TCC2601ExprLexer(input_stream)
    # tokens = CommonTokenStream(lexer)
    # parser = TCC2601ExprParser(tokens)
    # tree = parser.prog()
    # visitor = EvalVisitor()
    # result = visitor.visit(tree)
    # print(f"Resultado: {result}")
    # print(f"Árvore: {tree.toStringTree(recog=parser)}")

    # Demonstração sem o parser gerado:
    print("(gere o parser com: antlr4 -Dlanguage=Python3 -visitor TCC2601-Expr.g4)")


if __name__ == "__main__":
    main()
