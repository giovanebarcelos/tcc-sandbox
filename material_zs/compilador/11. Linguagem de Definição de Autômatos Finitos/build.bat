@echo off
setlocal

echo === Compilador de Automatos Finitos - Build Script ===
echo.

echo 1. Gerando analisador lexico com JFlex...
jflex Lexer.flex
if errorlevel 1 (
    echo [ERRO] Falha ao gerar o lexer.
    exit /b 1
)

echo 2. Gerando analisador sintatico com JCup...
java java_cup.Main Parser.cup
if errorlevel 1 (
    echo [ERRO] Falha ao gerar o parser.
    exit /b 1
)

echo 3. Compilando arquivos Java...
javac *.java
if errorlevel 1 (
    echo [ERRO] Falha ao compilar os arquivos Java.
    exit /b 1
)

echo.
echo [OK] Build concluido com sucesso!
echo Para executar:
echo   java Main

endlocal
exit /b 0
