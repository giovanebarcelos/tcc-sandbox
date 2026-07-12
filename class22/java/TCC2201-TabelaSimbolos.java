// TCC2201-TabelaSimbolos.java | Aula 22
// Tabela de símbolos reutilizável para o compilador.
// Espelho Java do TCC2201-TabelaSimbolos.py (mesmas operações e saídas).
import java.util.*;

public class TCC2201TabelaSimbolos {

    static class Simbolo {
        String nome, tipo;
        Object valor;
        Simbolo(String nome, String tipo, Object valor) {
            this.nome = nome; this.tipo = tipo; this.valor = valor;
        }
        public String toString() { return nome + ":" + tipo + "=" + valor; }
    }

    static class TabelaSimbolos {
        Map<String, Simbolo> simbolos = new LinkedHashMap<>();

        Simbolo declarar(String nome, String tipo, Object valor) {
            if (simbolos.containsKey(nome))
                throw new RuntimeException("Variável '" + nome + "' já declarada");
            Simbolo sim = new Simbolo(nome, tipo, valor);
            simbolos.put(nome, sim);
            return sim;
        }

        Simbolo buscar(String nome) { return simbolos.get(nome); }

        Simbolo atribuir(String nome, Object valor) {
            Simbolo sim = simbolos.get(nome);
            if (sim == null)
                throw new RuntimeException("Variável '" + nome + "' não declarada");
            sim.valor = valor;
            return sim;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder("TabelaSimbolos(");
            boolean first = true;
            for (Simbolo s : simbolos.values()) {
                if (!first) sb.append(", ");
                sb.append(s);
                first = false;
            }
            return sb.append(")").toString();
        }
    }

    public static void main(String[] args) {
        TabelaSimbolos ts = new TabelaSimbolos();
        ts.declarar("x", "int", 42);
        ts.declarar("y", "float", 3.14);
        ts.declarar("nome", "string", "Giovane");

        System.out.println("=== Tabela após declarações ===");
        System.out.println(ts);

        System.out.println("\n=== Atribuições ===");
        ts.atribuir("x", 100);
        ts.atribuir("y", 2.71);
        System.out.println(ts);

        System.out.println("\n=== Buscas ===");
        System.out.println("  x = " + ts.buscar("x"));
        System.out.println("  y = " + ts.buscar("y"));

        System.out.println("\n=== Erros ===");
        System.out.println("  z = " + ts.buscar("z") + " (null — não encontrado)");
        try {
            ts.declarar("x", "int", 0);
        } catch (RuntimeException e) {
            System.out.println("  " + e.getMessage());
        }
    }
}
