#!/bin/bash
set -e

cd "$(dirname "$0")"

echo "=== Executando Testes do Processador de Templates ==="

if [ ! -f "templates/valid_boas_vindas.tpl" ] || [ ! -f "templates/valid_pedido.tpl" ] || [ ! -f "templates/error_unclosed_if.tpl" ]; then
    echo "❌ Arquivos de template não encontrados."
    exit 1
fi

./build.sh

echo "\nRodando TestRunner...\n"
java TestRunner

echo "\n✅ Testes concluídos!"
