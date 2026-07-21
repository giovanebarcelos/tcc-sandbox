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
    
    public int getLine() {
        return yyline + 1;
    }
    
    public int getColumn() {
        return yycolumn + 1;
    }
%}

WHITESPACE = [ \t\r\n]+
IDENTIFIER = [a-zA-Z_][a-zA-Z0-9_]*
SYMBOL = [a-zA-Z0-9]

%%

<YYINITIAL> {
    "automaton"     { return symbol(sym.AUTOMATON); }
    "states"        { return symbol(sym.STATES); }
    "alphabet"      { return symbol(sym.ALPHABET); }
    "initial"       { return symbol(sym.INITIAL); }
    "final"         { return symbol(sym.FINAL); }
    "transitions"   { return symbol(sym.TRANSITIONS); }
    "end"           { return symbol(sym.END); }
    
    "{"             { return symbol(sym.LBRACE); }
    "}"             { return symbol(sym.RBRACE); }
    ","             { return symbol(sym.COMMA); }
    ":"             { return symbol(sym.COLON); }
    "->"            { return symbol(sym.ARROW); }
    ";"             { return symbol(sym.SEMICOLON); }
    
    {IDENTIFIER}    { return symbol(sym.IDENTIFIER, yytext()); }
    {SYMBOL}        { return symbol(sym.SYMBOL, yytext()); }
    {WHITESPACE}    { /* ignore */ }
    
    .               { 
        throw new Error("Erro léxico na linha " + (yyline + 1) + 
                       ", coluna " + (yycolumn + 1) + 
                       ": caractere inválido '" + yytext() + "'"); 
    }
}
