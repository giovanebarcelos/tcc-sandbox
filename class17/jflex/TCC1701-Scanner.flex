// TCC1701-Scanner.flex | Aula 17
// Especificação JFlex do mesmo scanner da Aula 16.
// Gera um lexer Java que reconhece: KEYWORD, NUM, ID, OP, PUNCT.
// Build (na pasta com TCC1701-Scanner.flex, TCC1701-Main.java e entrada.txt):
//        jflex TCC1701-Scanner.flex          -> gera TCC1701Scanner.java
//        javac TCC1701Scanner.java TCC1701-Main.java
//        java TCC1701Main entrada.txt
// Requer java-cup-11b-runtime.jar no CLASSPATH (classe java_cup.runtime.Symbol).

import java_cup.runtime.Symbol;

%%

%class TCC1701Scanner
%public
%unicode
%line
%column
%type Symbol
%function next_token

%{
  // Códigos dos tokens. Na Aula 21 eles virão da classe sym gerada pelo JCup.
  public static final int EOF = 0, KEYWORD = 1, NUM = 2, ID = 3, OP = 4, PUNCT = 5;
  public static final String[] NOMES = { "EOF", "KEYWORD", "NUM", "ID", "OP", "PUNCT" };

  // Symbol(tipo, linha, coluna, valor) — linha/coluna a partir de 1
  private Symbol token(int tipo) {
    return new Symbol(tipo, yyline + 1, yycolumn + 1, yytext());
  }
%}

%eofval{
  return new Symbol(EOF, yyline + 1, yycolumn + 1, null);
%eofval}

LineTerminator = \r|\n|\r\n
WhiteSpace     = {LineTerminator} | [ \t\f]
Comment        = "#"[^\r\n]*
Digit          = [0-9]
Number         = {Digit}+ ("." {Digit}+)?
Identifier     = [a-zA-Z_][a-zA-Z0-9_]*
Keyword        = "if" | "else" | "while" | "print" | "int" | "float"
Operator       = "+" | "-" | "*" | "/" | "=" | "=="
Punctuation    = "(" | ")" | "{" | "}" | ";"

%%

{Comment}        { /* ignora */ }
{WhiteSpace}     { /* ignora */ }
{Keyword}        { return token(KEYWORD); }
{Number}         { return token(NUM); }
{Identifier}     { return token(ID); }
{Operator}       { return token(OP); }
{Punctuation}    { return token(PUNCT); }

[^]              { throw new RuntimeException("Caractere inválido: '" + yytext() + "' linha " + (yyline+1) + " col " + (yycolumn+1)); }
