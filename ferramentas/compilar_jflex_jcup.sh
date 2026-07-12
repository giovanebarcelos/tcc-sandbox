#!/usr/bin/env bash
# Compila um projeto JFlex + JCup e executa a classe Main.
# Uso: ./compilar_jflex_jcup.sh <arquivo.flex> <arquivo.cup> [Main.java] [entrada.txt]
# Ex.:  ./compilar_jflex_jcup.sh calc.flex calc.cup Main.java teste.txt
set -e
DIR="$(cd "$(dirname "$0")" && pwd)"
JFLEX="$DIR/jflex-full-1.9.1.jar"
CUP="$DIR/java-cup-11b.jar"
CUP_RT="$DIR/java-cup-11b-runtime.jar"

FLEX_FILE="$1"; CUP_FILE="$2"; MAIN_FILE="${3:-Main.java}"; INPUT="${4:-}"
[ -z "$FLEX_FILE" ] || [ -z "$CUP_FILE" ] && { echo "Uso: $0 <arquivo.flex> <arquivo.cup> [Main.java] [entrada.txt]"; exit 1; }

echo "[1/4] Gerando scanner (JFlex)..."
java -jar "$JFLEX" -q "$FLEX_FILE"

echo "[2/4] Gerando parser (JCup)..."
java -jar "$CUP" -interface -parser parser -symbols sym "$CUP_FILE"

echo "[3/4] Compilando (javac)..."
javac -cp "$CUP_RT:." *.java

if [ -n "$INPUT" ]; then
  echo "[4/4] Executando ${MAIN_FILE%.java} < $INPUT ..."
  java -cp "$CUP_RT:." "${MAIN_FILE%.java}" "$INPUT"
else
  echo "[4/4] Pronto. Execute: java -cp \"$CUP_RT:.\" ${MAIN_FILE%.java} <entrada>"
fi
