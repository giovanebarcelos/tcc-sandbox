// TCC1101-SimuladorAP.java | Aula 11
// Simulador de Autômato de Pilha (AP) com trace da pilha.
// Reconhece aⁿbⁿ (n>=1) por estado final.
// Espelho Java do TCC1101-SimuladorAP.py (mesmas entradas e saídas).
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class TCC1101SimuladorAP {

    static class Resultado {
        boolean aceita;
        List<String> trace;
        Resultado(boolean aceita, List<String> trace) {
            this.aceita = aceita;
            this.trace = trace;
        }
    }

    static Resultado simularAP(String palavra) {
        List<String> trace = new ArrayList<>();
        Stack<String> pilha = new Stack<>();
        pilha.push("Z"); // fundo da pilha
        String estado = "q0";
        int i = 0;
        trace.add("Início: estado=" + estado + ", pilha=" + pilha + ", entrada='" + palavra + "'");

        while (i < palavra.length()) {
            char c = palavra.charAt(i);
            String topo = pilha.isEmpty() ? "ε" : pilha.peek();

            if (estado.equals("q0") && c == 'a' && topo.equals("Z")) {
                pilha.push("A");
                trace.add("δ(q0,a,Z)→(q0,AZ) | lê '" + c + "' | pilha=" + pilha);
                i++;
            } else if (estado.equals("q0") && c == 'a' && topo.equals("A")) {
                pilha.push("A");
                trace.add("δ(q0,a,A)→(q0,AA) | lê '" + c + "' | pilha=" + pilha);
                i++;
            } else if (estado.equals("q0") && c == 'b' && topo.equals("A")) {
                estado = "q1";
                trace.add("δ(q0,b,A)→(q1,A) | lê '" + c + "' | pilha=" + pilha);
                i++;
            } else if (estado.equals("q1") && c == 'b' && topo.equals("A")) {
                pilha.pop();
                trace.add("δ(q1,b,A)→(q1,ε) | lê '" + c + "' | pilha=" + pilha);
                i++;
            } else {
                trace.add("SEM transição: estado=" + estado + ", c='" + c + "', topo=" + topo + " → REJEITA");
                return new Resultado(false, trace);
            }
        }

        // Transição ε para q2
        String topo = pilha.isEmpty() ? "ε" : pilha.peek();
        if (estado.equals("q1") && topo.equals("Z")) {
            estado = "q2";
            trace.add("δ(q1,ε,Z)→(q2,Z) | lê ε | pilha=" + pilha);
        }

        boolean aceita = estado.equals("q2");
        trace.add("Fim: estado=" + estado + " → " + (aceita ? "ACEITA" : "REJEITA"));
        return new Resultado(aceita, trace);
    }

    public static void main(String[] args) {
        String[] testes = {"ab", "aabb", "aaabbb", "aab", "abb", "ba", ""};
        System.out.printf("%-12s %s%n", "Palavra", "Resultado");
        System.out.println("-".repeat(30));
        for (String w : testes) {
            Resultado r = simularAP(w);
            System.out.printf("%-12s %s%n", w.isEmpty() ? "ε" : w, r.aceita ? "ACEITA" : "REJEITA");
        }

        System.out.println("\n=== Trace detalhado para 'aabb' ===");
        Resultado r = simularAP("aabb");
        for (String linha : r.trace) {
            System.out.println(linha);
        }
    }
}
