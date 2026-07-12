// TCC1001-ArvoreDerivacao.java | Aula 10
// Gera árvore de derivação de uma GLC e detecta ambiguidade.
// Gramática exemplo: E → E+E | E*E | (E) | id
// Espelho Java do TCC1001-ArvoreDerivacao.py (mesmas saídas).
import java.util.ArrayList;
import java.util.List;

public class TCC1001ArvoreDerivacao {

    static class No {
        String simbolo;
        List<No> filhos = new ArrayList<>();

        No(String simbolo) {
            this.simbolo = simbolo;
        }

        No add(String simbolo) {
            No f = new No(simbolo);
            filhos.add(f);
            return f;
        }

        boolean ehFolha() {
            return filhos.isEmpty();
        }
    }

    static int contador = 0;

    static void imprimeArvore(No no, String prefixo, boolean ehUltimo) {
        String connector = ehUltimo ? "└── " : "├── ";
        System.out.println(prefixo + connector + no.simbolo);
        String novoPrefixo = prefixo + (ehUltimo ? "    " : "│   ");
        for (int i = 0; i < no.filhos.size(); i++) {
            imprimeArvore(no.filhos.get(i), novoPrefixo, i == no.filhos.size() - 1);
        }
    }

    static void imprimeArvore(No no) {
        imprimeArvore(no, "", true);
    }

    static String mermaid(No raiz) {
        contador = 0;
        StringBuilder sb = new StringBuilder("flowchart TD\n");
        mermaidWalk(raiz, null, sb);
        return sb.toString();
    }

    static String mermaidWalk(No no, String pid, StringBuilder sb) {
        String nid = "n" + (++contador);
        String rotulo = no.simbolo.isEmpty() ? "ε" : no.simbolo;
        sb.append("    ").append(nid).append("[\"").append(rotulo).append("\"]\n");
        if (pid != null) {
            sb.append("    ").append(pid).append(" --> ").append(nid).append("\n");
        }
        for (No f : no.filhos) {
            mermaidWalk(f, nid, sb);
        }
        return nid;
    }

    static No arvoreIdMaisId() {
        // E ⇒ E+E ⇒ id+E ⇒ id+id
        No raiz = new No("E");
        No e1 = raiz.add("E"); e1.add("id");
        raiz.add("+");
        No e2 = raiz.add("E"); e2.add("id");
        return raiz;
    }

    static No arvoreIdVezesIdMaisIdEsq() {
        // (id*id)+id
        No raiz = new No("E");
        No eEsq = raiz.add("E");
        No eEsqE = eEsq.add("E"); eEsqE.add("id");
        eEsq.add("*");
        No eEsqD = eEsq.add("E"); eEsqD.add("id");
        raiz.add("+");
        No eDir = raiz.add("E"); eDir.add("id");
        return raiz;
    }

    static No arvoreIdVezesIdMaisIdDir() {
        // id*(id+id) — AMBIGUIDADE
        No raiz = new No("E");
        No eEsq = raiz.add("E"); eEsq.add("id");
        raiz.add("*");
        No eDir = raiz.add("E");
        No eDirE = eDir.add("E"); eDirE.add("id");
        eDir.add("+");
        No eDirD = eDir.add("E"); eDirD.add("id");
        return raiz;
    }

    public static void main(String[] args) {
        System.out.println("=== Árvore 1: id+id (leftmost) ===");
        imprimeArvore(arvoreIdMaisId());

        System.out.println("\n=== Árvore 2: id*id+id — agrupamento à esquerda (id*id)+id ===");
        imprimeArvore(arvoreIdVezesIdMaisIdEsq());

        System.out.println("\n=== Árvore 3: id*id+id — agrupamento à direita id*(id+id) ===");
        imprimeArvore(arvoreIdVezesIdMaisIdDir());

        System.out.println("\n>>> AMBIGUIDADE: as árvores 2 e 3 derivam a MESMA palavra 'id*id+id'");
        System.out.println(">>> com estruturas diferentes → gramática ambígua!\n");

        System.out.println("=== Mermaid da Árvore 1 ===");
        System.out.println(mermaid(arvoreIdMaisId()));
    }
}
