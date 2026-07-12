@echo off
rem Gera lexer/parser com ANTLR 4.13.1.
rem Uso: antlr4.bat Gramatica.g4
rem      antlr4.bat -Dlanguage=Python3 Gramatica.g4
java -jar "%~dp0antlr-4.13.1-complete.jar" %*
