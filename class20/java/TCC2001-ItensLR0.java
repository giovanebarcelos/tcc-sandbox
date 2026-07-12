// TCC2001-ItensLR0.java | Aula 20
// Gera a coleção canônica de itens LR(0) de uma gramática.
// Espelho Java do TCC2001-ItensLR0.py (mesma gramática e saídas).
import java.util.*;

public class TCC2001ItensLR0 {

    // Produções: [esquerda, direita...]
    static String[][] producoes = {
        {"E'", "E"},
        {"E", "E", "+", "T"},
        {"E", "T"},
        {"T", "T", "*", "F"},
        {"T", "F"},
        {"F", "(", "E", ")"},
        {"F", "id"},
    };

    static Set<String> naoTerminais = new LinkedHashSet<>();
    static Set<String> terminais = new LinkedHashSet<>();

    static {
        for (String[] p : producoes) naoTerminais.add(p[0]);
        for (String[] p : producoes) {
            for (int i = 1; i < p.length; i++) {
                if (!naoTerminais.contains(p[i])) terminais.add(p[i]);
            }
        }
        terminais.add("$");
    }

    // Item = int[2]: {numProdução, posiçãoDoPonto}
    static Set<String> fecho(Set<String> itens) {
        Set<String> resultado = new LinkedHashSet<>(itens);
        List<String> fila = new ArrayList<>(itens);
        while (!fila.isEmpty()) {
            String item = fila.remove(0);
            String[] parts = item.split(":");
            int prodIdx = Integer.parseInt(parts[0]);
            int dot = Integer.parseInt(parts[1]);
            String[] prod = producoes[prodIdx];
            if (dot < prod.length - 1) {
                String simb = prod[dot + 1];
                if (naoTerminais.contains(simb)) {
                    for (int i = 0; i < producoes.length; i++) {
                        if (producoes[i][0].equals(simb)) {
                            String novo = i + ":0";
                            if (!resultado.contains(novo)) {
                                resultado.add(novo);
                                fila.add(novo);
                            }
                        }
                    }
                }
            }
        }
        return resultado;
    }

    static Set<String> gotoEstado(Set<String> itens, String simb) {
        Set<String> movidos = new LinkedHashSet<>();
        for (String item : itens) {
            String[] parts = item.split(":");
            int prodIdx = Integer.parseInt(parts[0]);
            int dot = Integer.parseInt(parts[1]);
            String[] prod = producoes[prodIdx];
            if (dot < prod.length - 1 && prod[dot + 1].equals(simb)) {
                movidos.add(prodIdx + ":" + (dot + 1));
            }
        }
        return movidos.isEmpty() ? new LinkedHashSet<>() : fecho(movidos);
    }

    static List<Set<String>> colecaoCanonica() {
        Set<String> inicial = fecho(new LinkedHashSet<>(Arrays.asList("0:0")));
        List<Set<String>> estados = new ArrayList<>();
        estados.add(inicial);
        List<Integer> fila = new ArrayList<>(Arrays.asList(0));
        while (!fila.isEmpty()) {
            int idx = fila.remove(0);
            Set<String> estado = estados.get(idx);
            Set<String> simbolos = new LinkedHashSet<>();
            for (String item : estado) {
                String[] parts = item.split(":");
                int prodIdx = Integer.parseInt(parts[0]);
                int dot = Integer.parseInt(parts[1]);
                String[] prod = producoes[prodIdx];
                if (dot < prod.length - 1) simbolos.add(prod[dot + 1]);
            }
            for (String simb : simbolos) {
                Set<String> novo = gotoEstado(estado, simb);
                if (novo.isEmpty()) continue;
                int existente = -1;
                for (int i = 0; i < estados.size(); i++) {
                    if (estados.get(i).equals(novo)) { existente = i; break; }
                }
                if (existente == -1) {
                    estados.add(novo);
                    fila.add(estados.size() - 1);
                }
            }
        }
        return estados;
    }

    public static void main(String[] args) {
        List<Set<String>> estados = colecaoCanonica();

        System.out.println("=== Coleção Canônica de Itens LR(0) ===\n");
        for (int i = 0; i < estados.size(); i++) {
            System.out.println("I" + i + ":");
            List<String> itensOrdenados = new ArrayList<>(estados.get(i));
            Collections.sort(itensOrdenados);
            for (String item : itensOrdenados) {
                String[] parts = item.split(":");
                int prodIdx = Integer.parseInt(parts[0]);
                int dot = Integer.parseInt(parts[1]);
                String[] prod = producoes[prodIdx];
                StringBuilder sb = new StringBuilder("  " + prod[0] + " → ");
                for (int j = 1; j < prod.length; j++) {
                    if (j - 1 == dot) sb.append("• ");
                    sb.append(prod[j]).append(" ");
                }
                if (dot == prod.length - 1) sb.append("•");
                System.out.println(sb.toString().trim());
            }
            System.out.println();
        }

        System.out.println("=== Follow (simplificado) ===");
        System.out.println("  Follow(E) = [+, $]");
        System.out.println("  Follow(T) = [*, +, $]");
        System.out.println("  Follow(F) = [*, +, ), $]");
    }
}
