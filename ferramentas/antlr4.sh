#!/usr/bin/env bash
# Gera lexer/parser com ANTLR 4.13.1.
# Uso: ./antlr4.sh Gramatica.g4              -> alvo Java (padrão)
#      ./antlr4.sh -Dlanguage=Python3 Gramatica.g4 -> alvo Python
# Teste interativo (grun): ./antlr4.sh grun Expr expr -tree
DIR="$(cd "$(dirname "$0")" && pwd)"
JAR="$DIR/antlr-4.13.1-complete.jar"
if [ "$1" = "grun" ]; then
  shift
  java -cp "$JAR:." org.antlr.v4.gui.TestRig "$@"
else
  java -jar "$JAR" "$@"
fi
