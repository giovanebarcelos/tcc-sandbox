#!/bin/bash

echo "=== Executando Testes do Compilador ==="
echo ""

# Verificar se os arquivos de teste existem
if [ ! -f "test1.dfa" ] || [ ! -f "test2.dfa" ] || [ ! -f "test3.dfa" ]; then
    echo "❌ Arquivos de teste não encontrados."
    echo "Certifique-se de que test1.dfa, test2.dfa e test3.dfa existem."
    exit 1
fi

# Executar os testes
echo "Executando testes..."
echo ""

java TestRunner

echo ""
echo "✅ Testes concluídos!"
