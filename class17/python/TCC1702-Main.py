#!/usr/bin/env python3
"""TCC1702-Main.py | Aula 17
Driver Python para o lexer ANTLR gerado a partir de TCC1702Scanner.g4.
Imprime os tokens no mesmo formato do JFlex e do PLY: TIPO('lexema')@linha:coluna.
Build: antlr4 -Dlanguage=Python3 TCC1702Scanner.g4   -> gera TCC1702Scanner.py
       python3 TCC1702-Main.py entrada.txt
Instalação: pip install antlr4-python3-runtime==4.13.1
"""
import sys

from antlr4 import FileStream, Token

from TCC1702Scanner import TCC1702Scanner

arquivo = sys.argv[1] if len(sys.argv) > 1 else "entrada.txt"
lexer = TCC1702Scanner(FileStream(arquivo, encoding="utf-8"))

print("=== Tokens (ANTLR) ===")
tok = lexer.nextToken()
while tok.type != Token.EOF:
    nome = lexer.symbolicNames[tok.type]
    # ANTLR: linha a partir de 1, coluna a partir de 0
    print(f"  {nome}({tok.text!r})@{tok.line}:{tok.column + 1}")
    tok = lexer.nextToken()
