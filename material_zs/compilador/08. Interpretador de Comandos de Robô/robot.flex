import java_cup.runtime.*;

%%

%class RobotLexer
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

    private void throwLexicalError() {
        throw new Error(
            "Erro léxico na linha " + (yyline + 1) +
            ", coluna " + (yycolumn + 1) +
            ": caractere inválido '" + yytext() + "'"
        );
    }
%}

digit = [0-9]
number = {digit}+
whitespace = [ \t\n\r]

%%

"MOVE" { return symbol(sym.MOVE); }
"LEFT" { return symbol(sym.LEFT); }
"RIGHT" { return symbol(sym.RIGHT); }
"LOOP" { return symbol(sym.LOOP); }
"TIMES" { return symbol(sym.TIMES); }
"{" { return symbol(sym.LBRACE); }
"}" { return symbol(sym.RBRACE); }
{number} { return symbol(sym.NUMBER, Integer.parseInt(yytext())); }
{whitespace} { /* ignore */ }

. { throwLexicalError(); }

<<EOF>> { return symbol(sym.EOF); }