// TCC2402-ErrorListener.java | Aula 24
// ErrorListener customizado para ANTLR (Java) — mensagens amigáveis com linha/coluna.
// Usa a gramática TCC2402-IfElse.g4 gerada com: antlr4 -Dlanguage=Java -visitor TCC2402-IfElse.g4
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import java.util.*;

public class TCC2402ErrorListener extends BaseErrorListener {

    List<String> erros = new ArrayList<>();

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer,
                            Object offendingSymbol,
                            int line, int charPositionInLine,
                            String msg, RecognitionException e) {
        String erro = "Erro de sintaxe na linha " + line + ", coluna " + charPositionInLine + ": " + msg;
        erros.add(erro);
        System.out.println("  ❌ " + erro);
    }

    public static void main(String[] args) {
        System.out.println("=== Código correto ===");
        analisar("x = 3; if (x > 2) { print(x); } else { print(0); }");

        System.out.println("\n=== Código com erro (falta ';') ===");
        analisar("x = 3 if (x > 2) { print(x); }");

        System.out.println("\n=== Código com erro (falta ')') ===");
        analisar("if (x > 2 { print(x); }");

        System.out.println("\n=== ErrorListener demonstra mensagens amigáveis com linha/coluna ===");
        System.out.println("Compare com o símbolo `error` do JCup (recuperação mais rígida).");
    }

    // Após gerar o parser com antlr4, descomente e use:
    // static void analisar(String fonte) {
    //     CharStream input = CharStreams.fromString(fonte);
    //     TCC2402IfElseLexer lexer = new TCC2402IfElseLexer(input);
    //     CommonTokenStream tokens = new CommonTokenStream(lexer);
    //     TCC2402IfElseParser parser = new TCC2402IfElseParser(tokens);
    //     TCC2402ErrorListener listener = new TCC2402ErrorListener();
    //     parser.removeErrorListeners();
    //     parser.addErrorListener(listener);
    //     parser.program();
    // }
    static void analisar(String fonte) {
        System.out.println("  (análise de: \"" + fonte + "\")");
    }
}
