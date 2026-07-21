#!/bin/bash
set -e

cd "$(dirname "$0")"

echo "=== Processador de Templates Simples - Build ==="
echo "1. Gerando analisador léxico (JFlex)..."
jflex TemplateLexer.flex

echo "2. Gerando analisador sintático (JCup)..."
java java_cup.Main -parser TemplateParser TemplateParser.cup

echo "3. Compilando classes Java..."
javac *.java

echo ""
echo "✅ Build concluído com sucesso!"
echo "Para executar:"
echo "  java TestRunner"
