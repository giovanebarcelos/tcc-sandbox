#!/bin/bash

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo ""
echo -e "${BLUE}=================================================="
echo "           EXECUTANDO TESTES"
echo "==================================================${NC}"
echo ""

# Teste 1 - Programa correto
echo -e "${YELLOW}[TESTE 1] Executando exemplo1.txt (programa correto)...${NC}"
echo ""
java Compiler exemplo1.txt
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ TESTE 1 PASSOU${NC}"
else
    echo -e "${RED}✗ TESTE 1 FALHOU${NC}"
fi
echo ""
echo "----------------------------------------------"
echo ""

# Teste 2 - Programa correto
echo -e "${YELLOW}[TESTE 2] Executando exemplo2.txt (programa correto)...${NC}"
echo ""
java Compiler exemplo2.txt
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ TESTE 2 PASSOU${NC}"
else
    echo -e "${RED}✗ TESTE 2 FALHOU${NC}"
fi
echo ""
echo "----------------------------------------------"
echo ""

# Teste 3 - Programa com erros
echo -e "${YELLOW}[TESTE 3] Executando exemplo_erro.txt (deve detectar erros)...${NC}"
echo ""
java Compiler exemplo_erro.txt
if [ $? -ne 0 ]; then
    echo -e "${GREEN}✓ TESTE 3 PASSOU (erros detectados corretamente)${NC}"
else
    echo -e "${RED}✗ TESTE 3 FALHOU (deveria ter detectado erros)${NC}"
fi
echo ""

echo ""
echo -e "${BLUE}=================================================="
echo "           TESTES CONCLUÍDOS"
echo "==================================================${NC}"
echo ""
