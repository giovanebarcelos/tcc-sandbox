import java_cup.runtime.*;

%%

%class DoLexer
%unicode
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

DIGITO = [0-9]
ID = [a-zA-Z_][a-zA-Z0-9_]*

%%

"do"        { return symbol(sym.DO); }
"["         { return symbol(sym.LCOL); }
"]"         { return symbol(sym.RCOL); }
"("         { return symbol(sym.LPAREN); }
")"         { return symbol(sym.RPAREN); }
";"         { return symbol(sym.PV); }
"="         { return symbol(sym.ATRIB); }
"<"         { return symbol(sym.LT); }
"++"        { return symbol(sym.INC); }
"out"       { return symbol(sym.OUT); }

{ID}        { return symbol(sym.ID, yytext()); }
{DIGITO}+   { return symbol(sym.NUM, Integer.parseInt(yytext())); }

[ \t\r\n]+  { /* ignorar espaços */ }

. {
  throw new RuntimeException(
    "Erro léxico: caractere inesperado '" + yytext() +
    "' na linha " + (yyline + 1) + ", coluna " + (yycolumn + 1));
}
