// TCC2301-AST.java | Aula 23
// AST (Abstract Syntax Tree) da calculadora com padrões Visitor e Factory.
// Espelho Java do TCC2301-AST.py (mesma hierarquia e saídas).
import java.util.*;

public class TCC2301AST {

    // ---------- AST ----------
    abstract static class No {
        abstract Object aceitar(Visitor v);
    }

    static class NumNode extends No {
        double valor;
        NumNode(double valor) { this.valor = valor; }
        Object aceitar(Visitor v) { return v.visitarNum(this); }
    }

    static class VarNode extends No {
        String nome;
        VarNode(String nome) { this.nome = nome; }
        Object aceitar(Visitor v) { return v.visitarVar(this); }
    }

    static class BinOpNode extends No {
        String op; No esq, dir;
        BinOpNode(String op, No esq, No dir) { this.op = op; this.esq = esq; this.dir = dir; }
        Object aceitar(Visitor v) { return v.visitarBinOp(this); }
    }

    static class AssignNode extends No {
        String nome; No expr;
        AssignNode(String nome, No expr) { this.nome = nome; this.expr = expr; }
        Object aceitar(Visitor v) { return v.visitarAssign(this); }
    }

    static class PrintNode extends No {
        No expr;
        PrintNode(No expr) { this.expr = expr; }
        Object aceitar(Visitor v) { return v.visitarPrint(this); }
    }

    // ---------- Factory ----------
    static class NoFactory {
        static NumNode num(double v) { return new NumNode(v); }
        static VarNode var(String n) { return new VarNode(n); }
        static BinOpNode binop(String op, No e, No d) { return new BinOpNode(op, e, d); }
        static AssignNode assign(String n, No e) { return new AssignNode(n, e); }
        static PrintNode print(No e) { return new PrintNode(e); }
    }

    // ---------- Visitor (abstrato) ----------
    abstract static class Visitor {
        abstract Object visitarNum(NumNode n);
        abstract Object visitarVar(VarNode n);
        abstract Object visitarBinOp(BinOpNode n);
        abstract Object visitarAssign(AssignNode n);
        abstract Object visitarPrint(PrintNode n);
    }

    // ---------- EvalVisitor ----------
    static class EvalVisitor extends Visitor {
        Map<String, Double> tabela = new LinkedHashMap<>();

        Object visitarNum(NumNode n) { return n.valor; }
        Object visitarVar(VarNode n) {
            if (!tabela.containsKey(n.nome))
                throw new RuntimeException("Variável '" + n.nome + "' não definida");
            return tabela.get(n.nome);
        }
        Object visitarBinOp(BinOpNode n) {
            double esq = (double) n.esq.aceitar(this);
            double dir = (double) n.dir.aceitar(this);
            switch (n.op) {
                case "+": return esq + dir;
                case "-": return esq - dir;
                case "*": return esq * dir;
                case "/": return esq / dir;
                default: throw new RuntimeException("Operador inválido: " + n.op);
            }
        }
        Object visitarAssign(AssignNode n) {
            double val = (double) n.expr.aceitar(this);
            tabela.put(n.nome, val);
            return val;
        }
        Object visitarPrint(PrintNode n) {
            double val = (double) n.expr.aceitar(this);
            System.out.println(val);
            return val;
        }
    }

    // ---------- PrintVisitor ----------
    static class PrintVisitor extends Visitor {
        Object visitarNum(NumNode n) { return String.valueOf(n.valor); }
        Object visitarVar(VarNode n) { return n.nome; }
        Object visitarBinOp(BinOpNode n) {
            return "(" + n.esq.aceitar(this) + " " + n.op + " " + n.dir.aceitar(this) + ")";
        }
        Object visitarAssign(AssignNode n) {
            return n.nome + " = " + n.expr.aceitar(this);
        }
        Object visitarPrint(PrintNode n) {
            return "print(" + n.expr.aceitar(this) + ")";
        }
    }

    public static void main(String[] args) {
        // AST para: x = 3 + 4 * 2; print(x);
        List<No> ast = Arrays.asList(
            NoFactory.assign("x", NoFactory.binop("+", NoFactory.num(3),
                NoFactory.binop("*", NoFactory.num(4), NoFactory.num(2)))),
            NoFactory.print(NoFactory.var("x"))
        );

        System.out.println("=== PrintVisitor (representação da AST) ===");
        PrintVisitor pv = new PrintVisitor();
        for (No no : ast) {
            System.out.println("  " + no.aceitar(pv));
        }

        System.out.println("\n=== EvalVisitor (avaliação) ===");
        EvalVisitor ev = new EvalVisitor();
        for (No no : ast) {
            no.aceitar(ev);
        }

        System.out.println("\nTabela de símbolos: " + ev.tabela);
    }
}
