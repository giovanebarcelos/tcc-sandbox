// TCC1301-ParadaExperimento.java | Aula 13
// Experimento didático do Problema da Parada.
// Demonstra por contradição que não existe um decisor universal `vaiParar(prog, entrada)`.
// Espelho Java do TCC1301-ParadaExperimento.py (mesmas saídas).
public class TCC1301ParadaExperimento {

    interface Programa {
        void executar(String entrada);
    }

    static boolean paraEmAte(Programa prog, String entrada, long maxPassos) {
        // Simula prog(entrada) numa thread com timeout (maxPassos ms).
        final boolean[] parou = {false};
        Thread t = new Thread(() -> {
            try {
                prog.executar(entrada);
                parou[0] = true;
            } catch (Exception e) {
                parou[0] = true; // parou (com erro)
            }
        });
        t.setDaemon(true);
        t.start();
        try {
            t.join(maxPassos);
        } catch (InterruptedException e) {
            // ignore
        }
        return !t.isAlive(); // se ainda vivo, não parou
    }

    static Programa loopInfinito = entrada -> {
        while (true) { }
    };

    static Programa programaOk = entrada -> {
        System.out.println("  programaOk executou com entrada='" + entrada + "' e parou.");
    };

    static void demonstrarParadoxo() {
        System.out.println("=== Argumento do Problema da Parada (Turing, 1936) ===\n");
        System.out.println("Suponha que exista um decisor universal H(prog, entrada):");
        System.out.println("  H(P, x) = True  se P(x) para");
        System.out.println("  H(P, x) = False se P(x) loopa\n");
        System.out.println("Construa o programa paradoxal D(x):");
        System.out.println("  se H(x, x) == True:  loop infinito");
        System.out.println("  senão:               para\n");
        System.out.println("Agora pergunte: H(D, D) = ?");
        System.out.println("  Se H(D,D)=True  → D(D) loopa  → H(D,D) deveria ser False. Contradição!");
        System.out.println("  Se H(D,D)=False → D(D) para    → H(D,D) deveria ser True.  Contradição!");
        System.out.println("\n∴ Não existe decisor universal H. O problema da parada é INDECIDÍVEL.\n");
    }

    public static void main(String[] args) {
        System.out.println("=== Teste: programaOk (sempre para) ===");
        boolean ok = paraEmAte(programaOk, "teste", 50);
        System.out.println("  Parou? " + ok + "\n");

        System.out.println("=== Teste: loopInfinito (nunca para) ===");
        ok = paraEmAte(loopInfinito, "x", 50);
        System.out.println("  Parou? " + ok + " (esperado: false — loop detectado por timeout)\n");

        demonstrarParadoxo();

        System.out.println("=== Classes de complexidade (noção) ===");
        System.out.println("  P  : problemas resolúveis em tempo polinomial (decidíveis)");
        System.out.println("  NP : soluções verificáveis em tempo polinomial");
        System.out.println("  Decidível : existe MT que sempre para com SIM ou NÃO");
        System.out.println("  Indecidível : não existe tal MT (ex.: problema da parada)");
    }
}
