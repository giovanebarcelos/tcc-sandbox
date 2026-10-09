// TCC2001-ItensLR0.java | Aula 20
// Gera a coleção canônica de itens LR(0), calcula FIRST/FOLLOW, monta a tabela
// SLR (ACTION + GOTO) e simula a análise de uma entrada.
// Espelho Java do TCC2001-ItensLR0.py (mesma gramática e saídas).
// Executar: java TCC2001-ItensLR0.java
//      (ou: javac TCC2001-ItensLR0.java && java TCC2001ItensLR0)
import java.util.*;

class TCC2001ItensLR0 {

    // Produções numeradas pelo índice: {esquerda, direita...}
    static String[][] producoes = {
        {"E'", "E"},
        {"E", "E", "+", "T"},
        {"E", "T"},
        {"T", "T", "*", "F"},
        {"T", "F"},
        {"F", "(", "E", ")"},
        {"F", "id"},
    };

    // LinkedHashSet: ordem de declaração/aparição, para a saída sair sempre igual
    static Set<String> naoTerminais = new LinkedHashSet<>();
    static List<String> terminais = new ArrayList<>();

    static {
        for (String[] p : producoes) naoTerminais.add(p[0]);
        for (String[] p : producoes)
            for (int i = 1; i < p.length; i++)
                if (!naoTerminais.contains(p[i]) && !terminais.contains(p[i])) terminais.add(p[i]);
        terminais.add("$");
    }

    static int tam(int p) { return producoes[p].length - 1; }              // |direita|
    static String simb(int p, int pos) { return producoes[p][pos + 1]; }  // símbolo na posição

    // Item = {produção, ponto}; record dá equals/hashCode e ordenação simples
    record Item(int prod, int dot) implements Comparable<Item> {
        public int compareTo(Item o) { return prod != o.prod ? prod - o.prod : dot - o.dot; }
    }

    /** Closure: para cada item A → α • B β, acrescenta B → • γ. */
    static Set<Item> fecho(Set<Item> itens) {
        Set<Item> resultado = new TreeSet<>(itens);
        Deque<Item> fila = new ArrayDeque<>(itens);
        while (!fila.isEmpty()) {
            Item it = fila.poll();
            if (it.dot() < tam(it.prod()) && naoTerminais.contains(simb(it.prod(), it.dot()))) {
                for (int i = 0; i < producoes.length; i++) {
                    Item novo = new Item(i, 0);
                    if (producoes[i][0].equals(simb(it.prod(), it.dot())) && resultado.add(novo)) fila.add(novo);
                }
            }
        }
        return resultado;
    }

    /** Goto(I, X) = fecho dos itens de I com o ponto avançado sobre X. */
    static Set<Item> gotoEstado(Set<Item> estado, String x) {
        Set<Item> movidos = new TreeSet<>();
        for (Item it : estado)
            if (it.dot() < tam(it.prod()) && simb(it.prod(), it.dot()).equals(x))
                movidos.add(new Item(it.prod(), it.dot() + 1));
        return movidos.isEmpty() ? movidos : fecho(movidos);
    }

    static List<Set<Item>> estados = new ArrayList<>();
    static Map<String, Integer> transicoes = new LinkedHashMap<>();   // chave "i,X"

    /** Gera os estados I0, I1, ... (BFS) e as transições do autômato LR(0). */
    static void colecaoCanonica() {
        estados.add(fecho(new TreeSet<>(List.of(new Item(0, 0)))));
        Deque<Integer> fila = new ArrayDeque<>(List.of(0));
        while (!fila.isEmpty()) {
            int idx = fila.poll();
            // símbolos após o ponto, na ordem dos itens (numeração igual à do livro do Dragão)
            Set<String> simbolos = new LinkedHashSet<>();
            for (Item it : estados.get(idx))
                if (it.dot() < tam(it.prod())) simbolos.add(simb(it.prod(), it.dot()));
            for (String x : simbolos) {
                Set<Item> novo = gotoEstado(estados.get(idx), x);
                int j = estados.indexOf(novo);
                if (j == -1) {
                    estados.add(novo);
                    j = estados.size() - 1;
                    fila.add(j);
                }
                transicoes.put(idx + "," + x, j);
            }
        }
    }

    /** FIRST de um símbolo (a gramática não tem produções ε). */
    static Set<String> first(String x) {
        Set<String> resultado = new HashSet<>();
        if (!naoTerminais.contains(x)) { resultado.add(x); return resultado; }
        for (String[] p : producoes)
            if (p[0].equals(x) && !p[1].equals(x))       // ignora recursão à esquerda (E → E + T)
                resultado.addAll(first(p[1]));
        return resultado;
    }

    /** FOLLOW por ponto fixo: repete até nenhum conjunto mudar. */
    static Map<String, Set<String>> follow() {
        Map<String, Set<String>> fol = new LinkedHashMap<>();
        for (String nt : naoTerminais) fol.put(nt, new HashSet<>());
        fol.get(producoes[0][0]).add("$");
        boolean mudou = true;
        while (mudou) {
            mudou = false;
            for (String[] p : producoes) {
                for (int j = 1; j < p.length; j++) {
                    if (!naoTerminais.contains(p[j])) continue;
                    Set<String> novos = j + 1 < p.length ? first(p[j + 1]) : fol.get(p[0]);
                    if (fol.get(p[j]).addAll(novos)) mudou = true;
                }
            }
        }
        return fol;
    }

    static Map<String, String> action = new HashMap<>();     // chave "i,a" -> s5 / r2 / acc
    static Map<String, Integer> gotoTab = new HashMap<>();   // chave "i,A" -> j
    static List<String> conflitos = new ArrayList<>();

    static void define(int i, String a, String acao) {
        String atual = action.get(i + "," + a);
        if (atual != null && !atual.equals(acao)) conflitos.add("I" + i + ", " + a + ": " + atual + " / " + acao);
        action.put(i + "," + a, acao);
    }

    /** Regras SLR: shift (terminal após o ponto), reduce (item completo, para FOLLOW(A)), accept. */
    static void tabelaSlr(Map<String, Set<String>> fol) {
        for (var t : transicoes.entrySet()) {
            String[] k = t.getKey().split(",");
            if (naoTerminais.contains(k[1])) gotoTab.put(t.getKey(), t.getValue());
            else define(Integer.parseInt(k[0]), k[1], "s" + t.getValue());
        }
        for (int i = 0; i < estados.size(); i++) {
            for (Item it : estados.get(i)) {
                if (it.dot() != tam(it.prod())) continue;
                if (it.prod() == 0) define(i, "$", "acc");
                else for (String a : terminais)
                    if (fol.get(producoes[it.prod()][0]).contains(a)) define(i, a, "r" + it.prod());
            }
        }
    }

    /** Analisador LR dirigido pela tabela: pilha de estados. */
    static boolean simular(List<String> tokens) {
        Deque<Integer> pilha = new ArrayDeque<>(List.of(0));   // topo = último (usada como pilha via addLast/removeLast)
        List<String> entrada = new ArrayList<>(tokens);
        entrada.add("$");
        System.out.printf("  %-22s%-22s%s%n", "pilha", "entrada", "ação");
        while (true) {
            String acao = action.get(pilha.peekLast() + "," + entrada.get(0));
            StringJoiner pj = new StringJoiner(" ");
            for (int s : pilha) pj.add(String.valueOf(s));
            System.out.printf("  %-22s%-22s%s%n", pj, String.join(" ", entrada), acao == null ? "ERRO" : acao);
            if (acao == null) return false;
            if (acao.equals("acc")) return true;
            int n = Integer.parseInt(acao.substring(1));
            if (acao.charAt(0) == 's') {
                pilha.addLast(n);
                entrada.remove(0);
            } else {
                for (int k = 0; k < tam(n); k++) pilha.removeLast();
                pilha.addLast(gotoTab.get(pilha.peekLast() + "," + producoes[n][0]));
            }
        }
    }

    static String fmt(Set<String> conjunto) {
        StringJoiner sj = new StringJoiner(", ", "{", "}");
        for (String t : terminais) if (conjunto.contains(t)) sj.add(t);
        return sj.toString();
    }

    public static void main(String[] args) {
        colecaoCanonica();

        System.out.println("=== Coleção Canônica de Itens LR(0) ===\n");
        for (int i = 0; i < estados.size(); i++) {
            System.out.println("I" + i + ":");
            for (Item it : estados.get(i)) {
                List<String> dir = new ArrayList<>(Arrays.asList(producoes[it.prod()]).subList(1, producoes[it.prod()].length));
                dir.add(it.dot(), "•");
                System.out.println("  " + producoes[it.prod()][0] + " → " + String.join(" ", dir));
            }
            System.out.println();
        }

        Map<String, Set<String>> fol = follow();
        System.out.println("=== FIRST e FOLLOW ===");
        for (String nt : naoTerminais)
            System.out.println("  FIRST(" + nt + ") = " + fmt(first(nt)) + "   FOLLOW(" + nt + ") = " + fmt(fol.get(nt)));

        tabelaSlr(fol);
        List<String> nts = new ArrayList<>(naoTerminais).subList(1, naoTerminais.size());
        System.out.println("\n=== Tabela SLR (ACTION | GOTO) ===");
        StringBuilder cab = new StringBuilder("  est | ");
        for (String t : terminais) cab.append(String.format("%5s", t));
        cab.append(" | ");
        for (String nt : nts) cab.append(String.format("%4s", nt));
        System.out.println(cab);
        for (int i = 0; i < estados.size(); i++) {
            StringBuilder linha = new StringBuilder(String.format("  %3d | ", i));
            for (String t : terminais) linha.append(String.format("%5s", action.getOrDefault(i + "," + t, "")));
            linha.append(" | ");
            for (String nt : nts) {
                Integer j = gotoTab.get(i + "," + nt);
                linha.append(String.format("%4s", j == null ? "" : j));
            }
            System.out.println(linha);
        }
        System.out.println("  Conflitos: " + (conflitos.isEmpty() ? "nenhum (a gramática é SLR)" : String.join(", ", conflitos)));

        String entrada = "id + id * id";
        System.out.println("\n=== Simulação: " + entrada + " ===");
        boolean ok = simular(Arrays.asList(entrada.split(" ")));
        System.out.println("  Resultado: " + (ok ? "ACEITA" : "REJEITA"));
    }
}
