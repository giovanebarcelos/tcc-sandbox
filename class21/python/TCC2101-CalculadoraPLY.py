#!/usr/bin/env python3
"""TCC2101-CalculadoraPLY.py | Aula 21
Calculadora em PLY (Python) — espelho do JFlex+JCup e ANTLR.
Instalação: pip install ply
"""
import ply.lex as lex
import ply.yacc as yacc

# ---------- Lexer ----------
tokens = ("NUM", "PLUS", "MINUS", "TIMES", "DIV", "LPAREN", "RPAREN", "SEMI")

t_PLUS   = r"\+"
t_MINUS  = r"-"
t_TIMES  = r"\*"
t_DIV    = r"/"
t_LPAREN = r"\("
t_RPAREN = r"\)"
t_SEMI   = r";"
t_ignore = " \t\r\n"


def t_NUM(t):
    r"\d+(\.\d+)?"
    t.value = float(t.value)
    return t


def t_error(t):
    print(f"Caractere inválido: {t.value[0]!r}")
    t.lexer.skip(1)


lexer = lex.lex()

# ---------- Parser ----------
precedence = (("left", "PLUS", "MINUS"), ("left", "TIMES", "DIV"))


def p_calc(p):
    "calc : expr SEMI"
    print(f"Resultado: {p[1]}")


def p_expr_add(p):
    "expr : expr PLUS term"
    p[0] = p[1] + p[3]


def p_expr_sub(p):
    "expr : expr MINUS term"
    p[0] = p[1] - p[3]


def p_expr_term(p):
    "expr : term"
    p[0] = p[1]


def p_term_mul(p):
    "term : term TIMES factor"
    p[0] = p[1] * p[3]


def p_term_div(p):
    "term : term DIV factor"
    p[0] = p[1] / p[3]


def p_term_factor(p):
    "term : factor"
    p[0] = p[1]


def p_factor_parens(p):
    "factor : LPAREN expr RPAREN"
    p[0] = p[2]


def p_factor_num(p):
    "factor : NUM"
    p[0] = p[1]


def p_error(p):
    if p:
        print(f"Erro de sintaxe em: {p.value!r}")
    else:
        print("Erro de sintaxe: fim de entrada inesperado")


parser = yacc.yacc()

if __name__ == "__main__":
    testes = ["3 + 4 * 2;", "(3 + 4) * 2;", "10 / 2 - 1;"]
    for expr in testes:
        print(f">>> {expr}")
        parser.parse(expr)
