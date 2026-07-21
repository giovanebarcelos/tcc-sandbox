lexer grammar HTMLLexer;

TAG_OPEN: '<' -> pushMode(TAG);
TAG_END_OPEN: '</' -> pushMode(TAG);
TAG_CLOSE: '>' -> popMode;
EQ: '=';
WS: [ \t\r\n]+;
TEXT: ~[<]+;

mode TAG;

ATTR_VALUE: '"' ( ~["] | '""' )* '"';

NAME: [a-zA-Z_:][a-zA-Z0-9_.:-]*;

WS_IN_TAG: [ \t\r\n]+ -> skip;

COMMENT: '<!--' .*? '-->' -> skip;


