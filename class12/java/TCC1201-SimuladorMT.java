// TCC1201-SimuladorMT.java | Aula 12
// Simulador de Máquina de Turing (MT) de uma fita com trace.
// Reconhece aⁿbⁿcⁿ (n>=1) — linguagem sensível ao contexto.
// Espelho Java do TCC1201-SimuladorMT.py (mesmas entradas e saídas).
import java.util.ArrayList;
import java.util.List;

public class TCC1201SimuladorMT {

    static class Resultado {
        boolean aceita;
        List<String> trace;
        Resultado(boolean aceita, List<String> trace) {
            this.aceita = aceita;
            this.trace = trace;
        }
    }

    static Resultado simularMT(String entrada) {
        List<String> fita = new ArrayList<>();
        for (char c : entrada.toCharArray()) {
            fita.add(String.valueOf(c));
        }
        fita.add("B"); // branco à direita
        int cabeca = 0;
        String estado = "q0";
        List<String> trace = new ArrayList<>();
        int maxPassos = 10000;
        int passos = 0;

        trace.add("Início: estado=" + estado + ", fita=" + fitaStr(fita) + ", cabeça=" + cabeca);

        while (passos < maxPassos) {
            passos++;
            if (cabeca >= fita.size()) {
                fita.add("B");
            }
            String simbolo = fita.get(cabeca);

            if (estado.equals("q0") && simbolo.equals("a")) {
                fita.set(cabeca, "X"); estado = "q1"; cabeca++;
                trace.add("δ(q0,a)→(q1,X,R) | fita=" + fitaStr(fita));
            } else if (estado.equals("q0") && simbolo.equals("Y")) {
                estado = "q4"; cabeca++;
                trace.add("δ(q0,Y)→(q4,Y,R) | fita=" + fitaStr(fita));
            } else if (estado.equals("q1") && (simbolo.equals("a") || simbolo.equals("b"))) {
                cabeca++;
                trace.add("δ(q1," + simbolo + ")→(q1," + simbolo + ",R) | fita=" + fitaStr(fita));
            } else if (estado.equals("q1") && simbolo.equals("b")) {
                fita.set(cabeca, "Y"); estado = "q2"; cabeca++;
                trace.add("δ(q1,b)→(q2,Y,R) | fita=" + fitaStr(fita));
            } else if (estado.equals("q2") && (simbolo.equals("b") || simbolo.equals("c"))) {
                cabeca++;
                trace.add("δ(q2," + simbolo + ")→(q2," + simbolo + ",R) | fita=" + fitaStr(fita));
            } else if (estado.equals("q2") && simbolo.equals("c")) {
                fita.set(cabeca, "Z"); estado = "q3"; cabeca++;
                trace.add("δ(q2,c)→(q3,Z,R) | fita=" + fitaStr(fita));
            } else if (estado.equals("q3") && (simbolo.equals("a") || simbolo.equals("b")
                    || simbolo.equals("c") || simbolo.equals("Y") || simbolo.equals("Z"))) {
                cabeca--;
                trace.add("δ(q3," + simbolo + ")→(q3," + simbolo + ",L) | fita=" + fitaStr(fita));
            } else if (estado.equals("q3") && simbolo.equals("X")) {
                estado = "q0"; cabeca++;
                trace.add("δ(q3,X)→(q0,X,R) | fita=" + fitaStr(fita));
            } else if (estado.equals("q4") && (simbolo.equals("Y") || simbolo.equals("Z"))) {
                cabeca++;
                trace.add("δ(q4," + simbolo + ")→(q4," + simbolo + ",R) | fita=" + fitaStr(fita));
            } else if (estado.equals("q4") && simbolo.equals("B")) {
                trace.add("δ(q4,B)→ACEITA | fita=" + fitaStr(fita));
                return new Resultado(true, trace);
            } else {
                trace.add("SEM transição: estado=" + estado + ", símbolo=" + simbolo + " → REJEITA");
                return new Resultado(false, trace);
            }
        }
        trace.add("Limite de passos excedido → REJEITA");
        return new Resultado(false, trace);
    }

    static String fitaStr(List<String> fita) {
        return String.join("", fita);
    }

    public static void main(String[] args) {
        String[] testes = {"abc", "aabbcc", "aaabbbccc", "aabbc", "abcc", "aabbc"};
        System.out.printf("%-14s %s%n", "Palavra", "Resultado");
        System.out.println("-".repeat(30));
        for (String w : testes) {
            Resultado r = simularMT(w);
            System.out.printf("%-14s %s%n", w, r.aceita ? "ACEITA" : "REJEITA");
        }

        System.out.println("\n=== Trace detalhado para 'abc' ===");
        Resultado r = simularMT("abc");
        for (String linha : r.trace) {
            System.out.println(linha);
        }
    }
}
