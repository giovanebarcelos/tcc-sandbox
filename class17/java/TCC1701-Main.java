// TCC1701-Main.java | Aula 17
// Driver para o scanner JFlex gerado a partir de TCC1701-Scanner.flex.
// Lê um arquivo de entrada e imprime os tokens reconhecidos.
// Build: java -jar jflex-full-1.9.1.jar TCC1701-Scanner.flex
//        javac TCC1701Scanner.java TCC1701Main.java
//        java TCC1701Main entrada.txt
package tcc;

import java.io.FileReader;

public class TCC1701Main {
    public static void main(String[] args) throws Exception {
        String arquivo = args.length > 0 ? args[0] : "entrada.txt";
        TCC1701Scanner scanner = new TCC1701Scanner(new FileReader(arquivo));
        java_cup.runtime.Symbol tok;
        System.out.println("=== Tokens (JFlex) ===");
        do {
            tok = scanner.next_token();
            System.out.printf("  %s(%s)%n", tok.sym, tok.value);
        } while (tok.sym != 0);
    }
}
