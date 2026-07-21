import java.io.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Compilador de Autômatos Finitos ===\n");
        
        // Teste 1: Autômato que reconhece palavras terminadas em 'a'
        String test1 = """
            automaton EndWithA {
                states: q0, q1;
                alphabet: a, b;
                initial: q0;
                final: q1;
                transitions: {
                    q0, a -> q1;
                    q0, b -> q0;
                    q1, a -> q1;
                    q1, b -> q0;
                }
            } end
            """;
        
        // Teste 2: Autômato que reconhece número par de 'a's
        String test2 = """
            automaton EvenAs {
                states: par, impar;
                alphabet: a, b;
                initial: par;
                final: par;
                transitions: {
                    par, a -> impar;
                    par, b -> par;
                    impar, a -> par;
                    impar, b -> impar;
                }
            } end
            """;
        
        // Teste 3: ERRO - faltando ponto e vírgula após states
        String test3 = """
            automaton ErrorTest {
                states: q0, q1
                alphabet: a, b;
                initial: q0;
                final: q1;
                transitions: {
                    q0, a -> q1;
                }
            } end
            """;
        
        System.out.println("==========================================");
        System.out.println("TESTE 1: Autômato que aceita palavras terminadas em 'a'");
        System.out.println("==========================================");
        compile(test1);
        
        System.out.println("\n==========================================");
        System.out.println("TESTE 2: Autômato que aceita número par de 'a's");
        System.out.println("==========================================");
        compile(test2);
        
        System.out.println("\n==========================================");
        System.out.println("TESTE 3: ERRO - Sintaxe incorreta (falta ponto e vírgula)");
        System.out.println("==========================================");
        compile(test3);
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
