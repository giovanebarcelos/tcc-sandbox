#!/bin/bash

echo "=== Compilador de Autômatos Finitos - Build Script ==="
echo ""

# Gerar o analisador léxico com JFlex
echo "1. Gerando analisador léxico com JFlex..."
jflex Lexer.flex

if [ $? -ne 0 ]; then
    echo "❌ Erro ao gerar o lexer"
    exit 1
fi

# Gerar o analisador sintático com JCup
echo "2. Gerando analisador sintático com JCup..."
java java_cup.Main Parser.cup

if [ $? -ne 0 ]; then
    echo "❌ Erro ao gerar o parser"
    exit 1
fi

# Compilar todos os arquivos Java
echo "3. Compilando arquivos Java..."
javac *.java 

if [ $? -ne 0 ]; then
    echo "❌ Erro ao compilar"
    exit 1
fi

echo ""
echo "✅ Build concluído com sucesso!"
echo ""
echo "Para executar:"
echo "  java Main"
echo ""
