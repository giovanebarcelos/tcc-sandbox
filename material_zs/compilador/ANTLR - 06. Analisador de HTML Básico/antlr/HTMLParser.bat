@echo off
setlocal

echo === Gerando lexer ===
antlr4 HTMLLexer.g4
if errorlevel 1 goto :error

echo === Gerando parser ===
antlr4 HTMLParser.g4
if errorlevel 1 goto :error

echo === Compilando classes Java ===
javac *.java
if errorlevel 1 goto :error

echo === Executando Main ===
java Main html.txt
if errorlevel 1 goto :error

echo [OK] Execucao concluida.
endlocal
exit /b 0

:error
echo [ERRO] Falha durante a execucao do script.
endlocal
exit /b 1
