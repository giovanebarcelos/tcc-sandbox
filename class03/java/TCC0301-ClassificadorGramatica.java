import java.util.*;

/**
 * TCC0301-ClassificadorGramatica.java | Aula 03
 * Classifica uma gramática formal segundo a Hierarquia de Chomsky (tipos 0-3).
 *
 * Mesma lógica e mesma saída do TCC0301-ClassificadorGramatica.py.
 */
class ClassificadorGramatica {

    static final Map<Integer, String> TIPO_NOME = Map.of(
            3, "Tipo 3 — Regular",
            2, "Tipo 2 — Livre de Contexto",
            1, "Tipo 1 — Sensível ao Contexto",
            0, "Tipo 0 — Irrestrita"
    );

    /**
     * Classifica uma gramática conforme a Hierarquia de Chomsky.
     *
     * @param producoes mapa lhs (String) -> lista de rhs (String)
     * @param V conjunto de variáveis (não-terminais)
     * @return tipo de Chomsky (3, 2, 1 ou 0)
     */
    static int classificar(Map<String, List<String>> producoes, Set<String> V) {
        int tipo = 3;  // começa otimista

        for (var entry : producoes.entrySet()) {
            String lhs = entry.getKey();
            for (String rhs : entry.getValue()) {

                // --- tipo 2+: lado esquerdo deve ser UMA única variável ---
                if (!V.contains(lhs) || lhs.length() != 1) {
                    tipo = Math.min(tipo, 1);
                    // tipo 1: |alpha| <= |beta| ?
                    if (lhs.length() > rhs.length()) {
                        tipo = 0;
                    }
                    continue;
                }

                // --- tipo 3: linear à direita ou à esquerda ---
                long varCount = rhs.chars().filter(c -> V.contains(String.valueOf((char) c))).count();
                if (varCount > 1) {
                    tipo = Math.min(tipo, 2);
                } else if (varCount == 1) {
                    // encontra a posição da variável
                    int varPos = -1;
                    for (int i = 0; i < rhs.length(); i++) {
                        if (V.contains(String.valueOf(rhs.charAt(i)))) {
                            varPos = i;
                            break;
                        }
                    }
                    boolean direita = varPos == rhs.length() - 1;
                    boolean esquerda = varPos == 0;
                    if (!direita && !esquerda) {
                        tipo = Math.min(tipo, 2);
                    }
                    if (esquerda && rhs.length() > 1) {
                        String restante = rhs.substring(1);
                        boolean temVar = restante.chars()
                                .anyMatch(c -> V.contains(String.valueOf((char) c)));
                        if (temVar) {
                            tipo = Math.min(tipo, 2);
                        }
                    }
                }
            }
        }
        return tipo;
    }

    static void testar(String nome, Map<String, List<String>> producoes,
                       Set<String> V, int esperado) {
        int tipo = classificar(producoes, V);
        String status = tipo == esperado ? "✓" : "✗ (esperado " + esperado + ")";
        System.out.println(status + " " + nome + ": " + TIPO_NOME.get(tipo));
    }

    public static void main(String[] args) {
        Set<String> V_bin = Set.of("S", "A");

        // Tipo 3: gramática regular para números binários
        testar("Binários (tipo 3)", Map.of(
                "S", List.of("0S", "1S", "0A", "1A"),
                "A", List.of("0", "1")
        ), Set.of("S", "A"), 3);

        // Tipo 2: GLC para aⁿbⁿ
        testar("aⁿbⁿ (tipo 2)", Map.of(
                "S", List.of("aSb", "")
        ), Set.of("S"), 2);

        // Tipo 2: gramática dos naturais (N -> D | DN)
        testar("Naturais N→D|DN (tipo 2)", Map.of(
                "N", List.of("D", "DN"),
                "D", List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
        ), Set.of("N", "D"), 2);

        // Tipo 2: palíndromos
        testar("Palíndromos (tipo 2)", Map.of(
                "S", List.of("aSa", "bSb", "a", "b", "")
        ), Set.of("S"), 2);

        // Tipo 3: linear à esquerda
        testar("Linear à esquerda (tipo 3)", Map.of(
                "S", List.of("Sa", "Sb", "a", "b")
        ), Set.of("S"), 3);

        // Tipo 0: irrestrita (|alpha| > |beta|)
        testar("Irrestrita (tipo 0)", Map.of(
                "AB", List.of("C")
        ), Set.of("A", "B", "C"), 0);
    }
}
