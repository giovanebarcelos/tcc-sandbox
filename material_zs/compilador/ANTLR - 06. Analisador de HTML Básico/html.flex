import java_cup.runtime.Symbol;

%%
%public
%class HtmlLexer
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

%state TAG

TAG_OPEN = "<"
TAG_CLOSE = ">"
TAG_END_OPEN = "</"
EQ = "="
DQ_ATTR_VALUE = \"[^\"]*\"
SQ_ATTR_VALUE = \'[^\']*\'
WS = [ \t\r\n]+
NAME = [a-zA-Z_:][a-zA-Z0-9_.:-]*

%%

<YYINITIAL> {
    {WS}                    { /* ignora espaços fora das tags */ }
    {TAG_END_OPEN}          { yybegin(TAG); return symbol(sym.TAG_END_OPEN, yytext()); }
    {TAG_OPEN}              { yybegin(TAG); return symbol(sym.TAG_OPEN, yytext()); }
    [^<]+                   { return symbol(sym.TEXT, yytext()); }
}

<TAG> {
    {WS}                    { /* ignora espaços dentro da tag */ }
    {NAME}                  { return symbol(sym.NAME, yytext()); }
    {EQ}                    { return symbol(sym.EQ, yytext()); }
    {DQ_ATTR_VALUE}         { return symbol(sym.ATTR_VALUE, yytext()); }
    {SQ_ATTR_VALUE}         { return symbol(sym.ATTR_VALUE, yytext()); }
    {TAG_CLOSE}             { yybegin(YYINITIAL); return symbol(sym.TAG_CLOSE, yytext()); }
    .                       { /* return symbol(sym.ERROR, yytext()); */ }
}