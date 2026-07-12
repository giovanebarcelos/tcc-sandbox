// TCC1802-ParserRecursivo.java | Aula 18
// Parser descendente recursivo para expressões.
// Espelho Java do TCC1802-ParserRecursivo.py (mesmas entradas e saídas).
import java.util.*;

public class TCC1802ParserRecursivo {

    static class ParserRecursivo {
        List<String> tokens;
        int pos = 0;

        ParserRecursivo(List<String> tokens) {
            this.tokens = new ArrayList<>(tokens);
            this.tokens.add("$");
        }

        String peek() { return tokens.get(pos); }
        String consume() { return tokens.get(pos++); }

        int parse() { return E(); }

        int E() {
            int val = T();
            return ELinha(val);
        }

        int ELinha(int inherited) {
            if (peek().equals("+")) {
                consume();
                int val = T();
                return ELinha(inherited + val);
            }
            return inherited; // ε
        }

        int T() {
            int val = F();
            return TLinha(val);
        }

        int TLinha(int inherited) {
            if (peek().equals("*")) {
                consume();
                int val = F();
                return TLinha(inherited * val);
            }
            return inherited; // ε
        }

        int F() {
            String t = peek();
            if (t.equals("(")) {
                consume();
                int val = E();
                if (!peek().equals(")"))
                    throw new RuntimeException("Esperado ')', obtido '" + peek() + "'");
                consume();
                return val;
            }
            if (t.matches("\\d+")) {
                consume();
                return Integer.parseInt(t);
            }
            throw new RuntimeException("Token inesperado em F: '" + t + "'");
        }
    }

    static List<String> tokenize(String expr) {
        List<String> tokens = new ArrayList<>();
        int i = 0, n = expr.length();
        while (i < n) {
            char c = expr.charAt(i);
            if (Character.isWhitespace(c)) { i++; continue; }
            if (Character.isDigit(c)) {
                int j = i;
                while (j < n && Character.isDigit(expr.charAt(j))) j++;
                tokens.add(expr.substring(i, j));
                i = j;
            } else if ("+*()".indexOf(c) >= 0) {
                tokens.add(String.valueOf(c));
                i++;
            } else {
                throw new RuntimeException("Caractere inválido: '" + c + "'");
            }
        }
        return tokens;
    }

    public static void main(String[] args) {
        String[] testes = {"3 + 4 * 2", "(3 + 4) * 2", "1 + 2 + 3", "2 * 3 + 4 * 5", "((1 + 2) * 3)"};
        for (String expr : testes) {
            List<String> tokens = tokenize(expr);
            int resultado = new ParserRecursivo(tokens).parse();
            System.out.println(expr + " = " + resultado);
        }
    }
}
