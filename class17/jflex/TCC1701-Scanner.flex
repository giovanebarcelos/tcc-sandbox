// TCC1701-Scanner.flex | Aula 17
// Especificação JFlex do mesmo scanner da Aula 16.
// Gera um lexer Java que reconhece: NUM, ID, KEYWORD, OP, PUNCT.
// Build: java -jar jflex-full-1.9.1.jar TCC1701-Scanner.flex
//        javac TCC1701Scanner.java TCC1701Main.java
//        java TCC1701Main entrada.txt

package tcc;

import java_cup.runtime.*;

%%

%class TCC1701Scanner
%unicode
%line
%column
%type Symbol
%public

%{
  private Symbol token(String type) {
    return new Symbol(0, yyline+1, yycolumn+1, yytext());
  }
%}

LineTerminator = \r|\n|\r\n
WhiteSpace     = {LineTerminator} | [ \t\f]
Comment        = "#"[^\n]*
Digit          = [0-9]
Number         = {Digit}+ ("." {Digit}+)?
Identifier     = [a-zA-Z_][a-zA-Z0-9_]*
Keyword        = "if" | "else" | "while" | "print" | "int" | "float"
Operator       = "+" | "-" | "*" | "/" | "=" | "=="
Punctuation    = "(" | ")" | "{" | "}" | ";"

%%

{Comment}        { /* ignora */ }
{WhiteSpace}     { /* ignora */ }
{Keyword}        { return token("KEYWORD"); }
{Number}         { return token("NUM"); }
{Identifier}     { return token("ID"); }
{Operator}       { return token("OP"); }
{Punctuation}    { return token("PUNCT"); }

[^]              { throw new RuntimeException("Caractere inválido: '" + yytext() + "' linha " + (yyline+1) + " col " + (yycolumn+1)); }
