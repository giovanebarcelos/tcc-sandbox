// TCC2302-Visitor.java | Aula 23
// Padrão Visitor isolado (versão didática enxuta do Slide 8): separa a
// estrutura da AST (Num/BinOp) do comportamento (avaliação), permitindo
// adicionar novas operações sem alterar as classes dos nós.
// Espelho Java do TCC2302-Visitor.py (mesma estrutura; a saída imprime 20.0
// em vez de 20 porque o valor é double, como no TCC2301-AST.java).
//
// Em Java o double dispatch fica explícito: `no.aceitar(v)` resolve o tipo do
// NÓ (despacho 1, pela tabela de métodos virtuais), e dentro de `aceitar` a
// chamada `v.visitarNum(this)` resolve o tipo do VISITOR (despacho 2, por
// sobrecarga estática já ligada ao tipo concreto). É esse par de despachos
// que substitui o `instanceof` encadeado.

public class TCC2302Visitor {

    // ---------- Visitor (abstrato) ----------
    abstract static class ASTVisitor {
        abstract double visitarNum(Num no);
        abstract double visitarBinOp(BinOp no);
    }

    // ---------- Nós da AST ----------
    abstract static class No {
        abstract double aceitar(ASTVisitor v);
    }

    static class Num extends No {
        double valor;
        Num(double valor) { this.valor = valor; }
        double aceitar(ASTVisitor v) { return v.visitarNum(this); }
    }

    static class BinOp extends No {
        String op; No esq, dir;
        BinOp(String op, No esq, No dir) { this.op = op; this.esq = esq; this.dir = dir; }
        double aceitar(ASTVisitor v) { return v.visitarBinOp(this); }
    }

    // ---------- Comportamento: avaliar ----------
    static class EvalVisitor extends ASTVisitor {
        double visitarNum(Num no) { return no.valor; }

        double visitarBinOp(BinOp no) {
            double esq = no.esq.aceitar(this);
            double dir = no.dir.aceitar(this);
            return no.op.equals("+") ? esq + dir : esq * dir;
        }
    }

    public static void main(String[] args) {
        // (2 + 3) * 4
        No ast = new BinOp("*", new BinOp("+", new Num(2), new Num(3)), new Num(4));
        double resultado = ast.aceitar(new EvalVisitor());
        System.out.println("(2 + 3) * 4 = " + resultado);
    }
}
