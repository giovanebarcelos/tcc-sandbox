#!/usr/bin/env python3
"""TCC2302-Visitor.py | Aula 23
Padrao Visitor isolado (versao didatica enxuta do Slide 8): separa a
estrutura da AST (BinOp/Num) do comportamento (avaliacao), permitindo
adicionar novas operacoes sem alterar as classes dos nos.
"""
from dataclasses import dataclass


class ASTVisitor:
    def visit_num(self, node):
        pass

    def visit_binop(self, node):
        pass


@dataclass
class Num:
    value: float

    def accept(self, visitor):
        return visitor.visit_num(self)


@dataclass
class BinOp:
    op: str
    left: "Num | BinOp"
    right: "Num | BinOp"

    def accept(self, visitor):
        return visitor.visit_binop(self)


class EvalVisitor(ASTVisitor):
    def visit_num(self, node):
        return node.value

    def visit_binop(self, node):
        l = node.left.accept(self)
        r = node.right.accept(self)
        return l + r if node.op == '+' else l * r


if __name__ == "__main__":
    # (2 + 3) * 4
    ast = BinOp("*", BinOp("+", Num(2), Num(3)), Num(4))
    resultado = ast.accept(EvalVisitor())
    print(f"(2 + 3) * 4 = {resultado}")
