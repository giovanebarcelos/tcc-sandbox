#!/usr/bin/env python3
"""TCC1501-FasesCompilador.py | Aula 15
Mini-pipeline didático de compilador: fonte → tokens → AST → código intermediário.
Demonstra as 6 fases de forma executável e simples.
"""
from dataclasses import dataclass, field
from typing import List, Union


# ---------- Fase 1: Análise Léxica (Scanner) ----------
@dataclass
class Token:
    tipo: str
    valor: str

    def __repr__(self):
        return f"{self.tipo}({self.valor!r})"


def scanner(fonte: str) -> List[Token]:
    """Scanner manual: reconhece NUM, ID, OP (+, -, *, /, =) e PAREN."""
    tokens: List[Token] = []
    i = 0
    while i < len(fonte):
        c = fonte[i]
        if c.isspace():
            i += 1
            continue
        if c.isdigit():
            j = i
            while j < len(fonte) and fonte[j].isdigit():
                j += 1
            tokens.append(Token("NUM", fonte[i:j]))
            i = j
        elif c.isalpha():
            j = i
            while j < len(fonte) and fonte[j].isalnum():
                j += 1
            tokens.append(Token("ID", fonte[i:j]))
            i = j
        elif c in "+-*/=":
            tokens.append(Token("OP", c))
            i += 1
        elif c in "()":
            tokens.append(Token("PAREN", c))
            i += 1
        else:
            raise SyntaxError(f"Caractere inválido: {c!r}")
    return tokens


# ---------- Fase 2: Análise Sintática (Parser) ----------
# Gramática: expr → term (('+'|'-') term)*
#            term → factor (('*'|'/') factor)*
#            factor → NUM | ID | '(' expr ')'

@dataclass
class No:
    op: str
    esq: Union["No", str]
    dir: Union["No", str]


class Parser:
    def __init__(self, tokens: List[Token]):
        self.tokens = tokens
        self.pos = 0

    def peek(self):
        return self.tokens[self.pos] if self.pos < len(self.tokens) else None

    def consume(self):
        t = self.tokens[self.pos]
        self.pos += 1
        return t

    def parse(self):
        return self.expr()

    def expr(self):
        no = self.term()
        while self.peek() and self.peek().tipo == "OP" and self.peek().valor in "+-":
            op = self.consume().valor
            dir_ = self.term()
            no = No(op, no, dir_)
        return no

    def term(self):
        no = self.factor()
        while self.peek() and self.peek().tipo == "OP" and self.peek().valor in "*/":
            op = self.consume().valor
            dir_ = self.factor()
            no = No(op, no, dir_)
        return no

    def factor(self):
        t = self.peek()
        if t and t.tipo in ("NUM", "ID"):
            self.consume()
            return t.valor
        if t and t.tipo == "PAREN" and t.valor == "(":
            self.consume()
            no = self.expr()
            if not (self.peek() and self.peek().valor == ")"):
                raise SyntaxError("Esperado ')'")
            self.consume()
            return no
        raise SyntaxError(f"Token inesperado: {t}")


# ---------- Fase 3: Análise Semântica (Tabela de Símbolos) ----------
def coletar_ids(no) -> set:
    ids = set()
    if isinstance(no, str):
        if no and not no.isdigit():
            ids.add(no)
    else:
        ids |= coletar_ids(no.esq)
        ids |= coletar_ids(no.dir)
    return ids


# ---------- Fase 4: Geração de Código Intermediário (TAC) ----------
tac: List[str] = []
temp_counter = [0]


def novo_temp():
    temp_counter[0] += 1
    return f"t{temp_counter[0]}"


def gerar_tac(no) -> str:
    if isinstance(no, str):
        return no
    esq = gerar_tac(no.esq)
    dir_ = gerar_tac(no.dir)
    t = novo_temp()
    tac.append(f"{t} = {esq} {no.op} {dir_}")
    return t


# ---------- Fase 5: Otimização (constant folding) ----------
def otimizar_tac():
    """Constant folding simples: se t1 = 3 + 4 → substitui por 7."""
    otimizado = []
    valores = {}
    for linha in tac:
        # t1 = 3 + 4
        parts = linha.split(" = ")
        if len(parts) == 2:
            dst, expr = parts
            tokens = expr.split()
            if len(tokens) == 3 and tokens[0].isdigit() and tokens[2].isdigit():
                a, op, b = int(tokens[0]), tokens[1], int(tokens[2])
                if op == "+": v = a + b
                elif op == "-": v = a - b
                elif op == "*": v = a * b
                elif op == "/": v = a // b
                else: v = None
                if v is not None:
                    valores[dst] = v
                    otimizado.append(f"{dst} = {v}  (constant folding)")
                    continue
        otimizado.append(linha)
    return otimizado


# ---------- Fase 6: Geração de Código Final ----------
def gerar_python(no) -> str:
    if isinstance(no, str):
        return no
    return f"({gerar_python(no.esq)} {no.op} {gerar_python(no.dir)})"


# ---------- Pipeline completo ----------
if __name__ == "__main__":
    fonte = "3 + 4 * x"
    print(f"=== Fonte: {fonte!r} ===\n")

    print("--- Fase 1: Análise Léxica ---")
    tokens = scanner(fonte)
    print(f"Tokens: {tokens}\n")

    print("--- Fase 2: Análise Sintática (AST) ---")
    ast = Parser(tokens).parse()
    print(f"AST: {ast}\n")

    print("--- Fase 3: Análise Semântica (Tabela de Símbolos) ---")
    ids = coletar_ids(ast)
    print(f"Identificadores: {ids}\n")

    print("--- Fase 4: Geração de Código Intermediário (TAC) ---")
    gerar_tac(ast)
    for linha in tac:
        print(f"  {linha}")
    print()

    print("--- Fase 5: Otimização (constant folding) ---")
    ot = otimizar_tac()
    for linha in ot:
        print(f"  {linha}")
    print()

    print("--- Fase 6: Geração de Código Final (Python) ---")
    print(f"  resultado = {gerar_python(ast)}")
