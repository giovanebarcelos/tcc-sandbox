// TCC2103CalcAcoesPy.g4 | Aula 21
// Calculadora ANTLR 4 com ações embutidas (alvo Python3) — o estilo do JCup/PLY.
// Mesma gramática do TCC2103CalcAcoes.g4, com as ações escritas em Python.
// Atenção: no alvo Python a ação começa colada na chave ({print(...)}), sem espaço,
// senão o código gerado fica com indentação errada (IndentationError).
// Build: antlr4 -Dlanguage=Python3 TCC2103CalcAcoesPy.g4
// Run:   pygrun TCC2103CalcAcoesPy calc entrada.txt
grammar TCC2103CalcAcoesPy;

calc  : linha+ EOF ;

linha : expr SEMI {print("Resultado:", $expr.v)} ;

expr returns [float v=0.0]
     : e1=expr PLUS  e2=term   {$v = $e1.v + $e2.v}
     | e1=expr MINUS e2=term   {$v = $e1.v - $e2.v}
     | t=term                  {$v = $t.v}
     ;

term returns [float v=0.0]
     : t1=term TIMES f=factor  {$v = $t1.v * $f.v}
     | t1=term DIV   f=factor  {$v = $t1.v / $f.v}
     | f=factor                {$v = $f.v}
     ;

factor returns [float v=0.0]
       : LPAREN e=expr RPAREN  {$v = $e.v}
       | NUM                   {$v = float($NUM.text)}
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
