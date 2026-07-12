// TCC2501-GeradorCodigo.java | Aula 25
// Gera código de três endereços (TAC) e código C a partir de uma AST.
// Espelho Java do TCC2501-GeradorCodigo.py (mesma AST e saídas).
import java.util.*;

public class TCC2501GeradorCodigo {

    // ---------- AST ----------
    static abstract class No {}
    static class Num extends No { double valor; Num(double v) { valor = v; } }
    static class Var extends No { String nome; Var(String n) { nome = n; } }
    static class BinOp extends No { String op; No esq, dir; BinOp(String o, No e, No d) { op = o; esq = e; dir = d; } }
    static class Assign extends No { String nome; No expr; Assign(String n, No e) { nome = n; expr = e; } }
    static class If extends No { No cond; List<No> thenBody, elseBody; If(No c, List<No> t, List<No> e) { cond = c; thenBody = t; elseBody = e; } }

    // ---------- Gerador TAC ----------
    static class GeradorTAC {
        List<String> codigo = new ArrayList<>();
        int tempCount = 0, labelCount = 0;

        String novoTemp() { return "t" + (++tempCount); }
        String novaLabel() { return "L" + (++labelCount); }

        String gerar(No no) {
            if (no instanceof Num) return String.valueOf(((Num) no).valor);
            if (no instanceof Var) return ((Var) no).nome;
            if (no instanceof BinOp) {
                BinOp b = (BinOp) no;
                String esq = gerar(b.esq), dir = gerar(b.dir);
                String t = novoTemp();
                codigo.add(t + " = " + esq + " " + b.op + " " + dir);
                return t;
            }
            if (no instanceof Assign) {
                Assign a = (Assign) no;
                String val = gerar(a.expr);
                codigo.add(a.nome + " = " + val);
                return a.nome;
            }
            if (no instanceof If) {
                If i = (If) no;
                String cond = gerar(i.cond);
                String lElse = novaLabel(), lFim = novaLabel();
                codigo.add("if " + cond + " == 0 goto " + lElse);
                for (No s : i.thenBody) gerar(s);
                codigo.add("goto " + lFim);
                codigo.add(lElse + ":");
                for (No s : i.elseBody) gerar(s);
                codigo.add(lFim + ":");
                return "";
            }
            throw new RuntimeException("Nó não suportado: " + no.getClass().getSimpleName());
        }
    }

    // ---------- Gerador C ----------
    static class GeradorC {
        List<String> linhas = new ArrayList<>();
        int indent = 0;
        String ind() { return "    ".repeat(indent); }

        String gerar(No no) {
            if (no instanceof Num) return String.valueOf(((Num) no).valor);
            if (no instanceof Var) return ((Var) no).nome;
            if (no instanceof BinOp) {
                BinOp b = (BinOp) no;
                return "(" + gerar(b.esq) + " " + b.op + " " + gerar(b.dir) + ")";
            }
            if (no instanceof Assign) {
                Assign a = (Assign) no;
                return ind() + a.nome + " = " + gerar(a.expr) + ";";
            }
            if (no instanceof If) {
                If i = (If) no;
                StringBuilder sb = new StringBuilder();
                sb.append(ind()).append("if (").append(gerar(i.cond)).append(") {\n");
                indent++;
                for (No s : i.thenBody) sb.append(gerar(s)).append("\n");
                indent--;
                sb.append(ind()).append("} else {\n");
                indent++;
                for (No s : i.elseBody) sb.append(gerar(s)).append("\n");
                indent--;
                sb.append(ind()).append("}");
                return sb.toString();
            }
            throw new RuntimeException("Nó não suportado: " + no.getClass().getSimpleName());
        }
    }

    public static void main(String[] args) {
        // AST para: if (x > 0) y = x * 2; else y = 0;
        If ast = new If(
            new BinOp(">", new Var("x"), new Num(0)),
            Arrays.asList(new Assign("y", new BinOp("*", new Var("x"), new Num(2)))),
            Arrays.asList(new Assign("y", new Num(0)))
        );

        System.out.println("=== Código de Três Endereços (TAC) ===");
        GeradorTAC genTac = new GeradorTAC();
        genTac.gerar(ast);
        for (String linha : genTac.codigo) System.out.println("  " + linha);

        System.out.println("\n=== Código C Gerado ===");
        GeradorC genC = new GeradorC();
        System.out.println(genC.gerar(ast));
    }
}
