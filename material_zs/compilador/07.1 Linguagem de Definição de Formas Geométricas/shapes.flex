import java_cup.runtime.*;

%%

%class ShapesLexer
%unicode
%cup
%line
%column

%{
    private Symbol symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }
    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
    }
%}

digit = [0-9]
number = {digit}+(\.{digit}+)?
color = #[0-9a-fA-F]{6}
id = [a-zA-Z][a-zA-Z0-9]*
whitespace = [ \t\n\r]

%%

"shape" { return symbol(sym.SHAPE); }
"rectangle" { return symbol(sym.RECTANGLE); }
"circle" { return symbol(sym.CIRCLE); }
"triangle" { return symbol(sym.TRIANGLE); }
"{" { return symbol(sym.LBRACE); }
"}" { return symbol(sym.RBRACE); }
":" { return symbol(sym.COLON); }
"," { return symbol(sym.COMMA); }
"width" { return symbol(sym.WIDTH); }
"height" { return symbol(sym.HEIGHT); }
"radius" { return symbol(sym.RADIUS); }
"color" { return symbol(sym.COLOR); }
"position" { return symbol(sym.POSITION); }
{id} { return symbol(sym.ID, yytext()); }
{number} { return symbol(sym.NUMBER, Double.parseDouble(yytext())); }
{color} { return symbol(sym.COLORVAL, yytext()); }
{whitespace} { /* ignore */ }

<<EOF>> { return symbol(sym.EOF); }
