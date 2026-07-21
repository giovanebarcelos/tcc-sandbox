grammar Do;

@header {
import java.util.*;
import java.io.*;
}

@members {
StringBuilder codeC = new StringBuilder();

public static void main(String[] args) throws Exception {
    var input = CharStreams.fromFileName(args[0]);
    var lexer = new DoLexer(input);
    var tokens = new CommonTokenStream(lexer);
    var parser = new DoParser(tokens);

    // Faz o parser lançar erro imediatamente ao encontrar erro de sintaxe
    parser.removeErrorListeners(); // Remove os listeners padrão
    parser.addErrorListener(DoErrorListener.INSTANCE);

    try {
        parser.programa(); // Tenta fazer o parsing
    } catch (RuntimeException e) {
        System.err.println("Erro detectado: " + e.getMessage());
        return; // Encerra o programa
    }

    try (PrintWriter out = new PrintWriter("output.c")) {
        out.println("#include <stdio.h>");
        out.println("int main() {");
        out.println(parser.codeC.toString());
        out.println("return 0;");
        out.println("}");
    }

    // Compilar com gcc
    Process proc = Runtime.getRuntime().exec("gcc output.c -o program.exe");
    proc.waitFor();
    System.out.println("Executable 'program.exe' successfully generated!");
}
}

programa
    : stmt EOF
    ;

stmt
    : DO LCOL ID ATRIB NUM PV ID LT NUM PV ID INC RCOL outStmt
      {
        String forCode = String.format(
            "for (int %s = %s; %s < %s; %s++) {\n%s}\n",
            $ID(0).getText(), $NUM(0).getText(),
            $ID(1).getText(), $NUM(1).getText(),
            $ID(2).getText(), $outStmt.code
        );
        codeC.append(forCode);
      }
    ;

outStmt returns [String code]
    : OUT LPAREN ID RPAREN
      {
        $code = String.format("    printf(\"%%d\\n\", %s);\n", $ID.getText());
      }
    ;

DO      : 'do';
LCOL    : '[';
RCOL    : ']';
LPAREN  : '(';
RPAREN  : ')';
PV      : ';';
ATRIB   : '=';
LT      : '<';
INC     : '++';
OUT     : 'out';

ID      : [a-zA-Z_][a-zA-Z0-9_]*;
NUM     : [0-9]+;

WS      : [ \t\r\n]+ -> skip;
ERROR : . -> channel(HIDDEN);
