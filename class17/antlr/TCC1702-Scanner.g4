// TCC1702-Scanner.g4 | Aula 17
// Gramática lexer ANTLR 4 — mesmo scanner da Aula 16.
// Gera lexer Java e Python (multi-alvo).
// Build: antlr4 -Dlanguage=Python3 TCC1702-Scanner.g4
//        antlr4 -Dlanguage=Java     TCC1702-Scanner.g4

lexer grammar TCC1702Scanner;

// Palavras-chave (devem vir ANTES de ID)
IF:     'if';
ELSE:   'else';
WHILE:  'while';
PRINT:  'print';
INT:    'int';
FLOAT:  'float';

// Operadores
EQ:     '==';
ASSIGN: '=';
PLUS:   '+';
MINUS:  '-';
TIMES:  '*';
DIV:    '/';

// Pontuação
LPAREN: '(';
RPAREN: ')';
LBRACE: '{';
RBRACE: '}';
SEMI:   ';';

// Literais
NUM:    [0-9]+ ('.' [0-9]+)?;
ID:     [a-zA-Z_] [a-zA-Z0-9_]*;

// Comentários e espaços (ignorados)
COMMENT: '#' ~[\n]* -> skip;
WS:     [ \t\r\n]+ -> skip;
