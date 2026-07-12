// TCC2502-Otimizador.java | Aula 25
// Otimizador didático: constant folding e eliminação de código morto.
// Espelho Java do TCC2502-Otimizador.py (mesmas entradas e saídas).
import java.util.*;

public class TCC2502Otimizador {

    static boolean ehNum(String s) {
        try { Double.parseDouble(s); return true; }
        catch (NumberFormatException e) { return false; }
    }

    static Double aplicarOp(double a, String op, double b) {
        switch (op) {
            case "+": return a + b;
            case "-": return a - b;
            case "*": return a * b;
            case "/": return b != 0 ? a / b : null;
            default: return null;
        }
    }

    static List<String> constantFolding(List<String> tac) {
        Map<String, Double> valores = new HashMap<>();
        List<String> otimizado = new ArrayList<>();
        for (String linha : tac) {
            String[] parts = linha.split(" = ", 2);
            if (parts.length != 2) { otimizado.add(linha); continue; }
            String dst = parts[0], expr = parts[1];
            String[] tokens = expr.split(" ");
            if (tokens.length == 3 && ehNum(tokens[0]) && ehNum(tokens[2])) {
                double a = Double.parseDouble(tokens[0]);
                double b = Double.parseDouble(tokens[2]);
                Double r = aplicarOp(a, tokens[1], b);
                if (r != null) {
                    valores.put(dst, r);
                    otimizado.add(dst + " = " + r + "  (constant folding)");
                    continue;
                }
            }
            for (int i = 0; i < tokens.length; i++) {
                if (valores.containsKey(tokens[i])) {
                    tokens[i] = String.valueOf(valores.get(tokens[i]));
                }
            }
            otimizado.add(dst + " = " + String.join(" ", tokens));
        }
        return otimizado;
    }

    static List<String> deadCodeElimination(List<String> tac) {
        Set<String> usados = new HashSet<>();
        for (String linha : tac) {
            String[] parts = linha.split(" = ", 2);
            if (parts.length == 2) {
                for (String token : parts[1].split(" ")) {
                    if (token.matches("[a-zA-Z_]\\w*") || token.startsWith("t")) {
                        usados.add(token);
                    }
                }
            }
        }
        List<String> otimizado = new ArrayList<>();
        for (String linha : tac) {
            String[] parts = linha.split(" = ", 2);
            if (parts.length == 2) {
                String dst = parts[0];
                if (dst.startsWith("t") && !usados.contains(dst)) continue;
            }
            otimizado.add(linha);
        }
        return otimizado;
    }

    public static void main(String[] args) {
        List<String> tac = Arrays.asList(
            "t1 = 3 + 4",
            "t2 = t1 * 2",
            "t3 = 10 - 5",
            "x = t2 + t3",
            "t4 = 100 * 0"
        );

        System.out.println("=== TAC Original ===");
        for (String l : tac) System.out.println("  " + l);

        System.out.println("\n=== Após Constant Folding ===");
        List<String> cf = constantFolding(tac);
        for (String l : cf) System.out.println("  " + l);

        System.out.println("\n=== Após Dead Code Elimination ===");
        List<String> dce = deadCodeElimination(cf);
        for (String l : dce) System.out.println("  " + l);
    }
}
