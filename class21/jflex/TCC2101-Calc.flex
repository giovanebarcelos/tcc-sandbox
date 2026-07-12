// TCC2101-Calc.flex | Aula 21
// Scanner JFlex para a calculadora (JFlex + JCup).
// Build: java -jar jflex-full-1.9.1.jar TCC2101-Calc.flex
package tcc;

import java_cup.runtime.*;

%%

%class CalcLexer
%unicode
%line
%column
%cup
%public

LineTerminator = \r|\n|\r\n
WhiteSpace     = {LineTerminator} | [ \t\f]
Digit          = [0-9]
Number         = {Digit}+ ("." {Digit}+)?

%%

{WhiteSpace}     { /* ignora */ }
{Number}         { return new Symbol(sym.NUM, yyline+1, yycolumn+1, Double.parseDouble(yytext())); }
"+"              { return new Symbol(sym.PLUS); }
"-"              { return new Symbol(sym.MINUS); }
"*"              { return new Symbol(sym.TIMES); }
"/"              { return new Symbol(sym.DIV); }
"("              { return new Symbol(sym.LPAREN); }
")"              { return new Symbol(sym.RPAREN); }
";"              { return new Symbol(sym.SEMI); }

[^]              { throw new RuntimeException("Caractere inválido: '" + yytext() + "'"); }
