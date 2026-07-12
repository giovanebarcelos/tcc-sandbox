// TCC1601-ScannerManual.java | Aula 16
// Scanner manual implementado como AFD programado.
// Reconhece: NUM, ID, OP (+, -, *, /, =), PUNCT, KEYWORD (if, else, while, print).
// Espelho Java do TCC1601-ScannerManual.py (mesmas entradas e saídas).
import java.util.*;

public class TCC1601ScannerManual {

    static class Token {
        String tipo, valor;
        int linha, col;
        Token(String tipo, String valor, int linha, int col) {
            this.tipo = tipo; this.valor = valor; this.linha = linha; this.col = col;
        }
        public String toString() { return tipo + "(\"" + valor + "\")@" + linha + ":" + col; }
    }

    static final Set<String> KEYWORDS = new HashSet<>(Arrays.asList(
        "if", "else", "while", "print", "int", "float"
    ));

    static List<Token> scanner(String fonte) {
        List<Token> tokens = new ArrayList<>();
        int i = 0, n = fonte.length();
        int linha = 1, col = 1;

        while (i < n) {
            char c = fonte.charAt(i);

            if (c == '\n') { i++; linha++; col = 1; continue; }
            if (c == ' ' || c == '\t' || c == '\r') { i++; col++; continue; }

            // Comentário # até fim da linha
            if (c == '#') {
                while (i < n && fonte.charAt(i) != '\n') { i++; col++; }
                continue;
            }

            int inicioCol = col;

            // Números (NUM)
            if (Character.isDigit(c)) {
                int j = i;
                while (j < n && Character.isDigit(fonte.charAt(j))) j++;
                if (j < n && fonte.charAt(j) == '.') {
                    j++;
                    while (j < n && Character.isDigit(fonte.charAt(j))) j++;
                }
                tokens.add(new Token("NUM", fonte.substring(i, j), linha, inicioCol));
                col += j - i; i = j; continue;
            }

            // Identificadores e palavras-chave
            if (Character.isLetter(c) || c == '_') {
                int j = i;
                while (j < n && (Character.isLetterOrDigit(fonte.charAt(j)) || fonte.charAt(j) == '_')) j++;
                String lexema = fonte.substring(i, j);
                String tipo = KEYWORDS.contains(lexema) ? "KEYWORD" : "ID";
                tokens.add(new Token(tipo, lexema, linha, inicioCol));
                col += j - i; i = j; continue;
            }

            // Operadores
            if ("+-*/=".indexOf(c) >= 0) {
                if (c == '=' && i + 1 < n && fonte.charAt(i + 1) == '=') {
                    tokens.add(new Token("OP", "==", linha, inicioCol));
                    i += 2; col += 2; continue;
                }
                tokens.add(new Token("OP", String.valueOf(c), linha, inicioCol));
                i++; col++; continue;
            }

            // Pontuação
            if ("(){};".indexOf(c) >= 0) {
                tokens.add(new Token("PUNCT", String.valueOf(c), linha, inicioCol));
                i++; col++; continue;
            }

            throw new RuntimeException("Caractere inválido: '" + c + "' na linha " + linha + ", coluna " + col);
        }
        tokens.add(new Token("EOF", "", linha, col));
        return tokens;
    }

    public static void main(String[] args) {
        String fonte = "int x = 42\nif x == 42\n    print(x)\nelse\n    x = x + 1";
        System.out.println("=== Fonte ===");
        System.out.println(fonte);
        System.out.println("\n=== Tokens ===");
        for (Token t : scanner(fonte)) {
            System.out.println("  " + t);
        }
    }
}
