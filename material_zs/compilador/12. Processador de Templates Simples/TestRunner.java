import java.nio.file.Files;
import java.nio.file.Paths;

public class TestRunner {
    private static final String[] FILES = {
        "templates/valid_boas_vindas.tpl",
        "templates/valid_pedido.tpl",
        "templates/error_unclosed_if.tpl"
    };

    private static final String[] DESCRIPTIONS = {
        "Template 1: Mensagem de boas-vindas com bloco condicional simples",
        "Template 2: Confirmação de pedido com bloco ELSE",
        "Template 3: ERRO - Bloco condicional sem fechamento"
    };

    public static void main(String[] args) {
        System.out.println("=== Processador de Templates Simples - Testes ===\n");
        for (int i = 0; i < FILES.length; i++) {
            System.out.println("==========================================");
            System.out.println(DESCRIPTIONS[i]);
            System.out.println("Arquivo: " + FILES[i]);
            System.out.println("==========================================");

            try {
                String content = Files.readString(Paths.get(FILES[i]));
                System.out.println("Conteúdo:\n" + content);
                System.out.println("\nResultado:");
                Template template = TemplateCompiler.compile(content);
                System.out.println(template);
            } catch (Error e) {
                System.err.println("\n❌ ERRO DE COMPILAÇÃO (Léxico):");
                System.err.println(e.getMessage());
            } catch (Exception e) {
                System.err.println("\n❌ ERRO DE COMPILAÇÃO (Sintático):");
                System.err.println(e.getMessage());
            }

            System.out.println("\n");
        }
    }
}
