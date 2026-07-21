@echo off
setlocal

echo === Executando Testes do Compilador ===
echo.

if not exist "test1.dfa" (
    echo [ERRO] Arquivo test1.dfa nao encontrado.
    exit /b 1
)
if not exist "test2.dfa" (
    echo [ERRO] Arquivo test2.dfa nao encontrado.
    exit /b 1
)
if not exist "test3.dfa" (
    echo [ERRO] Arquivo test3.dfa nao encontrado.
    exit /b 1
)

echo Executando testes...
java TestRunner
if errorlevel 1 (
    echo [ERRO] Falha ao executar os testes.
    exit /b 1
)

echo.
echo [OK] Testes concluidos!

endlocal
exit /b 0
