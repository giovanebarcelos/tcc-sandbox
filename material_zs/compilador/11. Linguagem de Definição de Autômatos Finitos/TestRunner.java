import java.io.*;
import java.nio.file.*;

public class TestRunner {
    public static void main(String[] args) {
        System.out.println("=== Compilador de Autômatos Finitos - Testes ===\n");
        
        String[] testFiles = {"test1.dfa", "test2.dfa", "test3.dfa"};
        String[] descriptions = {
            "Teste 1: Autômato que aceita palavras terminadas em 'a'",
            "Teste 2: Autômato que aceita número par de 'a's",
            "Teste 3: ERRO - Sintaxe incorreta (falta ponto e vírgula)"
        };
        
        for (int i = 0; i < testFiles.length; i++) {
            System.out.println("==========================================");
            System.out.println(descriptions[i]);
            System.out.println("Arquivo: " + testFiles[i]);
            System.out.println("==========================================");
            
            try {
                String content = readFile(testFiles[i]);
                System.out.println("Conteúdo:");
                System.out.println(content);
                System.out.println("\nResultado:");
                compile(content);
            } catch (IOException e) {
                System.err.println("❌ Erro ao ler arquivo: " + e.getMessage());
            }
            
            System.out.println("\n");
        }
    }
    
    private static String readFile(String filename) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filename)));
    }
    
    public static void compile(String input) {
        try {
            StringReader reader = new StringReader(input);
            Lexer lexer = new Lexer(reader);
            parser parser = new parser(lexer);
            
            parser.parse();
            
        } catch (Error e) {
            System.err.println("\n❌ ERRO DE COMPILAÇÃO:");
            System.err.println(e.getMessage());
            System.err.println("Tipo: Erro Léxico");
        } catch (Exception e) {
            System.err.println("\n❌ ERRO DE COMPILAÇÃO:");
            System.err.println(e.getMessage());
            System.err.println("Tipo: Erro Sintático");
        }
    }
}
