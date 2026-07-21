@echo off
setlocal
pushd "%~dp0"

echo === Compilador de Formas Geometricas ===
echo Gerando analisador lexico com JFlex...
jflex Lexer.flex
if errorlevel 1 (
    echo [ERRO] Falha ao gerar o lexer.
    popd
    exit /b 1
)

echo Gerando analisador sintatico com JCup...
java -jar java-cup-11b.jar -parser parser Parser.cup
if errorlevel 1 (
    echo [ERRO] Falha ao gerar o parser.
    popd
    exit /b 1
)

echo Compilando arquivos Java...
javac -cp ".;java-cup-11b-runtime.jar" *.java
if errorlevel 1 (
    echo [ERRO] Falha ao compilar os arquivos Java.
    popd
    exit /b 1
)

echo.
echo === Compilacao concluida com sucesso! ===
echo Para executar: java -cp ".;java-cup-11b-runtime.jar" Main

popd
endlocal
exit /b 0
