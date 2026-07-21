lexer grammar HTMLLexer;

// Modo padrão: fora de tags (conteúdo de texto puro)
TAG_END_OPEN : '</'     -> pushMode(TAG);
TAG_OPEN     : '<'      -> pushMode(TAG);
TEXT         : ~[<]+;   // Tudo que não começa com '<' é texto
WS           : [ \t\r\n]+ -> skip;

// Dentro de uma tag
mode TAG;

TAG_CLOSE    : '>'      -> popMode;
EQ           : '=';

ATTR_VALUE
    : '"' (~["\r\n])* '"'
    | '\'' (~['\r\n])* '\''
    ;

NAME
    : [a-zA-Z_:][a-zA-Z0-9_.:-]*
    ;

WS_IN_TAG
    : [ \t\r\n]+ -> skip
    ;

COMMENT : '<!--' .*? '-->' -> skip;
