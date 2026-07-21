@echo off
setlocal

echo ==================================================
echo     COMPILANDO O COMPILADOR
echo ==================================================

echo [1/4] Limpando arquivos antigos...
del /q Lexer.java sym.java parser.java *.class 2>nul

echo [2/4] Gerando analisador lexico (JFlex)...
jflex Lexer.flex
if errorlevel 1 goto :error_lexer

echo [3/4] Gerando analisador sintatico (JCup)...
java java_cup.Main Parser.cup
if errorlevel 1 goto :error_parser

echo [4/4] Compilando arquivos Java...
javac Lexer.java sym.java parser.java Compiler.java
if errorlevel 1 goto :error_compile

echo.
echo ==================================================
echo     COMPILACAO CONCLUIDA COM SUCESSO!
echo ==================================================
echo Para executar o compilador, use:
echo   java Compiler exemplo1.txt

endlocal
exit /b 0

:error_lexer
echo [ERRO] Falha ao gerar o analisador lexico.
exit /b 1

:error_parser
echo [ERRO] Falha ao gerar o analisador sintatico.
exit /b 1

:error_compile
echo [ERRO] Falha ao compilar os arquivos Java.
exit /b 1
