@echo off
setlocal

echo ==================================================
echo            EXECUTANDO TESTES
echo ==================================================

echo [TESTE 1] Executando exemplo1.txt (programa correto)...
java Compiler exemplo1.txt
if errorlevel 1 (
    echo   -> TESTE 1 FALHOU
) else (
    echo   -> TESTE 1 PASSOU
)
echo --------------------------------------------------

echo [TESTE 2] Executando exemplo2.txt (programa correto)...
java Compiler exemplo2.txt
if errorlevel 1 (
    echo   -> TESTE 2 FALHOU
) else (
    echo   -> TESTE 2 PASSOU
)
echo --------------------------------------------------

echo [TESTE 3] Executando exemplo_erro.txt (deve detectar erros)...
java Compiler exemplo_erro.txt
if errorlevel 1 (
    echo   -> TESTE 3 PASSOU (erros detectados corretamente)
) else (
    echo   -> TESTE 3 FALHOU (deveria ter detectado erros)
)

echo ==================================================
echo            TESTES CONCLUIDOS
echo ==================================================

endlocal
exit /b 0
