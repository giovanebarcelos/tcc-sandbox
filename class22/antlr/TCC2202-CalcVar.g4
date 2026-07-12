// TCC2202-CalcVar.g4 | Aula 22
// Gramática ANTLR 4 da calculadora com variáveis — para Visitor semântico.
// Gera parser Java e Python (multi-alvo).
// Build: antlr4 -Dlanguage=Python3 -visitor TCC2202-CalcVar.g4
//        antlr4 -Dlanguage=Java     -visitor TCC2202-CalcVar.g4
grammar TCC2202CalcVar;

program : stmt+ EOF ;

stmt : ID '=' expr ';'        # Assign
     | 'print' '(' expr ')' ';' # Print
     ;

expr : expr '+' term   # Add
     | expr '-' term   # Sub
     | term            # ExprTerm
     ;

term : term '*' factor # Mul
     | term '/' factor # Div
     | factor          # TermFactor
     ;

factor : '(' expr ')' # Parens
       | NUM          # Number
       | ID           # Var
       ;

PRINT : 'print' ;
ID    : [a-zA-Z_] [a-zA-Z0-9_]* ;
NUM   : [0-9]+ ('.' [0-9]+)? ;
WS    : [ \t\r\n]+ -> skip ;
