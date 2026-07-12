import java.util.*;

/**
 * TCC0601-SimuladorAFN.java | Aula 06
 * Simulador de AFN por conjunto de estados ativos.
 * Mesma lógica e mesma saída do TCC0601-SimuladorAFN.py.
 */
class SimuladorAFN {

    record Par(String estado, String simbolo) {}

    static Map<Par, Set<String>> transicoes = Map.of(
            new Par("q0", "a"), Set.of("q0", "q1"),
            new Par("q0", "b"), Set.of("q0", "q2"),
            new Par("q1", "a"), Set.of("qf"),
            new Par("q2", "b"), Set.of("qf"),
            new Par("qf", "a"), Set.of("qf"),
            new Par("qf", "b"), Set.of("qf")
    );

    static String q0 = "q0";
    static Set<String> finais = Set.of("qf");

    static boolean simular(String palavra) {
        Set<String> ativos = new HashSet<>(Set.of(q0));
        for (char c : palavra.toCharArray()) {
            Set<String> novos = new HashSet<>();
            for (String q : ativos) {
                Set<String> destinos = transicoes.get(new Par(q, String.valueOf(c)));
                if (destinos != null) novos.addAll(destinos);
            }
            ativos = novos;
            if (ativos.isEmpty()) return false;
        }
        return !Collections.disjoint(ativos, finais);
    }

    static void simularComTrace(String palavra) {
        Set<String> ativos = new HashSet<>(Set.of(q0));
        System.out.println("Início: {" + String.join(", ", ativos.stream().sorted().toList()) + "}");
        for (char c : palavra.toCharArray()) {
            String simbolo = String.valueOf(c);
            Set<String> novos = new HashSet<>();
            for (String q : ativos) {
                Set<String> destinos = transicoes.get(new Par(q, simbolo));
                if (destinos != null && !destinos.isEmpty()) {
                    System.out.println("  δ(" + q + ", " + simbolo + ") = {" +
                            String.join(", ", destinos.stream().sorted().toList()) + "}");
                    novos.addAll(destinos);
                }
            }
            ativos = novos;
            System.out.println("  Após '" + simbolo + "': {" +
                    String.join(", ", ativos.stream().sorted().toList()) + "}");
            if (ativos.isEmpty()) {
                System.out.println("  → sem estados ativos (REJEITA)");
                return;
            }
        }
        boolean aceita = !Collections.disjoint(ativos, finais);
        System.out.println("Final: {" + String.join(", ", ativos.stream().sorted().toList()) +
                "} → " + (aceita ? "ACEITA" : "REJEITADA"));
    }

    public static void main(String[] args) {
        List<String> palavras = List.of("abba", "aba", "aa", "bb", "ab");
        System.out.printf("%-10s %s%n", "Palavra", "Resultado");
        System.out.println("-".repeat(25));
        for (String w : palavras) {
            System.out.printf("%-10s %s%n", w, simular(w) ? "ACEITA" : "REJEITADA");
        }

        System.out.println("\n=== Trace detalhado: abba ===");
        simularComTrace("abba");
        System.out.println("\n=== Trace detalhado: aba ===");
        simularComTrace("aba");
    }
}
