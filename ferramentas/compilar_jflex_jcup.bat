@echo off
rem Compila um projeto JFlex + JCup e compila os .java gerados.
rem Uso: compilar_jflex_jcup.bat arquivo.flex arquivo.cup
if "%~2"=="" (
  echo Uso: %~nx0 arquivo.flex arquivo.cup
  exit /b 1
)
set DIR=%~dp0
echo [1/3] Gerando scanner (JFlex)...
java -jar "%DIR%jflex-full-1.9.1.jar" -q %1
if errorlevel 1 exit /b 1
echo [2/3] Gerando parser (JCup)...
java -jar "%DIR%java-cup-11b.jar" -interface -parser parser -symbols sym %2
if errorlevel 1 exit /b 1
echo [3/3] Compilando (javac)...
javac -cp "%DIR%java-cup-11b-runtime.jar;." *.java
echo Pronto. Execute: java -cp "%DIR%java-cup-11b-runtime.jar;." Main entrada.txt
