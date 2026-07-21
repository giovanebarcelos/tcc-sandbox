/* Arquivo: Main.java */
import java.io.*;
import java_cup.runtime.*;

public class Main {
    public static void main(String[] args) {
        try {
            // Exemplo de uso com diferentes expressões
            String[] expressoes = {
                "2 + 3",
                "10 - 5 * 2",
                "(10 - 5) * 2",
                "100 / (2 + 3)",
                "-5 + 10",
                "2 * (3 + 4) - 5"
            };
            
            for (String expr : expressoes) {
                System.out.println("\nExpressão: " + expr);
                calcular(expr);
            }
            
            // Modo interativo
            if (args.length == 0) {
                System.out.println("\n=== Calculadora Simples ===");
                System.out.println("Digite uma expressão (ou 'sair' para encerrar):");
                
                BufferedReader br = new BufferedReader(
                    new InputStreamReader(System.in)
                );
                
                String linha;
                while ((linha = br.readLine()) != null) {
                    if (linha.trim().equalsIgnoreCase("sair")) {
                        break;
                    }
                    if (!linha.trim().isEmpty()) {
                        calcular(linha);
                    }
                    System.out.print("\nDigite outra expressão: ");
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void calcular(String expressao) {
        try {
            Reader reader = new StringReader(expressao);
            Lexer lexer = new Lexer(reader);
            parser parser = new parser(lexer);
            
            Symbol result = parser.parse();
            System.out.println("Resultado: " + result.value);
            
        } catch (Exception e) {
            System.err.println("Erro ao calcular: " + e.getMessage());
        }
    }
}
