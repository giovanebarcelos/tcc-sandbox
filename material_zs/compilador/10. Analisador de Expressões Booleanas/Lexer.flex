/* Lexer.flex - Analisador Léxico */

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
%}

/* Definições de padrões */
WHITESPACE = [ \t\r\n]+
DIGIT = [0-9]
LETTER = [a-zA-Z]
ID = {LETTER}({LETTER}|{DIGIT}|_)*
NUMBER = {DIGIT}+
COMMENT = "//".*

%%

/* Palavras reservadas */
"program"       { return symbol(sym.PROGRAM); }
"begin"         { return symbol(sym.BEGIN); }
"end"           { return symbol(sym.END); }
"int"           { return symbol(sym.INT); }
"float"         { return symbol(sym.FLOAT); }
"if"            { return symbol(sym.IF); }
"then"          { return symbol(sym.THEN); }
"else"          { return symbol(sym.ELSE); }
"while"         { return symbol(sym.WHILE); }
"do"            { return symbol(sym.DO); }
"read"          { return symbol(sym.READ); }
"write"         { return symbol(sym.WRITE); }

/* Operadores e pontuação */
"+"             { return symbol(sym.PLUS); }
"-"             { return symbol(sym.MINUS); }
"*"             { return symbol(sym.TIMES); }
"/"             { return symbol(sym.DIVIDE); }
"="             { return symbol(sym.ASSIGN); }
"=="            { return symbol(sym.EQ); }
"!="            { return symbol(sym.NEQ); }
"<"             { return symbol(sym.LT); }
">"             { return symbol(sym.GT); }
"<="            { return symbol(sym.LE); }
">="            { return symbol(sym.GE); }
"("             { return symbol(sym.LPAREN); }
")"             { return symbol(sym.RPAREN); }
"{"             { return symbol(sym.LBRACE); }
"}"             { return symbol(sym.RBRACE); }
";"             { return symbol(sym.SEMI); }
","             { return symbol(sym.COMMA); }

/* Identificadores e números */
{ID}            { return symbol(sym.ID, yytext()); }
{NUMBER}        { return symbol(sym.NUMBER, Integer.parseInt(yytext())); }

/* Ignorar espaços e comentários */
{WHITESPACE}    { /* ignora */ }
{COMMENT}       { /* ignora */ }

/* Tratamento de erros */
.               { 
                  System.err.println("ERRO LÉXICO na linha " + (yyline + 1) + 
                                   ", coluna " + (yycolumn + 1) + 
                                   ": caractere inválido '" + yytext() + "'");
                  return symbol(sym.error);
                }
