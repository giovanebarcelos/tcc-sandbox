// TCC0101-SimuladorAFD.java
// Aula 01 - Teoria da Computação e Compiladores
// Simulador de Autômato Finito Determinístico (AFD).
// AFD de exemplo: aceita palavras sobre {0,1} com número PAR de '1's.
// Mesmo exemplo implementado em Python: TCC0101-SimuladorAFD.py
// Compilar/executar: javac TCC0101-SimuladorAFD.java (renomeie para SimuladorAFD.java)
//                    java SimuladorAFD

import java.util.Map;
import java.util.Set;

public class SimuladorAFD {
    private final Set<String> estados;              // conjunto de estados
    private final Set<Character> alfabeto;          // símbolos válidos
    private final Map<String, String> transicoes;   // "estado,simbolo" -> estado
    private final String inicial;                   // estado inicial
    private final Set<String> finais;               // conjunto de estados finais

    public SimuladorAFD(Set<String> estados, Set<Character> alfabeto,
                        Map<String, String> transicoes, String inicial, Set<String> finais) {
        this.estados = estados;
        this.alfabeto = alfabeto;
        this.transicoes = transicoes;
        this.inicial = inicial;
        this.finais = finais;
    }

    public boolean aceita(String palavra) {
        String estado = inicial;
        System.out.println("Palavra: '" + palavra + "'");
        for (char simbolo : palavra.toCharArray()) {
            if (!alfabeto.contains(simbolo)) {
                System.out.println("  símbolo '" + simbolo + "' não pertence ao alfabeto -> REJEITA");
                return false;
            }
            String proximo = transicoes.get(estado + "," + simbolo);
            System.out.println("  δ(" + estado + ", " + simbolo + ") = " + proximo);
            estado = proximo;
        }
        boolean aceita = finais.contains(estado);
        System.out.println("  estado final: " + estado + " -> " + (aceita ? "ACEITA" : "REJEITA"));
        return aceita;
    }

    public static void main(String[] args) {
        // AFD: número par de '1's  (q0 = par [final], q1 = ímpar)
        SimuladorAFD afd = new SimuladorAFD(
                Set.of("q0", "q1"),
                Set.of('0', '1'),
                Map.of(
                        "q0,0", "q0",
                        "q0,1", "q1",
                        "q1,0", "q1",
                        "q1,1", "q0"),
                "q0",
                Set.of("q0"));

        String[] palavras = {"", "0", "1", "11", "1010", "10101"};
        for (String palavra : palavras) {
            afd.aceita(palavra);
            System.out.println();
        }
    }
}
