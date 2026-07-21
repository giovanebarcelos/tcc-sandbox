import java_cup.runtime.*; 

%%
%class DoLexer 
%unicode
%cup
%line
%column
//%debug

%{
    private Symbol symbol(int type){
        return new Symbol(type, yyline + 1, yycolumn + 1);
    }

    private Symbol symbol(int type, Object value){
        return new Symbol(type, yyline + 1, yycolumn + 1, value);
    }
%}

ID = [a-zA-Z][a-zA-Z_0-9]*
DIGITO = [0-9]
NUMBER = {DIGITO}+

%%

"do" { return symbol(sym.DO);}
"[" { return symbol(sym.LCOL);}
"]" { return symbol(sym.RCOL);}
"=" { return symbol(sym.ATRIB);}
"<" { return symbol(sym.LT);}
";" { return symbol(sym.PV);}
"++" { return symbol(sym.INC);}
"out"  { return symbol(sym.OUT);}
"(" { return symbol(sym.LPAREN);}
")" { return symbol(sym.RPAREN);}

{ID} { return symbol(sym.ID, yytext());}
{NUMBER}  { return symbol(sym.NUM, Integer.parseInt(yytext()));}

[ \t\r\n]+ { /* IGNORE */ }

. { System.err.println("Lexical error: inexpected charactger '" + 
                       yytext() + "' in the line " + (yyline + 1)+
                       " and column " + (yycolumn+1));}
