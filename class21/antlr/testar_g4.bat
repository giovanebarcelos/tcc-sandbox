@echo off
rem testar_g4.bat | Aula 21
rem Versão Windows do testar_g4.sh: gera as três versões da calculadora ANTLR 4
rem com o ANTLR 4.13.1, roda cada uma sobre o mesmo arquivo de entrada e confere
rem se os resultados são idênticos.
rem   [py]       Python3: TCC2102Calc.g4        (gramática pura + Visitor: TCC2102-CalcANTLR.py)
rem   [pyacoes]  Python3: TCC2103CalcAcoesPy.g4 (ações embutidas, sem visitor/listener, pygrun)
rem   [java]     Java:    TCC2103CalcAcoes.g4   (ações embutidas, sem visitor/listener, grun)
rem
rem Uso:
rem   testar_g4.bat                  usa ..\entrada.txt
rem   testar_g4.bat minha_entrada.txt
rem
rem O .jar do ANTLR 4.13.1 é procurado em %ANTLR_JAR%, %TCC_TOOLS%, C:\ferramentas e
rem ..\..\ferramentas (a pasta ferramentas\ deste repositório), nessa ordem.
rem A bateria completa de testes (válidos e inválidos) está no testar_g4.sh (Linux/macOS/Git Bash).

setlocal EnableExtensions
chcp 65001 >nul
cd /d "%~dp0"

set "ANTLR_VERSION=4.13.1"
set "JARNAME=antlr-%ANTLR_VERSION%-complete.jar"

set "ENTRADA=%~1"
if "%ENTRADA%"=="" set "ENTRADA=..\entrada.txt"
if not exist "%ENTRADA%" (
    echo Arquivo de entrada nao encontrado: %ENTRADA%
    exit /b 1
)
for %%F in ("%ENTRADA%") do set "ENTRADA=%%~fF"

rem --- 0. Ferramenta ANTLR 4.13.1 (gerador + runtime Java) -----------------------
set "JAR="
if defined ANTLR_JAR if exist "%ANTLR_JAR%" set "JAR=%ANTLR_JAR%"
if not defined JAR if defined TCC_TOOLS if exist "%TCC_TOOLS%\%JARNAME%" set "JAR=%TCC_TOOLS%\%JARNAME%"
if not defined JAR if exist "C:\ferramentas\%JARNAME%" set "JAR=C:\ferramentas\%JARNAME%"
if not defined JAR if exist "..\..\ferramentas\%JARNAME%" set "JAR=%~dp0..\..\ferramentas\%JARNAME%"
if not defined JAR (
    echo %JARNAME% nao encontrado ^(defina ANTLR_JAR ou TCC_TOOLS^).
    exit /b 1
)

rem --- 1. Ambiente Python com o runtime do ANTLR (mesma versão da ferramenta) ---
if not exist ".venv-g4\Scripts\python.exe" (
    echo ^>^> Criando ambiente virtual .venv-g4
    python -m venv .venv-g4 2>nul || py -m venv .venv-g4 || exit /b 1
)
set "PY=%~dp0.venv-g4\Scripts\python.exe"
echo ^>^> Conferindo antlr4-python3-runtime==%ANTLR_VERSION%
"%PY%" -m pip install -q --disable-pip-version-check "antlr4-python3-runtime==%ANTLR_VERSION%" || exit /b 1

rem --- 2. Gerar o parser Python com Visitor ----------------------------------------
echo ^>^> Gerando parser Python a partir de TCC2102Calc.g4 (ANTLR %ANTLR_VERSION%)
if exist build-g4 rmdir /s /q build-g4
java -jar "%JAR%" -Dlanguage=Python3 -visitor -no-listener -o build-g4 -Xexact-output-dir TCC2102Calc.g4 || exit /b 1
copy /y TCC2102-CalcANTLR.py build-g4\ >nul

rem --- 2a. Gerar o parser Python com ações embutidas (sem visitor) ----------------
echo ^>^> Gerando parser Python a partir de TCC2103CalcAcoesPy.g4 (sem visitor/listener)
if exist build-g4-pyacoes rmdir /s /q build-g4-pyacoes
java -jar "%JAR%" -Dlanguage=Python3 -no-visitor -no-listener -o build-g4-pyacoes -Xexact-output-dir TCC2103CalcAcoesPy.g4 || exit /b 1

rem --- 2b. Gerar e compilar o parser Java (ações embutidas, sem visitor) ----------
echo ^>^> Gerando parser Java a partir de TCC2103CalcAcoes.g4 (sem visitor/listener)
if exist build-g4-java rmdir /s /q build-g4-java
java -jar "%JAR%" -no-visitor -no-listener -o build-g4-java -Xexact-output-dir TCC2103CalcAcoes.g4 || exit /b 1
javac -cp "%JAR%" -d build-g4-java build-g4-java\*.java || exit /b 1

rem --- 3. Executar as três versões sobre a mesma entrada ---------------------------
echo.
echo ^>^> Entrada: %ENTRADA%
type "%ENTRADA%"
echo.

echo [py] TCC2102-CalcANTLR.py (Visitor)
pushd build-g4
"%PY%" TCC2102-CalcANTLR.py "%ENTRADA%" > saida.txt
popd
findstr /b "Resultado:" build-g4\saida.txt > build-g4\resultados.txt
type build-g4\resultados.txt

echo [pyacoes] pygrun TCC2103CalcAcoesPy calc
pushd build-g4-pyacoes
"%PY%" -m antlr4._pygrun TCC2103CalcAcoesPy calc "%ENTRADA%" > saida.txt
popd
findstr /b "Resultado:" build-g4-pyacoes\saida.txt > build-g4-pyacoes\resultados.txt
type build-g4-pyacoes\resultados.txt

echo [java] grun TCC2103CalcAcoes calc
java -cp "%JAR%;build-g4-java" org.antlr.v4.gui.TestRig TCC2103CalcAcoes calc "%ENTRADA%" > build-g4-java\saida.txt
findstr /b "Resultado:" build-g4-java\saida.txt > build-g4-java\resultados.txt
type build-g4-java\resultados.txt

rem --- 4. Paridade entre as três versões -------------------------------------------
echo.
fc build-g4\resultados.txt build-g4-pyacoes\resultados.txt >nul || goto :diferente
fc build-g4\resultados.txt build-g4-java\resultados.txt >nul || goto :diferente
echo As tres versoes deram os mesmos resultados.
exit /b 0

:diferente
echo As versoes deram resultados DIFERENTES - compare as saidas acima.
exit /b 1
