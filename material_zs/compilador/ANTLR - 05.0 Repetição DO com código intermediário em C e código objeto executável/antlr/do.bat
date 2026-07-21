@echo off
setlocal

echo === Gerando artefatos ANTLR (Do) ===
antlr4 Do.g4
if errorlevel 1 goto :error

echo === Compilando classes Java ===
javac *.java
if errorlevel 1 goto :error

echo === Executando parser ===
java DoParser do.txt
if errorlevel 1 goto :error

echo [OK] Execucao concluida.
endlocal
exit /b 0

:error
echo [ERRO] Falha durante a execucao do script.
endlocal
exit /b 1
