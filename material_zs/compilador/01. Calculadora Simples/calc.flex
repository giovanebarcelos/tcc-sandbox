/* Arquivo: calc.flex */
import java_cup.runtime.*;

%%
%class Lexer
%cup
%line
%column

%{
  private Symbol symbol(int type) {
    return new Symbol(type, yyline + 1, yycolumn + 1);
  }
  
  private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline + 1, yycolumn + 1, value);
  }
%}

DIGIT = [0-9]
NUMBER = {DIGIT}+
WHITESPACE = [ \t\r\n]+

%%

/* Operadores */
"+"          { return symbol(sym.PLUS); }
"-"          { return symbol(sym.MINUS); }
"*"          { return symbol(sym.TIMES); }
"/"          { return symbol(sym.DIVIDE); }
"("          { return symbol(sym.LPAREN); }
")"          { return symbol(sym.RPAREN); }

/* Números */
{NUMBER}     { return symbol(sym.NUMBER, Integer.parseInt(yytext())); }

/* Espaços em branco */
{WHITESPACE} { /* ignora */ }

/* Erro */
.            { throw new Error("Caractere ilegal: " + yytext()); }
