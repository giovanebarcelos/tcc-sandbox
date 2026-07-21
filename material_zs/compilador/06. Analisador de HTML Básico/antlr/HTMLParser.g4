parser grammar HTMLParser;
options { tokenVocab=HTMLLexer; }

@header {
   import java.util.*;
   import java.io.*;
   import org.antlr.v4.runtime.*;
   import org.antlr.v4.runtime.tree.*;
}

@members {
   public static void main(String[] args) throws Exception {
      HTMLLexer lexer = 
         new HTMLLexer(CharStreams.fromFileName(args[0]));
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      HTMLParser parser = new HTMLParser(tokens);
      ParseTree tree = parser.document();
      System.out.println(tree.toStringTree(parser));
      System.out.println("Parsing completed successfully.");
      System.out.println("\nAstha La Vista Baby!");
   }
}

document: element_list EOF;

element_list: (element)* ; 

element: tag | TEXT; 

tag: TAG_OPEN NAME attributes TAG_CLOSE element_list TAG_END_OPEN NAME TAG_CLOSE;

attributes: (attribute)* ; 

attribute: NAME EQ ATTR_VALUE; 