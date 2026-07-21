import java_cup.runtime.*;

%%

%class IfElseLexer
%unicode
%cup
%line
%column

%{

StringBuffer string = new StringBuffer();

// Tokens para cup
private Symbol symbol(int type) {
    return new Symbol(type, yyline+1, yycolumn+1);
}

private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline+1, yycolumn+1, value);
}

%}

DIGITO = [0-9]
ID = [a-zA-Z_][a-zA-Z_0-9]*

%%

"if"        { return symbol(sym.IF); }
"else"      { return symbol(sym.ELSE); }

"("         { return symbol(sym.LPAREN); }
")"         { return symbol(sym.RPAREN); }
";"         { return symbol(sym.PV); }
"="         { return symbol(sym.ATRIB); }

"=="        { return symbol(sym.EQ); }
"<"         { return symbol(sym.LT); }
">"         { return symbol(sym.GT); }

{ID}        { return symbol(sym.ID, yytext()); }
{DIGITO}+   { return symbol(sym.NUM, Integer.parseInt(yytext())); }

[ \t\r\n]+  { /* ignora espaços em branco */ }
. {
  throw new RuntimeException(
    "Erro léxico: caractere inesperado '" + yytext() +
    "' na linha " + (yyline + 1) + ", coluna " + (yycolumn + 1));
}
