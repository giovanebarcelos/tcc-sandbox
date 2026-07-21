import java_cup.runtime.*;

%%

%class Lexer
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

LineTerminator = \r|\n|\r\n
WhiteSpace = {LineTerminator} | [ \t\f]
Comment = "//" [^\r\n]*

Identifier = [a-zA-Z_][a-zA-Z0-9_]*
Number = [0-9]+ ("." [0-9]+)?
String = \"[^\"]*\"

%%

<YYINITIAL> {
    // Palavras-chave de formas
    "circulo"           { return symbol(sym.CIRCULO); }
    "retangulo"         { return symbol(sym.RETANGULO); }
    "triangulo"         { return symbol(sym.TRIANGULO); }
    "quadrado"          { return symbol(sym.QUADRADO); }
    "elipse"            { return symbol(sym.ELIPSE); }
    
    // Propriedades
    "cor"               { return symbol(sym.COR); }
    "posicao"           { return symbol(sym.POSICAO); }
    "raio"              { return symbol(sym.RAIO); }
    "largura"           { return symbol(sym.LARGURA); }
    "altura"            { return symbol(sym.ALTURA); }
    "lado"              { return symbol(sym.LADO); }
    "base"              { return symbol(sym.BASE); }
    
    // Símbolos
    "{"                 { return symbol(sym.LBRACE); }
    "}"                 { return symbol(sym.RBRACE); }
    "("                 { return symbol(sym.LPAREN); }
    ")"                 { return symbol(sym.RPAREN); }
    ","                 { return symbol(sym.COMMA); }
    ":"                 { return symbol(sym.COLON); }
    ";"                 { return symbol(sym.SEMICOLON); }
    
    // Literais
    {Number}            { return symbol(sym.NUMBER, Double.parseDouble(yytext())); }
    {String}            { return symbol(sym.STRING, yytext().substring(1, yytext().length()-1)); }
    {Identifier}        { return symbol(sym.ID, yytext()); }
    
    // Ignorar
    {WhiteSpace}        { /* ignorar */ }
    {Comment}           { /* ignorar */ }
}

// Tratamento de erro léxico
[^] {
    throw new Error("Erro léxico na linha " + (yyline + 1) + 
                    ", coluna " + (yycolumn + 1) + 
                    ": caractere inválido '" + yytext() + "'");
}
