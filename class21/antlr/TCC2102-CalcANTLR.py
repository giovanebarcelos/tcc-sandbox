#!/usr/bin/env python3
"""TCC2102-CalcANTLR.py | Aula 21
Driver Python da calculadora ANTLR (TCC2102Calc.g4) com Visitor — Slide 8b.
Instalação: pip install antlr4-python3-runtime==4.13.1
Build:      antlr4 -Dlanguage=Python3 -visitor -no-listener TCC2102Calc.g4
Uso:        python3 TCC2102-CalcANTLR.py entrada.txt
"""
import sys
from antlr4 import FileStream, CommonTokenStream
from TCC2102CalcLexer import TCC2102CalcLexer
from TCC2102CalcParser import TCC2102CalcParser
from TCC2102CalcVisitor import TCC2102CalcVisitor

class Avaliador(TCC2102CalcVisitor):
    def visitCalc(self, ctx):       return [self.visit(l) for l in ctx.linha()]
    def visitLinha(self, ctx):      return self.visit(ctx.expr())
    def visitAdd(self, ctx):        return self.visit(ctx.expr()) + self.visit(ctx.term())
    def visitSub(self, ctx):        return self.visit(ctx.expr()) - self.visit(ctx.term())
    def visitExprTerm(self, ctx):   return self.visit(ctx.term())
    def visitMul(self, ctx):        return self.visit(ctx.term()) * self.visit(ctx.factor())
    def visitDiv(self, ctx):        return self.visit(ctx.term()) / self.visit(ctx.factor())
    def visitTermFactor(self, ctx): return self.visit(ctx.factor())
    def visitParens(self, ctx):     return self.visit(ctx.expr())
    def visitNumber(self, ctx):     return float(ctx.NUM().getText())

entrada = FileStream(sys.argv[1], encoding="utf-8")
lexer   = TCC2102CalcLexer(entrada)
parser  = TCC2102CalcParser(CommonTokenStream(lexer))
arvore  = parser.calc()                        # regra inicial da gramática
print(arvore.toStringTree(recog=parser))       # árvore sintática (opcional)
for valor in Avaliador().visit(arvore):
    print("Resultado:", valor)
