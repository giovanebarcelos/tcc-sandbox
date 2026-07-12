// TCC2102-Calc.g4 | Aula 21
// Gramática ANTLR 4 da calculadora — mesma linguagem do JFlex+JCup.
// Gera parser Java e Python (multi-alvo).
// Build: antlr4 -Dlanguage=Python3 TCC2102-Calc.g4
//        antlr4 -Dlanguage=Java     TCC2102-Calc.g4
grammar TCC2102Calc;

calc : expr SEMI EOF ;

expr : expr PLUS  term   # Add
     | expr MINUS term   # Sub
     | term              # ExprTerm
     ;

term : term TIMES factor # Mul
     | term DIV   factor # Div
     | factor            # TermFactor
     ;

factor : LPAREN expr RPAREN # Parens
       | NUM                # Number
       ;

PLUS   : '+' ;
MINUS  : '-' ;
TIMES  : '*' ;
DIV    : '/' ;
LPAREN : '(' ;
RPAREN : ')' ;
SEMI   : ';' ;
NUM    : [0-9]+ ('.' [0-9]+)? ;
WS     : [ \t\r\n]+ -> skip ;
