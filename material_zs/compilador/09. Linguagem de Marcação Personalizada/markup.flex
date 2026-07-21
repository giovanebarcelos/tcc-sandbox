/* =============================================================
   Lexer para Linguagem de Marcação Personalizada
   ============================================================= */

%%

%cup
%class MarkupLexer
%unicode
%public
%line
%column
%type java_cup.runtime.Symbol

%{
   private java_cup.runtime.Symbol symbol(int type) {
      return new java_cup.runtime.Symbol(type, yyline + 1, yycolumn + 1);
   }

   private java_cup.runtime.Symbol symbol(int type, Object value) {
      return new java_cup.runtime.Symbol(type, yyline + 1, yycolumn + 1, value);
   }

   private void lexicalError(String extra) {
      throw new Error(extra + " (linha " + (yyline + 1) + ", coluna " + (yycolumn + 1) + ")");
   }
%}

ALPHA       = [A-Za-z]
IDENT       = {ALPHA}+
ATTR_VALUE  = [^\]\r\n]+
TEXT        = [^\r\n\[]+
NEWLINE     = \r\n|\r|\n

%%

{NEWLINE} {
   return symbol(sym.NEWLINE, yytext());
}

"["{IDENT}"="{ATTR_VALUE}"]" {
   String text = yytext();
   int equalIndex = text.indexOf('=');
   String name = text.substring(1, equalIndex);
   String attr = text.substring(equalIndex + 1, text.length() - 1).trim();
   return symbol(sym.TAG_OPEN, new Tag(name, attr, yyline + 1, yycolumn + 1));
}

"["{IDENT}"]" {
   String text = yytext();
   String name = text.substring(1, text.length() - 1);
   return symbol(sym.TAG_OPEN, new Tag(name, null, yyline + 1, yycolumn + 1));
}

"[/"{IDENT}"]" {
   String text = yytext();
   String name = text.substring(2, text.length() - 1);
   return symbol(sym.TAG_CLOSE, new Tag(name, null, yyline + 1, yycolumn + 1));
}

{TEXT} {
   return symbol(sym.TEXT, yytext());
}

"[" {
   lexicalError("Erro léxico: colchete '[' inesperado");
}

<<EOF>> {
   return symbol(sym.EOF);
}


/* [u] */
