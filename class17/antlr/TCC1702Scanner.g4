// TCC1702Scanner.g4 | Aula 17
// Gramática lexer ANTLR 4 — mesmo scanner da Aula 16.
// Gera lexer Java e Python (multi-alvo).
// O nome do arquivo DEVE ser igual ao da gramática (TCC1702Scanner), por isso sem hífen.
// Build Java:   antlr4 TCC1702Scanner.g4
//               javac TCC1702Scanner.java
//               grun TCC1702Scanner tokens -tokens entrada.txt
// Build Python: antlr4 -Dlanguage=Python3 TCC1702Scanner.g4
//               python3 TCC1702-Main.py entrada.txt
//               (requer: pip install antlr4-python3-runtime==4.13.1)

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
COMMENT: '#' ~[\r\n]* -> skip;
WS:     [ \t\r\n]+ -> skip;
