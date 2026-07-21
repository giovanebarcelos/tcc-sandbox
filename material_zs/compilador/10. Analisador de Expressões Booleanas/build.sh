#!/bin/bash

echo "=================================================="
echo "    COMPILANDO O COMPILADOR"
echo "=================================================="

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Limpar arquivos antigos
echo -e "${YELLOW}[1/4] Limpando arquivos antigos...${NC}"
rm -f Lexer.java sym.java parser.java *.class

# Gerar o analisador léxico
echo -e "${YELLOW}[2/4] Gerando analisador léxico (JFlex)...${NC}"
jflex Lexer.flex

if [ $? -ne 0 ]; then
    echo -e "${RED}ERRO: Falha ao gerar o analisador léxico!${NC}"
    exit 1
fi

# Gerar o analisador sintático
echo -e "${YELLOW}[3/4] Gerando analisador sintático (JCup)...${NC}"
java java_cup.Main Parser.cup

if [ $? -ne 0 ]; then
    echo -e "${RED}ERRO: Falha ao gerar o analisador sintático!${NC}"
    exit 1
fi

# Compilar os arquivos Java
echo -e "${YELLOW}[4/4] Compilando arquivos Java...${NC}"
javac Lexer.java sym.java parser.java Compiler.java

if [ $? -ne 0 ]; then
    echo -e "${RED}ERRO: Falha ao compilar os arquivos Java!${NC}"
    exit 1
fi

echo ""
echo -e "${GREEN}=================================================="
echo "    COMPILAÇÃO CONCLUÍDA COM SUCESSO!"
echo "==================================================${NC}"
echo ""
echo "Para executar o compilador, use:"
echo "  java Compiler exemplo1.txt"
echo ""
