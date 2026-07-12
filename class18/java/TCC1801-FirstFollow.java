// TCC1801-FirstFollow.java | Aula 18
// Calcula conjuntos First e Follow de uma GLC.
// Espelho Java do TCC1801-FirstFollow.py (mesma gramática e saídas).
import java.util.*;

public class TCC1801FirstFollow {

    // Gramática: não-terminal → lista de produções
    static Map<String, List<List<String>>> gramatica = new LinkedHashMap<>();
    static Set<String> naoTerminais = new LinkedHashSet<>();
    static Set<String> terminais = new LinkedHashSet<>();

    static {
        gramatica.put("E",  Arrays.asList(Arrays.asList("T", "E'")));
        gramatica.put("E'", Arrays.asList(Arrays.asList("+", "T", "E'"), Arrays.asList("ε")));
        gramatica.put("T",  Arrays.asList(Arrays.asList("F", "T'")));
        gramatica.put("T'", Arrays.asList(Arrays.asList("*", "F", "T'"), Arrays.asList("ε")));
        gramatica.put("F",  Arrays.asList(Arrays.asList("(", "E", ")"), Arrays.asList("id")));

        naoTerminais.addAll(gramatica.keySet());
        for (List<List<String>> prods : gramatica.values()) {
            for (List<String> prod : prods) {
                for (String s : prod) {
                    if (!naoTerminais.contains(s)) terminais.add(s);
                }
            }
        }
        terminais.remove("ε");
    }

    static Map<String, Set<String>> first = new LinkedHashMap<>();
    static Map<String, Set<String>> follow = new LinkedHashMap<>();

    static Set<String> firstSimbolo(String s, Set<String> visitados) {
        if (terminais.contains(s) || s.equals("ε")) return new LinkedHashSet<>(Arrays.asList(s));
        if (first.containsKey(s)) return first.get(s);
        if (visitados.contains(s)) return new LinkedHashSet<>();
        visitados.add(s);
        Set<String> resultado = new LinkedHashSet<>();
        for (List<String> prod : gramatica.get(s)) {
            for (String simb : prod) {
                Set<String> f = firstSimbolo(simb, visitados);
                resultado.addAll(f); resultado.remove("ε");
                if (!f.contains("ε")) break;
            }
            // se todos derivam ε
            boolean todosEpsilon = true;
            for (String simb : prod) {
                Set<String> f = firstSimbolo(simb, visitados);
                if (!f.contains("ε")) { todosEpsilon = false; break; }
            }
            if (todosEpsilon) resultado.add("ε");
        }
        first.put(s, resultado);
        return resultado;
    }

    static Set<String> firstDeSequencia(List<String> seq) {
        Set<String> resultado = new LinkedHashSet<>();
        for (String s : seq) {
            Set<String> f = first.containsKey(s) ? first.get(s) : new LinkedHashSet<>(Arrays.asList(s));
            resultado.addAll(f); resultado.remove("ε");
            if (!f.contains("ε")) break;
        }
        boolean todosEpsilon = true;
        for (String s : seq) {
            Set<String> f = first.containsKey(s) ? first.get(s) : new LinkedHashSet<>(Arrays.asList(s));
            if (!f.contains("ε")) { todosEpsilon = false; break; }
        }
        if (todosEpsilon) resultado.add("ε");
        return resultado;
    }

    static void calcularFirst() {
        for (String nt : naoTerminais) firstSimbolo(nt, new HashSet<>());
    }

    static void calcularFollow() {
        for (String nt : naoTerminais) follow.put(nt, new LinkedHashSet<>());
        follow.get("E").add("$");
        boolean mudou = true;
        while (mudou) {
            mudou = false;
            for (String A : naoTerminais) {
                for (List<String> prod : gramatica.get(A)) {
                    for (int i = 0; i < prod.size(); i++) {
                        String B = prod.get(i);
                        if (!naoTerminais.contains(B)) continue;
                        List<String> resto = prod.subList(i + 1, prod.size());
                        Set<String> fResto = resto.isEmpty() ? new LinkedHashSet<>(Arrays.asList("ε")) : firstDeSequencia(resto);
                        Set<String> novo = new LinkedHashSet<>(follow.get(B));
                        novo.addAll(fResto); novo.remove("ε");
                        if (fResto.contains("ε") || resto.isEmpty()) {
                            novo.addAll(follow.get(A));
                        }
                        if (!novo.equals(follow.get(B))) {
                            follow.put(B, novo);
                            mudou = true;
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        calcularFirst();
        calcularFollow();

        System.out.println("=== FIRST ===");
        for (String nt : naoTerminais) {
            System.out.println("  First(" + nt + ") = " + first.get(nt));
        }

        System.out.println("\n=== FOLLOW ===");
        for (String nt : naoTerminais) {
            System.out.println("  Follow(" + nt + ") = " + follow.get(nt));
        }
    }
}
