// TCC2103CalcAcoes.g4 | Aula 21
// Calculadora ANTLR 4 com ações embutidas (alvo Java) — o estilo do JCup.
// Build: antlr4 TCC2103CalcAcoes.g4 && javac TCC2103CalcAcoes*.java
// Run:   grun TCC2103CalcAcoes calc entrada.txt
grammar TCC2103CalcAcoes;

calc  : linha+ EOF ;

linha : expr SEMI { System.out.println("Resultado: " + $expr.v); } ;

expr returns [double v]
     : e1=expr PLUS  e2=term   { $v = $e1.v + $e2.v; }
     | e1=expr MINUS e2=term   { $v = $e1.v - $e2.v; }
     | t=term                  { $v = $t.v; }
     ;

term returns [double v]
     : t1=term TIMES f=factor  { $v = $t1.v * $f.v; }
     | t1=term DIV   f=factor  { $v = $t1.v / $f.v; }
     | f=factor                { $v = $f.v; }
     ;

factor returns [double v]
       : LPAREN e=expr RPAREN  { $v = $e.v; }
       | NUM                   { $v = Double.parseDouble($NUM.text); }
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
