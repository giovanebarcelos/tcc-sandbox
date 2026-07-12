// TCC2402-IfElse.g4 | Aula 24
// Gramática ANTLR 4 com IF/ELSE, DO/WHILE e recuperação de erros.
// Build: antlr4 -Dlanguage=Python3 -visitor TCC2402-IfElse.g4
//        antlr4 -Dlanguage=Java     -visitor TCC2402-IfElse.g4
grammar TCC2402IfElse;

program : stmt+ EOF ;

stmt : 'if' '(' expr ')' block ('else' block)?   # IfElse
     | 'do' block 'while' '(' expr ')' ';'        # DoWhile
     | ID '=' expr ';'                            # Assign
     | 'print' '(' expr ')' ';'                    # Print
     ;

block : '{' stmt* '}' ;

expr : expr '+' term   # Add
     | expr '-' term   # Sub
     | expr '>' term   # Gt
     | expr '<' term   # Lt
     | term            # ExprTerm
     ;

term : term '*' factor # Mul
     | factor          # TermFactor
     ;

factor : '(' expr ')' # Parens
       | NUM          # Number
       | ID           # Var
       ;

IF    : 'if' ;
ELSE  : 'else' ;
DO    : 'do' ;
WHILE : 'while' ;
PRINT : 'print' ;
ID    : [a-zA-Z_] [a-zA-Z0-9_]* ;
NUM   : [0-9]+ ('.' [0-9]+)? ;
WS    : [ \t\r\n]+ -> skip ;
