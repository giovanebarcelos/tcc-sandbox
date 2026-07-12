#!/usr/bin/env python3
"""TCC2301-AST.py | Aula 23
AST (Abstract Syntax Tree) da calculadora com padrões Visitor e Factory.
Hierarquia: No (abstrato) → NumNode, VarNode, BinOpNode, AssignNode, PrintNode
"""
from abc import ABC, abstractmethod
from dataclasses import dataclass
from typing import Any, Dict


# ---------- AST ----------
class No(ABC):
    """Nó base da AST — abstração (SOLID: Dependency Inversion)."""
    @abstractmethod
    def aceitar(self, visitor: "Visitor") -> Any:
        ...


@dataclass
class NumNode(No):
    valor: float

    def aceitar(self, visitor):
        return visitor.visitar_num(self)


@dataclass
class VarNode(No):
    nome: str

    def aceitar(self, visitor):
        return visitor.visitar_var(self)


@dataclass
class BinOpNode(No):
    op: str
    esq: No
    dir: No

    def aceitar(self, visitor):
        return visitor.visitar_binop(self)


@dataclass
class AssignNode(No):
    nome: str
    expr: No

    def aceitar(self, visitor):
        return visitor.visitar_assign(self)


@dataclass
class PrintNode(No):
    expr: No

    def aceitar(self, visitor):
        return visitor.visitar_print(self)


# ---------- Factory ----------
class NoFactory:
    """Factory: centraliza criação de nós (SOLID: Open/Closed)."""
    @staticmethod
    def num(valor): return NumNode(valor)
    @staticmethod
    def var(nome): return VarNode(nome)
    @staticmethod
    def binop(op, esq, dir_): return BinOpNode(op, esq, dir_)
    @staticmethod
    def assign(nome, expr): return AssignNode(nome, expr)
    @staticmethod
    def print_(expr): return PrintNode(expr)


# ---------- Visitor (abstrato) ----------
class Visitor(ABC):
    @abstractmethod
    def visitar_num(self, no: NumNode) -> Any: ...
    @abstractmethod
    def visitar_var(self, no: VarNode) -> Any: ...
    @abstractmethod
    def visitar_binop(self, no: BinOpNode) -> Any: ...
    @abstractmethod
    def visitar_assign(self, no: AssignNode) -> Any: ...
    @abstractmethod
    def visitar_print(self, no: PrintNode) -> Any: ...


# ---------- EvalVisitor (avalia expressões) ----------
class EvalVisitor(Visitor):
    def __init__(self):
        self.tabela: Dict[str, float] = {}

    def visitar_num(self, no):
        return no.valor

    def visitar_var(self, no):
        if no.nome not in self.tabela:
            raise NameError(f"Variável '{no.nome}' não definida")
        return self.tabela[no.nome]

    def visitar_binop(self, no):
        esq = no.esq.aceitar(self)
        dir_ = no.dir.aceitar(self)
        if no.op == "+": return esq + dir_
        if no.op == "-": return esq - dir_
        if no.op == "*": return esq * dir_
        if no.op == "/": return esq / dir_
        raise ValueError(f"Operador inválido: {no.op}")

    def visitar_assign(self, no):
        val = no.expr.aceitar(self)
        self.tabela[no.nome] = val
        return val

    def visitar_print(self, no):
        val = no.expr.aceitar(self)
        print(val)
        return val


# ---------- PrintVisitor (imprime a AST) ----------
class PrintVisitor(Visitor):
    def visitar_num(self, no):
        return str(no.valor)

    def visitar_var(self, no):
        return no.nome

    def visitar_binop(self, no):
        return f"({no.esq.aceitar(self)} {no.op} {no.dir.aceitar(self)})"

    def visitar_assign(self, no):
        return f"{no.nome} = {no.expr.aceitar(self)}"

    def visitar_print(self, no):
        return f"print({no.expr.aceitar(self)})"


if __name__ == "__main__":
    # Constrói AST para: x = 3 + 4 * 2; print(x);
    f = NoFactory
    ast = [
        f.assign("x", f.binop("+", f.num(3), f.binop("*", f.num(4), f.num(2)))),
        f.print_(f.var("x")),
    ]

    print("=== PrintVisitor (representação da AST) ===")
    pv = PrintVisitor()
    for no in ast:
        print(f"  {no.aceitar(pv)}")

    print("\n=== EvalVisitor (avaliação) ===")
    ev = EvalVisitor()
    for no in ast:
        no.aceitar(ev)

    print(f"\nTabela de símbolos: {ev.tabela}")
