#!/usr/bin/env python3
"""TCC1701-ScannerPLY.py | Aula 17
Scanner em PLY (Python) — espelho do JFlex/ANTLR da Aula 17.
Reconhece os mesmos tokens: KEYWORD, NUM, ID, OP, PUNCT.
Uso: python3 TCC1701-ScannerPLY.py entrada.txt   (sem argumento: usa o fonte de exemplo)
Instalação: pip install ply
"""
import sys

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
t_ignore_COMMENT = r"\#[^\r\n]*"  # comentário
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


def coluna(t):
    """PLY só guarda lexpos (posição no texto todo); a coluna é contada
    a partir do último '\\n' antes do token (a partir de 1)."""
    inicio_linha = lexer.lexdata.rfind("\n", 0, t.lexpos) + 1
    return t.lexpos - inicio_linha + 1


def t_error(t):
    print(f"Caractere inválido: {t.value[0]!r} linha {t.lineno} col {coluna(t)}")
    t.lexer.skip(1)


# Constrói o lexer
lexer = lex.lex()

FONTE_EXEMPLO = """int x = 42
if x == 42   # comentário
    print(x)
else
    x = x + 1.5
"""

if __name__ == "__main__":
    if len(sys.argv) > 1:
        with open(sys.argv[1], encoding="utf-8") as f:
            fonte = f.read()
    else:
        fonte = FONTE_EXEMPLO
        print(f"=== Fonte ===\n{fonte}")
    print("=== Tokens (PLY) ===")
    lexer.input(fonte)
    for tok in lexer:
        print(f"  {tok.type}({tok.value!r})@{tok.lineno}:{coluna(tok)}")
