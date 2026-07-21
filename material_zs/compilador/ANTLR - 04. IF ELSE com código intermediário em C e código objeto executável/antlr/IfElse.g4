grammar IfElse;

@header {
import java.util.*;
}

@members {
    StringBuilder codeC = new StringBuilder();

    public StringBuilder getCodeC() {
        return codeC;
    }
}

// PARSER RULES
programa
    : stmt EOF { codeC.append($stmt.result); }
    ;

stmt returns [String result]
    : IF LPAREN c=cond RPAREN b1=bloco ELSE b2=bloco
      {
        $result = "if (" + $c.result + ") {\n" + $b1.result + "} else {\n" + $b2.result + "}\n";
      }
    ;

cond returns [String result]
    : var=ID oper=op num=NUM
      {
        $result = $var.getText() + " " + $oper.result + " " + $num.getText();
      }
    ;

op returns [String result]
    : EQ { $result = "=="; }
    | LT { $result = "<";  }
    | GT { $result = ">";  }
    ;

bloco returns [String result]
    : var=ID ATRIB num=NUM PV
      {
        $result = $var.getText() + " = " + $num.getText() + ";\n";
      }
    ;

// LEXER RULES
IF      : 'if';
ELSE    : 'else';
LPAREN  : '(';
RPAREN  : ')';
PV      : ';';
ATRIB   : '=';
EQ      : '==';
LT      : '<';
GT      : '>';

ID      : [a-zA-Z_][a-zA-Z_0-9]*;
NUM     : [0-9]+;

WS      : [ \t\r\n]+ -> skip;

