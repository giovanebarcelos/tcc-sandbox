// TCC2601-Main.java | Aula 26
// Driver Java para a gramática ANTLR TCC2601-Expr.g4.
// Avalia expressões usando Visitor.
// Build: antlr4 -Dlanguage=Java -visitor TCC2601-Expr.g4
//        javac TCC2601Expr*.java TCC2601Main.java
//        java TCC2601Main "3 + 4 * 2"
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class TCC2601Main extends TCC2601ExprBaseVisitor<Double> {

    @Override
    public Double visitProg(TCC2601ExprParser.ProgContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public Double visitAdd(TCC2601ExprParser.AddContext ctx) {
        return visit(ctx.expr(0)) + visit(ctx.expr(1));
    }

    @Override
    public Double visitMul(TCC2601ExprParser.MulContext ctx) {
        return visit(ctx.expr(0)) * visit(ctx.expr(1));
    }

    @Override
    public Double visitParens(TCC2601ExprParser.ParensContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public Double visitNumber(TCC2601ExprParser.NumberContext ctx) {
        return Double.parseDouble(ctx.NUM().getText());
    }

    public static void main(String[] args) {
        String input = args.length > 0 ? args[0] : "3 + 4 * 2";
        System.out.println("=== ANTLR Java Driver ===");
        System.out.println("Expressão: " + input);
        CharStream cs = CharStreams.fromString(input);
        TCC2601ExprLexer lexer = new TCC2601ExprLexer(cs);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        TCC2601ExprParser parser = new TCC2601ExprParser(tokens);
        ParseTree tree = parser.prog();
        TCC2601Main visitor = new TCC2601Main();
        Double result = visitor.visit(tree);
        System.out.println("Resultado: " + result);
        System.out.println("Árvore: " + tree.toStringTree(parser));
    }
}
