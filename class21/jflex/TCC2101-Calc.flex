// TCC2101-Calc.flex | Aula 21
// Scanner JFlex para a calculadora (JFlex + JCup).
// Build (na pasta com TCC2101-Calc.flex, TCC2101-Calc.cup e entrada.txt):
//        bash ferramentas/compilar_jflex_jcup.sh TCC2101-Calc.flex TCC2101-Calc.cup parser.java entrada.txt
//   ou, passo a passo:
//        jflex TCC2101-Calc.flex                                   -> CalcLexer.java
//        cup -interface -parser parser -symbols sym TCC2101-Calc.cup  -> parser.java, sym.java
//        javac CalcLexer.java parser.java sym.java                 (java-cup-11b-runtime.jar no CLASSPATH)
//        java parser entrada.txt

import java_cup.runtime.*;

%%

%class CalcLexer
%unicode
%line
%column
%cup
%public

%{
  // Symbol(tipo, linha, coluna[, valor]) — linha/coluna a partir de 1
  private Symbol simbolo(int tipo)              { return new Symbol(tipo, yyline + 1, yycolumn + 1); }
  private Symbol simbolo(int tipo, Object valor) { return new Symbol(tipo, yyline + 1, yycolumn + 1, valor); }
%}

LineTerminator = \r|\n|\r\n
WhiteSpace     = {LineTerminator} | [ \t\f]
Digit          = [0-9]
Number         = {Digit}+ ("." {Digit}+)?

%%

{WhiteSpace}     { /* ignora */ }
{Number}         { return simbolo(sym.NUM, Double.parseDouble(yytext())); }
"+"              { return simbolo(sym.PLUS); }
"-"              { return simbolo(sym.MINUS); }
"*"              { return simbolo(sym.TIMES); }
"/"              { return simbolo(sym.DIV); }
"("              { return simbolo(sym.LPAREN); }
")"              { return simbolo(sym.RPAREN); }
";"              { return simbolo(sym.SEMI); }

[^]              { throw new RuntimeException("Caractere inválido: '" + yytext() + "' linha " + (yyline+1) + " col " + (yycolumn+1)); }
