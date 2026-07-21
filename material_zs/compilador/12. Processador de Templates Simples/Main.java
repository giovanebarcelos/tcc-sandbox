public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Uso: java Main <arquivo.tpl>");
            System.exit(1);
        }

        String templatePath = args[0];
        try {
            Template template = TemplateCompiler.compileFile(templatePath);
            System.out.println("Template compilado com sucesso!\n" + template);
        } catch (Error e) {
            System.err.println("\n❌ ERRO DE COMPILAÇÃO (Léxico):");
            System.err.println(e.getMessage());
            System.exit(2);
        } catch (Exception e) {
            System.err.println("\n❌ ERRO DE COMPILAÇÃO (Sintático):");
            System.err.println(e.getMessage());
            System.exit(3);
        }
    }
}
