// TCC2601Expr.g4 | Aula 26
// Gramática ANTLR 4 de expressões — uma gramática, dois alvos (Java e Python).
// Build Java:   antlr4 -Dlanguage=Java -visitor TCC2601Expr.g4
//               javac TCC2601Expr*.java TCC2601-Main.java
//               java TCC2601Main "3 + 4 * 2"
// Build Python: antlr4 -Dlanguage=Python3 -visitor TCC2601Expr.g4
//               python3 TCC2601-main.py "3 + 4 * 2"   (pip install antlr4-python3-runtime==4.13.1)
grammar TCC2601Expr;

prog : expr EOF ;

expr : expr '+' expr   # Add
     | expr '*' expr   # Mul
     | '(' expr ')'    # Parens
     | NUM             # Number
     ;

NUM : [0-9]+ ('.' [0-9]+)? ;
WS  : [ \t\r\n]+ -> skip ;
