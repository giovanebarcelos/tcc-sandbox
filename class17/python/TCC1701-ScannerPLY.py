#!/usr/bin/env python3
"""TCC1701-ScannerPLY.py | Aula 17
Scanner em PLY (Python) — espelho do JFlex/ANTLR da Aula 17.
Reconhece os mesmos tokens: NUM, ID, KEYWORD, OP, PUNCT.
Instalação: pip install ply
"""
import ply.lex as lex

# Lista de nomes de tokens
tokens = (
    "NUM", "ID", "KEYWORD", "OP", "PUNCT",
)

# Palavras-chave (mapeadas de ID para KEYWORD)
reserved = {
    "if": "KEYWORD", "else": "KEYWORD", "while": "KEYWORD",
    "print": "KEYWORD", "int": "KEYWORD", "float": "KEYWORD",
}

# Regras simples (strings)
t_OP = r"==|[+\-*/=]"
t_PUNCT = r"[(){};]"
t_ignore_COMMENT = r"\#[^\n]*"  # comentário
t_ignore = " \t\r"

# Regras com funções (precisam vir antes de t_ID)


def t_NUM(t):
    r"\d+(\.\d+)?"
    return t


def t_ID(t):
    r"[a-zA-Z_][a-zA-Z0-9_]*"
    t.type = reserved.get(t.value, "ID")
    return t


def t_newline(t):
    r"\n+"
    t.lexer.lineno += len(t.value)


def t_error(t):
    print(f"Caractere inválido: {t.value[0]!r} linha {t.lineno}")
    t.lexer.skip(1)


# Constrói o lexer
lexer = lex.lex()

if __name__ == "__main__":
    fonte = """int x = 42
if x == 42
    print(x)
else
    x = x + 1"""
    print(f"=== Fonte ===\n{fonte}\n")
    print("=== Tokens (PLY) ===")
    lexer.input(fonte)
    for tok in lexer:
        print(f"  {tok.type}({tok.value!r})@{tok.lineno}:{tok.lexpos}")
