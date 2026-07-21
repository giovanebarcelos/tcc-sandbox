import java.io.Reader;
import java.io.StringReader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java_cup.runtime.Symbol;

public class TemplateCompiler {
    public static Template compile(String input) throws Exception {
        return compile(new StringReader(input));
    }

    public static Template compile(Reader reader) throws Exception {
        Lexer lexer = new Lexer(reader);
        TemplateParser parser = new TemplateParser(lexer);
        Symbol result = parser.parse();
        return (Template) result.value;
    }

    public static Template compileFile(String path) throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            return compile(reader);
        }
    }
}
