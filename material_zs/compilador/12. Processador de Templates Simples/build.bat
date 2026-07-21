@echo off
setlocal
pushd "%~dp0"

echo === Processador de Templates Simples - Build ===
echo 1. Gerando analisador lexico (JFlex)...
jflex TemplateLexer.flex
if errorlevel 1 goto :error

echo 2. Gerando analisador sintatico (JCup)...
java java_cup.Main -parser TemplateParser TemplateParser.cup
if errorlevel 1 goto :error

echo 3. Compilando classes Java...
javac *.java
if errorlevel 1 goto :error

echo.
echo [OK] Build concluido com sucesso!
echo Para executar:
echo   java TestRunner

popd
endlocal
exit /b 0

:error
echo [ERRO] Falha durante o processo de build.
popd
endlocal
exit /b 1
