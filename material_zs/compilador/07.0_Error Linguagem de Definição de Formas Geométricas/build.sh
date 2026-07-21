#!/bin/bash

echo "=== Compilador de Formas Geométricas ==="
echo "Gerando analisador léxico com JFlex..."

# Gerar o lexer
jflex Lexer.flex

if [ $? -ne 0 ]; then
    echo "Erro ao gerar o lexer!"
    exit 1
fi

echo "Gerando analisador sintático com JCup..."

# Gerar o parser
java -jar java-cup-11b.jar -parser parser Parser.cup

if [ $? -ne 0 ]; then
    echo "Erro ao gerar o parser!"
    exit 1
fi

echo "Compilando arquivos Java..."

# Compilar todos os arquivos Java
javac -cp .:java-cup-11b-runtime.jar *.java

if [ $? -ne 0 ]; then
    echo "Erro ao compilar os arquivos Java!"
    exit 1
fi

echo ""
echo "=== Compilação concluída com sucesso! ==="
echo ""
echo "Para executar: java -cp .:java-cup-11b-runtime.jar Main"
echo ""
