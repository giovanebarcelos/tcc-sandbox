import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.io.*;

public class Main {
    public static void main(String[] args) throws Exception {
        CharStream input = CharStreams.fromFileName(args[0]);
        IfElseLexer lexer = new IfElseLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        IfElseParser parser = new IfElseParser(tokens);

        parser.removeErrorListeners();
        parser.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer,
                                    Object offendingSymbol,
                                    int line, int charPositionInLine,
                                    String msg,
                                    RecognitionException e) {
                System.err.printf("Erro sintático: linha %d:%d - %s%n", line, charPositionInLine, msg);
                throw new RuntimeException("Erro de parsing");
            }
        });

        try {
            parser.programa(); // preenche parser.codeC
        } catch (RuntimeException e) {
            System.err.println("Parsing interrompido.");
            return;
        }

        // Gerar arquivo .c
        try (PrintWriter out = new PrintWriter("saida.c")) {
            out.println("#include <stdio.h>");
            out.println("int main() {");
            out.println("int a = 0, b = 0, c = 0;");
            out.println(parser.getCodeC().toString());
            out.println("printf(\"\\nAstha La Vista Baby!\\n\");");
            out.println("return 0;");
            out.println("}");
        }

        // Compilar com GCC
        Process p = Runtime.getRuntime().exec("gcc saida.c -o programa.exe");
        p.waitFor();

        System.out.println("Executável 'programa.exe' gerado com sucesso!");
    }
}

