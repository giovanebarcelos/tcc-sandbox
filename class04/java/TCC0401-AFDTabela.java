import java.util.*;

/**
 * TCC0401-AFDTabela.java | Aula 04
 * Simulador de AFD por tabela de transição.
 * AFD: palavras sobre {a,b} que contêm aa ou bb como subpalavra.
 *
 * Mesma lógica e mesma saída do TCC0401-AFDTabela.py.
 */
class AFDTabela {

    static Map<String, Map<String, String>> transicoes = Map.of(
            "q0", Map.of("a", "q1", "b", "q2"),
            "q1", Map.of("a", "qf", "b", "q2"),
            "q2", Map.of("a", "q1", "b", "qf"),
            "qf", Map.of("a", "qf", "b", "qf")
    );

    static String estadoInicial = "q0";
    static Set<String> estadosFinais = Set.of("qf");

    static boolean aceita(String palavra) {
        String estado = estadoInicial;
        for (char c : palavra.toCharArray()) {
            Map<String, String> trans = transicoes.get(estado);
            if (trans == null) return false;
            estado = trans.get(String.valueOf(c));
            if (estado == null) return false;
        }
        return estadosFinais.contains(estado);
    }

    static void testar(List<String> palavras) {
        System.out.printf("%-12s %-10s %s%n", "Palavra", "Resultado", "Estados");
        System.out.println("-".repeat(55));
        for (String w : palavras) {
            List<String> estados = new ArrayList<>();
            String estado = estadoInicial;
            estados.add(estado);
            for (char s : w.toCharArray()) {
                Map<String, String> trans = transicoes.get(estado);
                if (trans == null) break;
                estado = trans.get(String.valueOf(s));
                if (estado == null) break;
                estados.add(estado);
            }
            String resultado = aceita(w) ? "ACEITA" : "REJEITADA";
            String trace = String.join(" → ", estados);
            System.out.printf("%-12s %-10s %s%n", w, resultado, trace);
        }
    }

    public static void main(String[] args) {
        List<String> palavras = List.of(
                "abba", "aba", "bab", "aabb", "abab",
                "baa", "bba", "aa", "bb", "ab");
        testar(palavras);
    }
}
