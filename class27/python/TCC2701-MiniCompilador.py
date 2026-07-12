#!/usr/bin/env python3
"""TCC2701-MiniCompilador.py | Aula 27
Esqueleto do Projeto Integrador A3 — mini-interpretador.
Linguagem: variáveis, expressões, if/else, while, print.
Stack: Python puro (sem dependências) — pode ser portado para ANTLR/PLY.
"""
import re
from typing import Dict, List, Tuple


# ---------- Scanner ----------
TOKEN_SPEC = [
    ("NUM",     r"\d+(\.\d+)?"),
    ("ID",      r"[a-zA-Z_]\w*"),
    ("OP",      r"==|[-+*/=<>]"),
    ("PUNCT",   r"[(){};]"),
    ("WS",      r"\s+"),
    ("COMMENT", r"#[^\n]*"),
]
KEYWORDS = {"if", "else", "while", "print"}


def scanner(fonte: str) -> List[Tuple[str, str, int, int]]:
    tokens = []
    pos = 0
    linha = 1
    col = 1
    while pos < len(fonte):
        for tipo, pattern in TOKEN_SPEC:
            m = re.match(pattern, fonte[pos:])
            if m:
                valor = m.group(0)
                if tipo == "WS":
                    col += len(valor)
                    pos += len(valor)
                    break
                if tipo == "COMMENT":
                    pos += len(valor)
                    break
                if tipo == "ID" and valor in KEYWORDS:
                    tipo = "KEYWORD"
                tokens.append((tipo, valor, linha, col))
                col += len(valor)
                pos += len(valor)
                break
        else:
            if fonte[pos] == "\n":
                linha += 1
                col = 1
                pos += 1
            else:
                raise SyntaxError(f"Caractere inválido {fonte[pos]!r} linha {linha} col {col}")
    tokens.append(("EOF", "", linha, col))
    return tokens


# ---------- Parser (descendente recursivo) ----------
class Parser:
    def __init__(self, tokens):
        self.tokens = tokens
        self.pos = 0

    def peek(self):
        return self.tokens[self.pos]

    def consume(self, tipo=None, valor=None):
        t = self.tokens[self.pos]
        if tipo and t[0] != tipo:
            raise SyntaxError(f"Esperado {tipo}, obtido {t[0]}({t[1]!r}) linha {t[2]}")
        if valor and t[1] != valor:
            raise SyntaxError(f"Esperado {valor!r}, obtido {t[1]!r} linha {t[2]}")
        self.pos += 1
        return t

    def parse(self):
        stmts = []
        while self.peek()[0] != "EOF":
            stmts.append(self.stmt())
        return stmts

    def stmt(self):
        t = self.peek()
        if t[0] == "KEYWORD" and t[1] == "if":
            return self.if_stmt()
        if t[0] == "KEYWORD" and t[1] == "while":
            return self.while_stmt()
        if t[0] == "KEYWORD" and t[1] == "print":
            return self.print_stmt()
        if t[0] == "ID":
            return self.assign()
        raise SyntaxError(f"Token inesperado: {t}")

    def assign(self):
        nome = self.consume("ID")[1]
        self.consume("OP", "=")
        expr = self.expr()
        self.consume("PUNCT", ";")
        return ("assign", nome, expr)

    def if_stmt(self):
        self.consume("KEYWORD", "if")
        self.consume("PUNCT", "(")
        cond = self.expr()
        self.consume("PUNCT", ")")
        then_block = self.block()
        else_block = []
        if self.peek()[0] == "KEYWORD" and self.peek()[1] == "else":
            self.consume("KEYWORD", "else")
            else_block = self.block()
        return ("if", cond, then_block, else_block)

    def while_stmt(self):
        self.consume("KEYWORD", "while")
        self.consume("PUNCT", "(")
        cond = self.expr()
        self.consume("PUNCT", ")")
        body = self.block()
        return ("while", cond, body)

    def print_stmt(self):
        self.consume("KEYWORD", "print")
        self.consume("PUNCT", "(")
        expr = self.expr()
        self.consume("PUNCT", ")")
        self.consume("PUNCT", ";")
        return ("print", expr)

    def block(self):
        self.consume("PUNCT", "{")
        stmts = []
        while self.peek()[1] != "}":
            stmts.append(self.stmt())
        self.consume("PUNCT", "}")
        return stmts

    def expr(self):
        return self.expr_binop(self.term, ["+", "-"])

    def term(self):
        return self.expr_binop(self.factor, ["*", "/"])

    def factor(self):
        t = self.peek()
        if t[0] == "NUM":
            self.consume()
            return ("num", float(t[1]))
        if t[0] == "ID":
            self.consume()
            return ("var", t[1])
        if t[1] == "(":
            self.consume("PUNCT", "(")
            e = self.expr()
            self.consume("PUNCT", ")")
            return e
        raise SyntaxError(f"Token inesperado em factor: {t}")

    def expr_binop(self, sub, ops):
        esq = sub()
        while self.peek()[0] == "OP" and self.peek()[1] in ops:
            op = self.consume()[1]
            dir_ = sub()
            esq = ("binop", op, esq, dir_)
        return esq


# ---------- Interpretador ----------
class Interpretador:
    def __init__(self):
        self.vars: Dict[str, float] = {}

    def executar(self, stmts):
        for s in stmts:
            self.exec_stmt(s)

    def exec_stmt(self, s):
        if s[0] == "assign":
            val = self.eval(s[2])
            self.vars[s[1]] = val
        elif s[0] == "print":
            print(self.eval(s[1]))
        elif s[0] == "if":
            if self.eval(s[1]) != 0:
                self.executar(s[2])
            else:
                self.executar(s[3])
        elif s[0] == "while":
            while self.eval(s[1]) != 0:
                self.executar(s[2])

    def eval(self, e):
        if e[0] == "num":
            return e[1]
        if e[0] == "var":
            if e[1] not in self.vars:
                raise NameError(f"Variável '{e[1]}' não definida")
            return self.vars[e[1]]
        if e[0] == "binop":
            esq = self.eval(e[2])
            dir_ = self.eval(e[3])
            ops = {"+": esq + dir_, "-": esq - dir_, "*": esq * dir_, "/": esq / dir_}
            return ops[e[1]]


if __name__ == "__main__":
    fonte = """
x = 3 + 4 * 2;
print(x);
if (x > 10) {
    print(1);
} else {
    print(0);
}
"""
    print("=== Fonte ===")
    print(fonte)
    tokens = scanner(fonte)
    ast = Parser(tokens).parse()
    print("=== Execução ===")
    Interpretador().executar(ast)
