import java.util.*;

/**
 * TCC0701-Determinizacao.java | Aula 07
 * Algoritmo de construção de subconjuntos: AFN → AFD.
 * Mesma lógica do TCC0701-Determinizacao.py.
 */
class Determinizacao {

    record Par(String estado, String simbolo) {}

    static Map<Par, Set<String>> afn = Map.of(
            new Par("q0", "a"), Set.of("q0", "q1"),
            new Par("q0", "b"), Set.of("q0"),
            new Par("q1", "b"), Set.of("q2"),
            new Par("q2", "b"), Set.of("q3")
    );
    static String q0afn = "q0";
    static Set<String> finaisAfn = Set.of("q3");
    static Set<String> alfabeto = Set.of("a", "b");

    record AfdResult(Map<Set<String>, Map<String, Set<String>>> transicoes,
                     Set<String> q0, Set<Set<String>> finais, Set<Set<String>> estados) {}

    static AfdResult determinizar(Map<Par, Set<String>> afnTransicoes,
                                   String q0, Set<String> finais, Set<String> alfabeto) {
        Map<Set<String>, Map<String, Set<String>>> afdTrans = new LinkedHashMap<>();
        Set<Set<String>> afdFinais = new HashSet<>();
        Set<String> estadoInicial = Set.of(q0);
        Queue<Set<String>> fila = new LinkedList<>();
        Set<Set<String>> visitados = new LinkedHashSet<>();

        fila.add(estadoInicial);
        visitados.add(estadoInicial);

        while (!fila.isEmpty()) {
            Set<String> C = fila.poll();
            boolean ehFinal = C.stream().anyMatch(finais::contains);
            if (ehFinal) afdFinais.add(C);

            Map<String, Set<String>> transDeC = new LinkedHashMap<>();
            for (String a : alfabeto) {
                Set<String> D = new HashSet<>();
                for (String q : C) {
                    Set<String> dest = afnTransicoes.get(new Par(q, a));
                    if (dest != null) D.addAll(dest);
                }
                if (!D.isEmpty()) {
                    transDeC.put(a, D);
                    if (!visitados.contains(D)) {
                        visitados.add(D);
                        fila.add(D);
                    }
                }
            }
            afdTrans.put(C, transDeC);
        }
        return new AfdResult(afdTrans, estadoInicial, afdFinais, visitados);
    }

    public static void main(String[] args) {
        AfdResult r = determinizar(afn, q0afn, finaisAfn, alfabeto);
        System.out.println("AFD: " + r.estados().size() + " estados, " + r.finais().size() + " finais");
        for (Set<String> C : r.estados()) {
            String mark = r.finais().contains(C) ? " (final)" : "";
            System.out.println("  " + C + mark);
        }
        System.out.println("\nTransições:");
        for (var entry : r.transicoes().entrySet()) {
            for (var t : entry.getValue().entrySet()) {
                System.out.println("  δ(" + entry.getKey() + ", " + t.getKey() + ") = " + t.getValue());
            }
        }
    }
}
