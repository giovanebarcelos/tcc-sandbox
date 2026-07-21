import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws Exception {
        CharStream input = CharStreams.fromFileName("exemplo.html");

        HTMLLexer lexer = new HTMLLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // Debug opcional:
        // tokens.fill();
        // for (Token t : tokens.getTokens()) System.out.println(t);

        HTMLParser parser = new HTMLParser(tokens);
        ParseTree tree = parser.document();
        System.out.println(tree.toStringTree(parser));
    }
}
