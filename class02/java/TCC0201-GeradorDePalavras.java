import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TCC0201-GeradorDePalavras.java | Aula 02
 * Gerador de palavras para a gramática dos números naturais:
 *   (1) N -> D
 *   (2) N -> DN
 *   (3) D -> 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9
 *
 * Mesma lógica e mesma saída do TCC0201-GeradorDePalavras.py.
 */
public class GeradorDePalavras {

    static Map<String, List<List<String>>> producoes = new LinkedHashMap<>();
    static {
        producoes.put("N", List.of(List.of("D"), List.of("D", "N")));
        List<List<String>> digitos = new ArrayList<>();
        for (char c = '0'; c <= '9'; c++) {
            digitos.add(List.of(String.valueOf(c)));
        }
        producoes.put("D", digitos);
    }

    record Passo(String variavel, int regra) {}

    static String deriva(List<String> formaInicial, List<Passo> passos) {
        List<String> forma = new ArrayList<>(formaInicial);
        System.out.println(String.join("", forma));
        for (Passo p : passos) {
            int i = forma.indexOf(p.variavel());
            List<String> substituicao = producoes.get(p.variavel()).get(p.regra());
            forma.remove(i);
            forma.addAll(i, substituicao);
            System.out.println("=> " + String.join("", forma));
        }
        return String.join("", forma);
    }

    static List<Passo> gerarParaNumero(String numero) {
        List<Passo> passos = new ArrayList<>();
        for (int i = 0; i < numero.length(); i++) {
            boolean ultimo = (i == numero.length() - 1);
            passos.add(new Passo("N", ultimo ? 0 : 1));
            passos.add(new Passo("D", numero.charAt(i) - '0'));
        }
        return passos;
    }

    public static void main(String[] args) {
        System.out.println("=== Derivação de 243 (passos manuais) ===");
        String resultado = deriva(List.of("N"), List.of(
                new Passo("N", 1), new Passo("D", 2),
                new Passo("N", 1), new Passo("D", 4),
                new Passo("N", 0), new Passo("D", 3)));
        assert resultado.equals("243");

        System.out.println();
        System.out.println("=== Derivação automática de 77 ===");
        String resultado77 = deriva(List.of("N"), gerarParaNumero("77"));
        assert resultado77.equals("77");

        System.out.println();
        System.out.println("=== Derivação automática de 5890 ===");
        String resultado5890 = deriva(List.of("N"), gerarParaNumero("5890"));
        assert resultado5890.equals("5890");
    }
}
