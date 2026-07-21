import java_cup.runtime.*;
%%
%class JSONLexer 
%unicode
%cup
%line
%column

%{
   private Symbol symbol(int type, Object value) {
       return new Symbol(type, yyline + 1, yycolumn + 1, value);
   }
   private Symbol symbol(int type) {
       return new Symbol(type, yyline + 1, yycolumn + 1);
   }
%}

digit = [0-9]
int = -?{digit}+
float = -?{digit}+\.{digit}+
string = \"([^\"\\]|\\.)*\"
whitespace = [ \t\n\r]

%%
"{"    { return symbol(sym.LBRACE); }
"}"    { return symbol(sym.RBRACE); }
":"    { return symbol(sym.COLON); }
","    { return symbol(sym.COMMA); }
"true"  { return symbol(sym.TRUE); }
"false" { return symbol(sym.FALSE); }
"["   { return symbol(sym.LBRACKET); }
"]"    { return symbol(sym.RBRACKET); }
"null"  { return symbol(sym.NULL); }
{string} { return symbol(sym.STRING, 
             yytext().substring(1, yytext().length() -1)); }
{int}   { return symbol(sym.INT, Integer.valueOf(yytext())); }
{float} { return symbol(sym.FLOAT, Float.valueOf(yytext())); }
{whitespace} { /* ignore whitespace */ }

.            {
    throw new Error("Erro léxico na linha " + (yyline + 1) +
                    ", coluna " + (yycolumn + 1) +
                    ": caractere inválido '" + yytext() + "'");
}

<<EOF>> { return symbol(sym.EOF); }
