// TCC2701-MiniCompilador.java | Aula 27
// Esqueleto do Projeto Integrador A3 — mini-interpretador.
// Linguagem: variáveis, expressões, if/else, while, print.
// Espelho Java do TCC2701-MiniCompilador.py (mesma linguagem e saídas).
import java.util.*;

public class TCC2701MiniCompilador {

    // ---------- Token ----------
    static class Token {
        String tipo, valor; int linha, col;
        Token(String t, String v, int l, int c) { tipo = t; valor = v; linha = l; col = c; }
    }

    // ---------- Scanner ----------
    static List<Token> scanner(String fonte) {
        List<Token> tokens = new ArrayList<>();
        int i = 0, n = fonte.length(), linha = 1, col = 1;
        Set<String> keywords = new HashSet<>(Arrays.asList("if", "else", "while", "print"));
        while (i < n) {
            char c = fonte.charAt(i);
            if (c == '\n') { i++; linha++; col = 1; continue; }
            if (Character.isWhitespace(c)) { i++; col++; continue; }
            if (c == '#') { while (i < n && fonte.charAt(i) != '\n') i++; continue; }
            int startCol = col;
            if (Character.isDigit(c)) {
                int j = i; while (j < n && Character.isDigit(fonte.charAt(j))) j++;
                if (j < n && fonte.charAt(j) == '.') { j++; while (j < n && Character.isDigit(fonte.charAt(j))) j++; }
                tokens.add(new Token("NUM", fonte.substring(i, j), linha, startCol));
                col += j - i; i = j; continue;
            }
            if (Character.isLetter(c) || c == '_') {
                int j = i; while (j < n && (Character.isLetterOrDigit(fonte.charAt(j)) || fonte.charAt(j) == '_')) j++;
                String lex = fonte.substring(i, j);
                tokens.add(new Token(keywords.contains(lex) ? "KEYWORD" : "ID", lex, linha, startCol));
                col += j - i; i = j; continue;
            }
            if ("+-*/=<>".indexOf(c) >= 0) {
                if (c == '=' && i + 1 < n && fonte.charAt(i + 1) == '=') {
                    tokens.add(new Token("OP", "==", linha, startCol)); i += 2; col += 2; continue;
                }
                tokens.add(new Token("OP", String.valueOf(c), linha, startCol)); i++; col++; continue;
            }
            if ("(){};".indexOf(c) >= 0) {
                tokens.add(new Token("PUNCT", String.valueOf(c), linha, startCol)); i++; col++; continue;
            }
            throw new RuntimeException("Caractere inválido: '" + c + "' linha " + linha + " col " + col);
        }
        tokens.add(new Token("EOF", "", linha, col));
        return tokens;
    }

    // ---------- AST ----------
    static abstract class No {}
    static class Num extends No { double val; Num(double v) { val = v; } }
    static class Var extends No { String nome; Var(String n) { nome = n; } }
    static class BinOp extends No { String op; No esq, dir; BinOp(String o, No e, No d) { op = o; esq = e; dir = d; } }
    static class Assign extends No { String nome; No expr; Assign(String n, No e) { nome = n; expr = e; } }
    static class Print extends No { No expr; Print(No e) { expr = e; } }
    static class If extends No { No cond; List<No> thenB, elseB; If(No c, List<No> t, List<No> e) { cond = c; thenB = t; elseB = e; } }
    static class While extends No { No cond; List<No> body; While(No c, List<No> b) { cond = c; body = b; } }

    // ---------- Parser ----------
    static class Parser {
        List<Token> tokens; int pos = 0;
        Parser(List<Token> t) { tokens = t; }
        Token peek() { return tokens.get(pos); }
        Token consume() { return tokens.get(pos++); }
        Token consume(String tipo) {
            Token t = tokens.get(pos);
            if (!t.tipo.equals(tipo)) throw new RuntimeException("Esperado " + tipo + ", obtido " + t.tipo + "('" + t.valor + "') linha " + t.linha);
            pos++; return t;
        }
        Token consume(String tipo, String valor) {
            Token t = consume(tipo);
            if (!t.valor.equals(valor)) throw new RuntimeException("Esperado '" + valor + "', obtido '" + t.valor + "' linha " + t.linha);
            return t;
        }

        List<No> parse() {
            List<No> stmts = new ArrayList<>();
            while (!peek().tipo.equals("EOF")) stmts.add(stmt());
            return stmts;
        }

        No stmt() {
            Token t = peek();
            if (t.tipo.equals("KEYWORD") && t.valor.equals("if")) return ifStmt();
            if (t.tipo.equals("KEYWORD") && t.valor.equals("while")) return whileStmt();
            if (t.tipo.equals("KEYWORD") && t.valor.equals("print")) return printStmt();
            if (t.tipo.equals("ID")) return assign();
            throw new RuntimeException("Token inesperado: " + t.tipo + "('" + t.valor + "')");
        }

        No assign() {
            String nome = consume("ID").valor;
            consume("OP", "=");
            No expr = expr();
            consume("PUNCT", ";");
            return new Assign(nome, expr);
        }

        No ifStmt() {
            consume("KEYWORD", "if"); consume("PUNCT", "(");
            No cond = expr(); consume("PUNCT", ")");
            List<No> thenB = block();
            List<No> elseB = new ArrayList<>();
            if (peek().tipo.equals("KEYWORD") && peek().valor.equals("else")) {
                consume("KEYWORD", "else"); elseB = block();
            }
            return new If(cond, thenB, elseB);
        }

        No whileStmt() {
            consume("KEYWORD", "while"); consume("PUNCT", "(");
            No cond = expr(); consume("PUNCT", ")");
            return new While(cond, block());
        }

        No printStmt() {
            consume("KEYWORD", "print"); consume("PUNCT", "(");
            No expr = expr(); consume("PUNCT", ")"); consume("PUNCT", ";");
            return new Print(expr);
        }

        List<No> block() {
            consume("PUNCT", "{");
            List<No> stmts = new ArrayList<>();
            while (!peek().valor.equals("}")) stmts.add(stmt());
            consume("PUNCT", "}");
            return stmts;
        }

        No expr() { return binop(term(), "+-"); }
        No term() { return binop(factor(), "*/"); }

        No binop(No esq, String ops) {
            while (peek().tipo.equals("OP") && ops.contains(peek().valor)) {
                String op = consume().valor;
                No dir = ops.contains("+") ? term() : factor();
                esq = new BinOp(op, esq, dir);
            }
            return esq;
        }

        No factor() {
            Token t = peek();
            if (t.tipo.equals("NUM")) { consume(); return new Num(Double.parseDouble(t.valor)); }
            if (t.tipo.equals("ID")) { consume(); return new Var(t.valor); }
            if (t.valor.equals("(")) { consume("PUNCT", "("); No e = expr(); consume("PUNCT", ")"); return e; }
            throw new RuntimeException("Token inesperado em factor: " + t.tipo + "('" + t.valor + "')");
        }
    }

    // ---------- Interpretador ----------
    static class Interpretador {
        Map<String, Double> vars = new HashMap<>();

        void executar(List<No> stmts) { for (No s : stmts) execStmt(s); }

        void execStmt(No s) {
            if (s instanceof Assign) { Assign a = (Assign) s; vars.put(a.nome, eval(a.expr)); }
            else if (s instanceof Print) { System.out.println(eval(((Print) s).expr)); }
            else if (s instanceof If) { If i = (If) s; if (eval(i.cond) != 0) executar(i.thenB); else executar(i.elseB); }
            else if (s instanceof While) { While w = (While) s; while (eval(w.cond) != 0) executar(w.body); }
        }

        double eval(No e) {
            if (e instanceof Num) return ((Num) e).val;
            if (e instanceof Var) {
                Var v = (Var) e;
                if (!vars.containsKey(v.nome)) throw new RuntimeException("Variável '" + v.nome + "' não definida");
                return vars.get(v.nome);
            }
            if (e instanceof BinOp) {
                BinOp b = (BinOp) e;
                double esq = eval(b.esq), dir = eval(b.dir);
                switch (b.op) {
                    case "+": return esq + dir;
                    case "-": return esq - dir;
                    case "*": return esq * dir;
                    case "/": return esq / dir;
                    case ">": return esq > dir ? 1 : 0;
                    case "<": return esq < dir ? 1 : 0;
                    default: throw new RuntimeException("Operador inválido: " + b.op);
                }
            }
            throw new RuntimeException("Nó não suportado: " + e.getClass().getSimpleName());
        }
    }

    public static void main(String[] args) {
        String fonte = "x = 3 + 4 * 2;\nprint(x);\nif (x > 10) {\n    print(1);\n} else {\n    print(0);\n}\n";
        System.out.println("=== Fonte ===");
        System.out.println(fonte);
        List<Token> tokens = scanner(fonte);
        List<No> ast = new Parser(tokens).parse();
        System.out.println("=== Execução ===");
        new Interpretador().executar(ast);
    }
}
