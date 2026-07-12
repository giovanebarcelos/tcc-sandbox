// TCC1501-FasesCompilador.java | Aula 15
// Mini-pipeline didático de compilador: fonte → tokens → AST → código intermediário.
// Demonstra as 6 fases de forma executável e simples.
// Espelho Java do TCC1501-FasesCompilador.py (mesmas entradas e saídas).
import java.util.*;

public class TCC1501FasesCompilador {

    // ---------- Fase 1: Análise Léxica (Scanner) ----------
    static class Token {
        String tipo, valor;
        Token(String tipo, String valor) { this.tipo = tipo; this.valor = valor; }
        public String toString() { return tipo + "(" + valor + ")"; }
    }

    static List<Token> scanner(String fonte) {
        List<Token> tokens = new ArrayList<>();
        int i = 0, n = fonte.length();
        while (i < n) {
            char c = fonte.charAt(i);
            if (Character.isWhitespace(c)) { i++; continue; }
            if (Character.isDigit(c)) {
                int j = i;
                while (j < n && Character.isDigit(fonte.charAt(j))) j++;
                tokens.add(new Token("NUM", fonte.substring(i, j)));
                i = j;
            } else if (Character.isLetter(c)) {
                int j = i;
                while (j < n && Character.isLetterOrDigit(fonte.charAt(j))) j++;
                tokens.add(new Token("ID", fonte.substring(i, j)));
                i = j;
            } else if ("+-*/=".indexOf(c) >= 0) {
                tokens.add(new Token("OP", String.valueOf(c)));
                i++;
            } else if (c == '(' || c == ')') {
                tokens.add(new Token("PAREN", String.valueOf(c)));
                i++;
            } else {
                throw new RuntimeException("Caractere inválido: " + c);
            }
        }
        return tokens;
    }

    // ---------- Fase 2: Análise Sintática (Parser) ----------
    static class No {
        String op;
        Object esq, dir;
        No(String op, Object esq, Object dir) { this.op = op; this.esq = esq; this.dir = dir; }
        public String toString() {
            return "No(" + op + "," + esq + "," + dir + ")";
        }
    }

    static class Parser {
        List<Token> tokens;
        int pos = 0;
        Parser(List<Token> tokens) { this.tokens = tokens; }

        Token peek() { return pos < tokens.size() ? tokens.get(pos) : null; }
        Token consume() { return tokens.get(pos++); }

        No parse() { return expr(); }

        No expr() {
            Object no = term();
            while (peek() != null && peek().tipo.equals("OP") && "+-".contains(peek().valor)) {
                String op = consume().valor;
                Object dir = term();
                no = new No(op, no, dir);
            }
            return (No) no;
        }

        Object term() {
            Object no = factor();
            while (peek() != null && peek().tipo.equals("OP") && "*/".contains(peek().valor)) {
                String op = consume().valor;
                Object dir = factor();
                no = new No(op, no, dir);
            }
            return no;
        }

        Object factor() {
            Token t = peek();
            if (t != null && (t.tipo.equals("NUM") || t.tipo.equals("ID"))) {
                consume();
                return t.valor;
            }
            if (t != null && t.tipo.equals("PAREN") && t.valor.equals("(")) {
                consume();
                No no = expr();
                if (peek() == null || !peek().valor.equals(")"))
                    throw new RuntimeException("Esperado ')'");
                consume();
                return no;
            }
            throw new RuntimeException("Token inesperado: " + t);
        }
    }

    // ---------- Fase 3: Análise Semântica (Tabela de Símbolos) ----------
    static Set<String> coletarIds(Object no) {
        Set<String> ids = new LinkedHashSet<>();
        if (no instanceof String) {
            String s = (String) no;
            if (!s.isEmpty() && !s.matches("\\d+")) ids.add(s);
        } else if (no instanceof No) {
            No n = (No) no;
            ids.addAll(coletarIds(n.esq));
            ids.addAll(coletarIds(n.dir));
        }
        return ids;
    }

    // ---------- Fase 4: Geração de Código Intermediário (TAC) ----------
    static List<String> tac = new ArrayList<>();
    static int tempCounter = 0;

    static String novoTemp() { return "t" + (++tempCounter); }

    static String gerarTac(Object no) {
        if (no instanceof String) return (String) no;
        No n = (No) no;
        String esq = gerarTac(n.esq);
        String dir = gerarTac(n.dir);
        String t = novoTemp();
        tac.add(t + " = " + esq + " " + n.op + " " + dir);
        return t;
    }

    // ---------- Fase 5: Otimização (constant folding) ----------
    static List<String> otimizarTac() {
        List<String> ot = new ArrayList<>();
        Map<String, Integer> valores = new HashMap<>();
        for (String linha : tac) {
            String[] parts = linha.split(" = ");
            if (parts.length == 2) {
                String dst = parts[0], expr = parts[1];
                String[] toks = expr.split(" ");
                if (toks.length == 3 && toks[0].matches("\\d+") && toks[2].matches("\\d+")) {
                    int a = Integer.parseInt(toks[0]), b = Integer.parseInt(toks[2]);
                    String op = toks[1];
                    Integer v = null;
                    switch (op) {
                        case "+": v = a + b; break;
                        case "-": v = a - b; break;
                        case "*": v = a * b; break;
                        case "/": v = a / b; break;
                    }
                    if (v != null) {
                        valores.put(dst, v);
                        ot.add(dst + " = " + v + "  (constant folding)");
                        continue;
                    }
                }
            }
            ot.add(linha);
        }
        return ot;
    }

    // ---------- Fase 6: Geração de Código Final ----------
    static String gerarPython(Object no) {
        if (no instanceof String) return (String) no;
        No n = (No) no;
        return "(" + gerarPython(n.esq) + " " + n.op + " " + gerarPython(n.dir) + ")";
    }

    // ---------- Pipeline completo ----------
    public static void main(String[] args) {
        String fonte = "3 + 4 * x";
        System.out.println("=== Fonte: \"" + fonte + "\" ===\n");

        System.out.println("--- Fase 1: Análise Léxica ---");
        List<Token> tokens = scanner(fonte);
        System.out.println("Tokens: " + tokens + "\n");

        System.out.println("--- Fase 2: Análise Sintática (AST) ---");
        No ast = new Parser(tokens).parse();
        System.out.println("AST: " + ast + "\n");

        System.out.println("--- Fase 3: Análise Semântica (Tabela de Símbolos) ---");
        Set<String> ids = coletarIds(ast);
        System.out.println("Identificadores: " + ids + "\n");

        System.out.println("--- Fase 4: Geração de Código Intermediário (TAC) ---");
        gerarTac(ast);
        for (String linha : tac) System.out.println("  " + linha);
        System.out.println();

        System.out.println("--- Fase 5: Otimização (constant folding) ---");
        List<String> ot = otimizarTac();
        for (String linha : ot) System.out.println("  " + linha);
        System.out.println();

        System.out.println("--- Fase 6: Geração de Código Final (Python) ---");
        System.out.println("  resultado = " + gerarPython(ast));
    }
}
