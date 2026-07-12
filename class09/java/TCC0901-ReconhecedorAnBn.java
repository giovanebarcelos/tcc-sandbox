// TCC0901-ReconhecedorAnBn.java | Aula 09
// Reconhece aⁿbⁿ usando contador — mostra que NÃO é autômato finito.
// Espelho Java do TCC0901-ReconhecedorAnBn.py (mesmas entradas e saídas).
public class TCC0901ReconhecedorAnBn {

    public static boolean reconheceAnbn(String palavra) {
        int i = 0;
        int n = palavra.length();
        while (i < n && palavra.charAt(i) == 'a') {
            i++;
        }
        int nA = i;
        while (i < n && palavra.charAt(i) == 'b') {
            i++;
        }
        int nB = i - nA;
        return i == n && nA == nB && nA > 0;
    }

    public static void derivarGlc(int n) {
        // Deriva aⁿbⁿ usando GLC: S → aSb | ε
        System.out.print("S");
        for (int k = 0; k < n; k++) {
            System.out.print(" ⇒ aSb");
        }
        System.out.println(n > 0 ? " ⇒ aⁿbⁿ" : " ⇒ ε");
    }

    public static void main(String[] args) {
        String[] testes = {"ab", "aabb", "aaabbb", "aab", "abb", "aba"};
        System.out.printf("%-12s %s%n", "Palavra", "Resultado");
        for (String w : testes) {
            System.out.printf("%-12s %s%n", w, reconheceAnbn(w) ? "ACEITA" : "REJEITADA");
        }
        System.out.println("\nDerivação GLC para a³b³:");
        derivarGlc(3);
    }
}
