@echo off
setlocal
pushd "%~dp0"

echo === Executando Testes do Processador de Templates ===

if not exist "templates\valid_boas_vindas.tpl" (
    echo [ERRO] templates\valid_boas_vindas.tpl nao encontrado.
    goto :error
)
if not exist "templates\valid_pedido.tpl" (
    echo [ERRO] templates\valid_pedido.tpl nao encontrado.
    goto :error
)
if not exist "templates\error_unclosed_if.tpl" (
    echo [ERRO] templates\error_unclosed_if.tpl nao encontrado.
    goto :error
)

call build.bat
if errorlevel 1 goto :error

echo Rodando TestRunner...
java TestRunner
if errorlevel 1 goto :error

echo [OK] Testes concluidos!
popd
endlocal
exit /b 0

:error
echo [ERRO] Falha durante a execucao dos testes.
popd
endlocal
exit /b 1
