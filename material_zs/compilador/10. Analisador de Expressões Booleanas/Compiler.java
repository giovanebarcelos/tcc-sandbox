import java.io.*;

public class Compiler {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Uso: java Compiler <arquivo_fonte>");
            System.out.println("\nExemplos disponíveis:");
            System.out.println("  java Compiler exemplo1.txt");
            System.out.println("  java Compiler exemplo2.txt");
            System.out.println("  java Compiler exemplo_erro.txt");
            return;
        }

        String filename = args[0];
        
        try {
            System.out.println("=".repeat(60));
            System.out.println("COMPILANDO: " + filename);
            System.out.println("=".repeat(60));
            
            FileReader file = new FileReader(filename);
            Lexer lexer = new Lexer(file);
            parser parser = new parser(lexer);
            
            parser.parse();
            
            System.out.println("=".repeat(60));
            
        } catch (FileNotFoundException e) {
            System.err.println("ERRO: Arquivo '" + filename + "' não encontrado!");
        } catch (Exception e) {
            System.err.println("ERRO durante a compilação: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
