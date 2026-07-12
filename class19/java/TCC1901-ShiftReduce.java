// TCC1901-ShiftReduce.java | Aula 19
// Simulador de análise sintática shift-reduce (bottom-up).
// Espelho Java do TCC1901-ShiftReduce.py (mesmas entradas e saídas).
import java.util.*;

public class TCC1901ShiftReduce {

    // Produções (lado esquerdo, lado direito)
    static String[][] producoes = {
        {"E", "E", "+", "E"},
        {"E", "E", "*", "E"},
        {"E", "(", "E", ")"},
        {"E", "id"},
    };

    static boolean reduzir(List<String> pilha) {
        for (String[] prod : producoes) {
            String esq = prod[0];
            int len = prod.length - 1;
            if (pilha.size() >= len) {
                boolean match = true;
                for (int k = 0; k < len; k++) {
                    if (!pilha.get(pilha.size() - len + k).equals(prod[k + 1])) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    for (int k = 0; k < len; k++) pilha.remove(pilha.size() - 1);
                    pilha.add(esq);
                    return true;
                }
            }
        }
        return false;
    }

    static class Resultado {
        boolean aceita;
        List<String> trace;
        Resultado(boolean a, List<String> t) { aceita = a; trace = t; }
    }

    static Resultado shiftReduce(List<String> entrada) {
        List<String> pilha = new ArrayList<>();
        List<String> ent = new ArrayList<>(entrada);
        ent.add("$");
        int i = 0;
        List<String> trace = new ArrayList<>();
        trace.add("Início: pilha=" + pilha + ", entrada=" + ent.subList(i, ent.size()));

        while (true) {
            while (reduzir(pilha)) {
                trace.add("Reduce: pilha=" + pilha + ", entrada=" + ent.subList(i, ent.size()));
            }
            if (pilha.size() == 1 && pilha.get(0).equals("E") && ent.get(i).equals("$")) {
                trace.add("ACEITA");
                return new Resultado(true, trace);
            }
            if (!ent.get(i).equals("$")) {
                pilha.add(ent.get(i));
                i++;
                trace.add("Shift:  pilha=" + pilha + ", entrada=" + ent.subList(i, ent.size()));
            } else {
                trace.add("REJEITA (entrada esgotada sem aceitação)");
                return new Resultado(false, trace);
            }
        }
    }

    static List<String> tokenize(String expr) {
        List<String> tokens = new ArrayList<>();
        int i = 0, n = expr.length();
        while (i < n) {
            char c = expr.charAt(i);
            if (Character.isWhitespace(c)) { i++; continue; }
            if (Character.isDigit(c)) {
                tokens.add("id");
                while (i < n && Character.isDigit(expr.charAt(i))) i++;
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
        String[] testes = {"id + id", "id * id + id", "( id + id ) * id", "id +"};
        for (String expr : testes) {
            List<String> tokens = tokenize(expr);
            Resultado r = shiftReduce(tokens);
            System.out.println((r.aceita ? "ACEITA" : "REJEITA") + ": " + expr);
            if (expr.equals("id + id")) {
                for (String linha : r.trace) {
                    System.out.println("  " + linha);
                }
            }
        }
    }
}
