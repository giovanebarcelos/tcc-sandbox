import java.io.*;

public class Main {
    public static void main(String[] args) {
        // Exemplos de entrada
        String[] exemplos = {
            // Exemplo 1: Correto
            """
            circulo meuCirculo {
                cor: "vermelho";
                posicao: (10, 20);
                raio: 5.5;
            }
            
            retangulo minhaJanela {
                cor: azul;
                posicao: (0, 0);
                largura: 100;
                altura: 50;
            }
            """,
            
            // Exemplo 2: Erro - círculo sem raio
            """
            circulo circuloErrado {
                cor: "verde";
                posicao: (5, 5);
            }
            """,
            
            // Exemplo 3: Erro - retângulo sem altura
            """
            retangulo retanguloIncompleto {
                cor: "azul";
                largura: 50;
            }
            """,
            
            // Exemplo 4: Erro sintático - falta ponto e vírgula
            """
            quadrado meuQuadrado {
                lado: 10
                cor: "amarelo";
            }
            """,
            
            // Exemplo 5: Erro - posição mal formatada
            """
            triangulo trianguloErrado {
                base: 10;
                altura: 15;
                posicao: (10 20);
            }
            """
        };
        
        for (int i = 0; i < exemplos.length; i++) {
            System.out.println("\n" + "=".repeat(60));
            System.out.println("TESTANDO EXEMPLO " + (i + 1));
            System.out.println("=".repeat(60));
            System.out.println("CÓDIGO:");
            System.out.println(exemplos[i]);
            System.out.println("-".repeat(60));
            
            compilar(exemplos[i]);
        }
    }
    
    public static void compilar(String input) {
        try {
            // Criar lexer e parser
            StringReader reader = new StringReader(input);
            Lexer lexer = new Lexer(reader);
            parser parser = new parser(lexer);
            
            // Fazer parsing
            try {
                parser.parse();
                
                // Verificar se houve erros
                if (!parser.getErrors().isEmpty()) {
                    System.err.println("\n*** ERROS ENCONTRADOS ***");
                    for (String erro : parser.getErrors()) {
                        System.err.println("  " + erro);
                    }
                }
            } catch (Exception e) {
                System.err.println("\n*** ERRO DURANTE COMPILAÇÃO ***");
                System.err.println("  " + e.getMessage());
                
                if (!parser.getErrors().isEmpty()) {
                    System.err.println("\nDetalhes dos erros:");
                    for (String erro : parser.getErrors()) {
                        System.err.println("  " + erro);
                    }
                }
            }
            
        } catch (Error e) {
            // Erros léxicos
            System.err.println("\n*** ERRO LÉXICO ***");
            System.err.println("  " + e.getMessage());
        } catch (Exception e) {
            System.err.println("\n*** ERRO INESPERADO ***");
            System.err.println("  " + e.getMessage());
            e.printStackTrace();
        }
    }
}
