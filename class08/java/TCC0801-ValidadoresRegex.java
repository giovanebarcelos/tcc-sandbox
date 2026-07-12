import java.util.regex.Pattern;

/**
 * TCC0801-ValidadoresRegex.java | Aula 08
 * Validadores com expressões regulares: CPF, e-mail, URL.
 * Mesma lógica e mesmos padrões do TCC0801-ValidadoresRegex.py.
 */
class ValidadoresRegex {

    static final String CPF = "^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$";
    static final String EMAIL = "^[\\w.+-]+@[\\w-]+\\.[\\w.]+$";
    static final String IDENTIFICADOR = "^[a-zA-Z_][a-zA-Z0-9_]*$";
    static final String INTEIRO = "^[+-]?\\d+$";

    static void validar(String nome, String pattern,
                        String[] validos, String[] invalidos) {
        System.out.println("\n=== " + nome + " ===");
        System.out.println("Pattern: " + pattern);
        Pattern p = Pattern.compile(pattern);
        for (String ex : validos) {
            boolean ok = p.matcher(ex).matches();
            System.out.println("  " + (ok ? "✓" : "✗") + " " + ex);
        }
        for (String ex : invalidos) {
            boolean ok = p.matcher(ex).matches();
            System.out.println("  " + (!ok ? "✓" : "✗") + " " + ex + " (inválido)");
        }
    }

    public static void main(String[] args) {
        validar("CPF", CPF,
                new String[]{"123.456.789-00", "000.000.000-00"},
                new String[]{"123.456.789-0", "12345678900", "12.345.678-900"});
        validar("E-mail", EMAIL,
                new String[]{"user@example.com", "a.b+c@domain.co"},
                new String[]{"user@", "@domain.com", "user@domain", ""});
        validar("Identificador", IDENTIFICADOR,
                new String[]{"x", "x1", "_total", "MAX_VALUE"},
                new String[]{"1x", "x-y", "", "class"});
        validar("Inteiro", INTEIRO,
                new String[]{"0", "42", "-7", "+100"},
                new String[]{"", "3.14", "1_000", "abc"});
    }
}
