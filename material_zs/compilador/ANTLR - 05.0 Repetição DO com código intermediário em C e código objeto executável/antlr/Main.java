import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.gui.Trees;
import java.io.*;

public class Main {
    public static void main(String[] args) throws Exception {
        CharStream input = CharStreams.fromFileName("do.txt");
        DoLexer lexer = new DoLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        DoParser parser = new DoParser(tokens);
        ParseTree tree = parser.programa();

        // Mostra no console
        System.out.println(tree.toStringTree(parser));

        // Mostra graficamente
        Trees.inspect(tree, parser);
    }
}

