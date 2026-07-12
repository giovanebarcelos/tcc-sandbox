#!/usr/bin/env python3
"""TCC2501-GeradorCodigo.py | Aula 25
Gera código de três endereços (TAC) e código C a partir de uma AST.
"""
from dataclasses import dataclass
from typing import List


@dataclass
class No:
    pass


@dataclass
class Num(No):
    valor: float


@dataclass
class Var(No):
    nome: str


@dataclass
class BinOp(No):
    op: str
    esq: No
    dir: No


@dataclass
class Assign(No):
    nome: str
    expr: No


@dataclass
class If(No):
    cond: No
    then_body: List[No]
    else_body: List[No]


class GeradorTAC:
    """Gera código de três endereços (TAC) a partir da AST."""
    def __init__(self):
        self.codigo: List[str] = []
        self.temp_count = 0
        self.label_count = 0

    def novo_temp(self) -> str:
        self.temp_count += 1
        return f"t{self.temp_count}"

    def nova_label(self) -> str:
        self.label_count += 1
        return f"L{self.label_count}"

    def gerar(self, no: No) -> str:
        if isinstance(no, Num):
            return str(no.valor)
        if isinstance(no, Var):
            return no.nome
        if isinstance(no, BinOp):
            esq = self.gerar(no.esq)
            dir_ = self.gerar(no.dir)
            t = self.novo_temp()
            self.codigo.append(f"{t} = {esq} {no.op} {dir_}")
            return t
        if isinstance(no, Assign):
            val = self.gerar(no.expr)
            self.codigo.append(f"{no.nome} = {val}")
            return no.nome
        if isinstance(no, If):
            cond = self.gerar(no.cond)
            l_else = self.nova_label()
            l_fim = self.nova_label()
            self.codigo.append(f"if {cond} == 0 goto {l_else}")
            for s in no.then_body:
                self.gerar(s)
            self.codigo.append(f"goto {l_fim}")
            self.codigo.append(f"{l_else}:")
            for s in no.else_body:
                self.gerar(s)
            self.codigo.append(f"{l_fim}:")
            return ""
        raise ValueError(f"Nó não suportado: {type(no)}")


class GeradorC:
    """Gera código C a partir da AST."""
    def __init__(self):
        self.codigo: List[str] = []
        self.indent = 0

    def _ind(self) -> str:
        return "    " * self.indent

    def gerar(self, no: No) -> str:
        if isinstance(no, Num):
            return str(no.valor)
        if isinstance(no, Var):
            return no.nome
        if isinstance(no, BinOp):
            return f"({self.gerar(no.esq)} {no.op} {self.gerar(no.dir)})"
        if isinstance(no, Assign):
            return f"{self._ind()}{no.nome} = {self.gerar(no.expr)};"
        if isinstance(no, If):
            linhas = [f"{self._ind()}if ({self.gerar(no.cond)}) {{"]
            self.indent += 1
            for s in no.then_body:
                linhas.append(self.gerar(s))
            self.indent -= 1
            linhas.append(f"{self._ind()}}} else {{")
            self.indent += 1
            for s in no.else_body:
                linhas.append(self.gerar(s))
            self.indent -= 1
            linhas.append(f"{self._ind()}}}")
            return "\n".join(linhas)
        raise ValueError(f"Nó não suportado: {type(no)}")


if __name__ == "__main__":
    # AST para: if (x > 0) y = x * 2; else y = 0;
    ast = If(
        cond=BinOp(">", Var("x"), Num(0)),
        then_body=[Assign("y", BinOp("*", Var("x"), Num(2)))],
        else_body=[Assign("y", Num(0))],
    )

    print("=== Código de Três Endereços (TAC) ===")
    gen_tac = GeradorTAC()
    gen_tac.gerar(ast)
    for linha in gen_tac.codigo:
        print(f"  {linha}")

    print("\n=== Código C Gerado ===")
    gen_c = GeradorC()
    print(gen_c.gerar(ast))
