grammar Do;

@header {
   import java.util.*;
   import java.io.*;
   import org.antlr.v4.runtime.*;
   import org.antlr.v4.runtime.tree.*;
}

@members {
   StringBuilder sb = new StringBuilder();

   public static void main(String[] args) throws Exception {
      DoLexer lexer = 
         new DoLexer(CharStreams.fromFileName(args[0]));
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      DoParser parser = new DoParser(tokens);
      ParseTree tree = parser.program();
      System.out.println(tree.toStringTree(parser));
      System.out.println("Parsing completed successfully.");
      System.out.println("\nAstha La Vista Baby!");

      System.out.println("Generating C code...");
      parser.createCodeC();

      Process prc = Runtime.getRuntime().exec(
         "gcc output.c -o program.exe");
      prc.waitFor(); 
      System.out.println("\nExecutable 'program.exe' successfully generated!\n");
   }

   public void createCodeC() throws Exception{    
    try (PrintWriter out = 
         new PrintWriter(new FileWriter("output.c"))) {
            out.println("#include <stdio.h>");
            out.println("int main() {");
            out.println(sb.toString());
            out.println("return 0;");
            out.println("}");
      } catch (IOException e) {
         System.err.println("Error writing to file: " + e.getMessage());
      }
  }

}

// Parser rules
// Syntatic rules
program: stmt EOF;

stmt: DO LCOL atribs PV decisions PV stepStmt RCOL outStmt
      { 
        String forCode = String.format(
          "for (int %s; %s; %s) %s",
          $atribs.atrib,
          $decisions.decision,
          $stepStmt.step,
          $outStmt.out
        ); 

        sb.append(forCode);
      };

atribs returns [String atrib]: ID ATRIB NUM 
   {
      $atrib = $ID.getText() + 
               $ATRIB.getText() + 
               $NUM.getText();
   };

decisions returns [String decision]: ID operator NUM
   {
      $decision = $ID.getText() + 
                  $operator.oper + 
                  $NUM.getText();
   };

operator returns [String oper]: 
   LT {$oper = $LT.getText(); } |
   GT {$oper = $GT.getText(); } | 
   LTE  {$oper = $LTE.getText(); } |
   GTE {$oper = $GTE.getText(); } | 
   DIFF {$oper = $DIFF.getText(); };

stepStmt returns [String step]: ID INC 
   {
      $step = $ID.getText() + 
              $INC.getText();
   };

outStmt returns [String out]: OUT LPAREN ID RPAREN 
   {
      $out = "{ printf( \"%d\\n\", " + 
              $LPAREN.getText() + 
              $ID.getText() + 
              $RPAREN.getText() + ");}";
   };

// Scanner rules
// Lexer rules
DO: 'do';
LCOL: '[';
RCOL: ']';
ATRIB: '=';
LT: '<';
GT: '>';
LTE: '<=';
GTE: '>=';
DIFF: '!=';
PV: ';';
INC: '++';
OUT: 'out';
LPAREN: '(';
RPAREN: ')';

ID: [a-zA-Z_][a-zA-Z0-9_]*;
NUM: [0-9]+;
WS: [ \t\r\n]+ -> skip;
