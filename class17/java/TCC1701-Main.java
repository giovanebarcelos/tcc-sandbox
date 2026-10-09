// TCC1701-Main.java | Aula 17
// Driver para o scanner JFlex gerado a partir de TCC1701-Scanner.flex.
// Lê um arquivo de entrada e imprime os tokens reconhecidos (TIPO('lexema')@linha:coluna).
// Build: jflex TCC1701-Scanner.flex
//        javac TCC1701Scanner.java TCC1701-Main.java
//        java TCC1701Main entrada.txt
// A classe não é "public" para poder ficar no arquivo TCC1701-Main.java
// (o javac só exige nome de arquivo igual ao da classe quando ela é public).

import java.io.FileReader;
import java_cup.runtime.Symbol;

class TCC1701Main {
    public static void main(String[] args) throws Exception {
        String arquivo = args.length > 0 ? args[0] : "entrada.txt";
        TCC1701Scanner scanner = new TCC1701Scanner(new FileReader(arquivo));
        System.out.println("=== Tokens (JFlex) ===");
        for (Symbol tok = scanner.next_token(); tok.sym != TCC1701Scanner.EOF; tok = scanner.next_token()) {
            System.out.printf("  %s('%s')@%d:%d%n",
                    TCC1701Scanner.NOMES[tok.sym], tok.value, tok.left, tok.right);
        }
    }
}
