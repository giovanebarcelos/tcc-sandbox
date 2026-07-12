import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * TCC0501-TestadorLote.java | Aula 05
 * Testador em lote: lê um arquivo de palavras e testa cada uma em um AFD.
 * Mesma lógica e mesma saída do TCC0501-TestadorLote.py.
 */
class TestadorLote {

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

    static void testarArquivo(String caminho) throws IOException {
        Files.lines(Path.of(caminho))
                .map(String::trim)
                .filter(l -> !l.isEmpty())
                .forEach(linha -> {
                    String resultado = aceita(linha) ? "ACEITA" : "REJEITADA";
                    System.out.println(linha + ": " + resultado);
                });
    }

    public static void main(String[] args) throws IOException {
        if (args.length > 0) {
            testarArquivo(args[0]);
        } else {
            System.out.println("=== Testador em Lote (demo) ===");
            List<String> palavras = List.of(
                    "abba", "aba", "bab", "aabb", "abab",
                    "baa", "bba", "aa", "bb", "ab");
            System.out.printf("%-12s %s%n", "Palavra", "Resultado");
            System.out.println("-".repeat(28));
            for (String w : palavras) {
                System.out.printf("%-12s %s%n", w, aceita(w) ? "ACEITA" : "REJEITADA");
            }
            System.out.println();
            System.out.println("Uso: java TestadorLote <arquivo.txt>");
        }
    }
}
