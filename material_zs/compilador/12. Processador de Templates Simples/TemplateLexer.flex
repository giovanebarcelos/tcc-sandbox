import java_cup.runtime.*;

%%

%class Lexer
%cup
%unicode
%line
%column

%{
    private Symbol symbol(int type) {
        return new Symbol(type, yyline + 1, yycolumn + 1);
    }

    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline + 1, yycolumn + 1, value);
    }

    private void throwLexicalError(String lexeme) {
        throw new Error("Erro léxico na linha " + (yyline + 1) +
                        ", coluna " + (yycolumn + 1) +
                        ": caractere inválido '" + lexeme + "'");
    }
%}

WHITESPACE = [ \t\r\n]+
IDENT       = [a-zA-Z_][a-zA-Z0-9_]*
TEXT_LINE   = [^\{\n]+

%xstate VAR_STATE CTRL_STATE

%%

<YYINITIAL> {
    "{{"        { yybegin(VAR_STATE); return symbol(sym.VAR_OPEN); }
    "{%"        { yybegin(CTRL_STATE); return symbol(sym.CTL_OPEN); }
    {TEXT_LINE}+ { return symbol(sym.TEXT, yytext()); }
    "\n"        { return symbol(sym.TEXT, yytext()); }
    "{"         { return symbol(sym.TEXT, yytext()); }
    <<EOF>>      { return symbol(sym.EOF); }
}

<VAR_STATE> {
    "}}"        { yybegin(YYINITIAL); return symbol(sym.VAR_CLOSE); }
    {WHITESPACE} { /* ignore */ }
    {IDENT}      { return symbol(sym.IDENT, yytext()); }
    .            { throwLexicalError(yytext()); }
    <<EOF>>      { throwLexicalError("EOF inesperado dentro de '{{ }}'"); }
}

<CTRL_STATE> {
    "%}"        { yybegin(YYINITIAL); return symbol(sym.CTL_CLOSE); }
    {WHITESPACE} { /* ignore */ }
    "if"        { return symbol(sym.IF); }
    "else"      { return symbol(sym.ELSE); }
    "endif"     { return symbol(sym.ENDIF); }
    {IDENT}      { return symbol(sym.IDENT, yytext()); }
    .            { throwLexicalError(yytext()); }
    <<EOF>>      { throwLexicalError("EOF inesperado dentro de '{% %}'"); }
}