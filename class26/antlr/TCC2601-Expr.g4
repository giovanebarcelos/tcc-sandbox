// TCC2601-Expr.g4 | Aula 26
// Gramática ANTLR 4 de expressões — uma gramática, dois alvos (Java e Python).
// Build Java:   antlr4 -Dlanguage=Java     -visitor TCC2601-Expr.g4
// Build Python: antlr4 -Dlanguage=Python3 -visitor TCC2601-Expr.g4
grammar TCC2601Expr;

prog : expr EOF ;

expr : expr '+' expr   # Add
     | expr '*' expr   # Mul
     | '(' expr ')'    # Parens
     | NUM             # Number
     ;

NUM : [0-9]+ ('.' [0-9]+)? ;
WS  : [ \t\r\n]+ -> skip ;
