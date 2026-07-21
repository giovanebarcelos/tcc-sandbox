import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.err.println("Uso: java Main <arquivo.mark>");
            System.exit(1);
        }

        try (Reader reader = new BufferedReader(new FileReader(args[0]))) {
            MarkupLexer lexer = new MarkupLexer(reader);
            parser p = new parser(lexer);
            p.parse();
            System.out.println("\n=== Parse concluído sem erros ===");
        } catch (Exception e) {
            System.err.println("Erro durante parse: " + e.getMessage());
            throw e;
        }
    }
}

