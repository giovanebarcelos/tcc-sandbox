# 10 Exemplos de Enunciados e Soluções com JFlex e JCup  
  
Aqui estão 10 exemplos completos de analisadores léxicos e sintáticos usando JFlex e CUP, juntamente com arquivos de teste para cada um.  
  
## Exemplo 1: Calculadora Simples  
  
**Enunciado**: Criar uma calculadora que reconheça expressões aritméticas com números inteiros, operadores +, -, *, / e parênteses.  
  
### Arquivos:  
  
------------------------------------------------------------------------------------------  
  
1. `calc.flex`:  
```lex  
import java_cup.runtime.*;  
  
%%  
  
%class Lexer  
%unicode  
%cup  
%line  
%column  
  
%{  
    private Symbol symbol(int type) {  
        return new Symbol(type, yyline, yycolumn);  
    }  
    private Symbol symbol(int type, Object value) {  
        return new Symbol(type, yyline, yycolumn, value);  
    }  
%}  
  
digit = [0-9]  
number = {digit}+  
whitespace = [ \t\n\r]  
  
%%  
  
%%  
  
"+"     { return symbol(sym.PLUS); }  
"-"     { return symbol(sym.MINUS); }  
"*"     { return symbol(sym.TIMES); }  
"/"     { return symbol(sym.DIVIDE); }  
"("     { return symbol(sym.LPAREN); }  
")"     { return symbol(sym.RPAREN); }  
{number} { return symbol(sym.NUMBER, Integer.valueOf(yytext())); }  
{whitespace} { /* ignore */ }  
. { System.err.println("Caractere ilegal: '" + yytext() + "'"); }  
  
<<EOF>> { return symbol(sym.EOF); }  
```  
  
2. `calc.cup`:  
```cup  
import java_cup.runtime.*;  
  
parser code {:  
    public static void main(String[] args) throws Exception {  
        Lexer lexer = new Lexer(new java.io.FileReader(args[0]));  
        parser p = new parser(lexer);  
        Object result = p.parse().value;  
        System.out.println("Resultado = "+result);  
    }  
:}  
  
terminal Integer NUMBER;  
terminal PLUS, MINUS, TIMES, DIVIDE, LPAREN, RPAREN;  
  
non terminal Integer expr;  
  
precedence left PLUS, MINUS;  
precedence left TIMES, DIVIDE;  
  
expr ::= expr:e1 PLUS expr:e2       {: RESULT = e1 + e2; :}  
       | expr:e1 MINUS expr:e2      {: RESULT = e1 - e2; :}  
       | expr:e1 TIMES expr:e2      {: RESULT = e1 * e2; :}  
       | expr:e1 DIVIDE expr:e2     {: RESULT = e1 / e2; :}  
       | LPAREN expr:e RPAREN       {: RESULT = e; :}  
       | NUMBER:n                   {: RESULT = n; :}  
       ;  
  
```  
  
3. `test_calc.txt`:  
```  
(2 + 3) * 5 - 8 / 2  
```  
  
```  
# 1. Gerar Lexer  
jflex calc.flex  
  
# 2. Gerar Parser  
java java_cup.Main calc.cup  
  
# 3. Compilar tudo  
javac *.java  
  
# 4. Executar  
java parser test_calc.txt  
```  
  
------------------------------------------------------------------------------------------  
  
## Exemplo 2: Analisador de JSON Simples  
  
**Enunciado**: Criar um analisador para um subconjunto de JSON que suporte objetos com pares chave-valor, arrays, strings e números.  
  
### Arquivos:  
  
1. `json.flex`:  
```flex  
import java_cup.runtime.*;  
  
%%  
  
%class JSONLexer  
%unicode  
%cup  
%line  
%column  
  
%{  
    private Symbol symbol(int type) {  
        return new Symbol(type, yyline, yycolumn);  
    }  
    private Symbol symbol(int type, Object value) {  
        return new Symbol(type, yyline, yycolumn, value);  
    }  
%}  
  
digit = [0-9]  
int = -?{digit}+  
float = -?{digit}+\.{digit}+  
string = \"([^\"\\]|\\.)*\"  
whitespace = [ \t\n\r]  
  
%%  
  
"{"     { return symbol(sym.LBRACE); }  
"}"     { return symbol(sym.RBRACE); }  
"["     { return symbol(sym.LBRACKET); }  
"]"     { return symbol(sym.RBRACKET); }  
":"     { return symbol(sym.COLON); }  
","     { return symbol(sym.COMMA); }  
"true"  { return symbol(sym.TRUE); }  
"false" { return symbol(sym.FALSE); }  
"null"  { return symbol(sym.NULL); }  
{int}   { return symbol(sym.INTEGER, Integer.valueOf(yytext())); }  
{float} { return symbol(sym.FLOAT, Double.valueOf(yytext())); }  
{string} { return symbol(sym.STRING, yytext().substring(1, yytext().length()-1)); }  
{whitespace} { /* ignore */ }  
  
<<EOF>> { return symbol(sym.EOF); }  
```  
  
2. `json.cup`:  
```cup  
import java_cup.runtime.*;  
  
parser code {:  
    public static void main(String[] args) throws Exception {  
        JSONLexer lexer = new JSONLexer(new java.io.FileReader(args[0]));  
        parser p = new parser(lexer);  
        Object result = p.parse().value;  
        System.out.println("Resultado = "+result);  
    }  
:}  
  
terminal LBRACE, RBRACE, LBRACKET, RBRACKET, COLON, COMMA, TRUE, FALSE, NULL;  
terminal Integer INTEGER;  
terminal Double FLOAT;  
terminal String STRING;  
  
non terminal Object json, value, object, array, pair, pairs, elements;  
  
json ::= value {: RESULT = "OK"; :};  
  
value ::= object  
        | array  
        | STRING  
        | INTEGER  
        | FLOAT  
        | TRUE  
        | FALSE  
        | NULL  
        ;  
  
object ::= LBRACE pairs RBRACE  
          | LBRACE RBRACE  
          ;  
  
pairs ::= pair  
         | pairs COMMA pair  
         ;  
  
pair ::= STRING COLON value;  
  
array ::= LBRACKET elements RBRACKET  
         | LBRACKET RBRACKET  
         ;  
  
elements ::= value  
            | elements COMMA value  
            ;  
```  
  
3. `test_json.txt`:  
```json  
{  
  "name": "John",  
  "age": 30,  
  "scores": [95.5, 88, 92],  
  "active": true  
}  
```  
  
```  
# 1. Gerar Lexer  
jflex json.flex  
  
# 2. Gerar Parser  
java java_cup.Main json.cup  
  
# 3. Compilar tudo  
javac *.java  
  
# 4. Executar  
java parser test_json.txt  
```  
  
------------------------------------------------------------------------------------------  
  
## Exemplo 3: Linguagem de Consulta Simples  
  
**Enunciado**: Criar uma linguagem para consultar dados com filtros básicos (SELECT, FROM, WHERE).  
  
### Arquivos:  
  
1. `query.flex`:  
```lex  
import java_cup.runtime.*;  
  
%%  
  
%class QueryLexer  
%unicode  
%cup  
%line  
%column  
  
%{  
    private Symbol symbol(int type) {  
        return new Symbol(type, yyline, yycolumn);  
    }  
    private Symbol symbol(int type, Object value) {  
        return new Symbol(type, yyline, yycolumn, value);  
    }  
%}  
  
letter = [a-zA-Z]  
digit = [0-9]  
id = {letter}({letter}|{digit}|"_")*  
number = {digit}+  
whitespace = [ \t\n\r]  
  
%%  
  
"SELECT" { return symbol(sym.SELECT); }  
"FROM" { return symbol(sym.FROM); }  
"WHERE" { return symbol(sym.WHERE); }  
"AND" { return symbol(sym.AND); }  
"=" { return symbol(sym.EQ); }  
">" { return symbol(sym.GT); }  
"<" { return symbol(sym.LT); }  
">=" { return symbol(sym.GTEQ); }  
"<=" { return symbol(sym.LTEQ); }  
"!=" { return symbol(sym.NOTEQ); }  
"," { return symbol(sym.COMMA); }  
"*" { return symbol(sym.ALL); }  
\'([^\\']|\\.)*\' { return symbol(sym.STRING, yytext().substring(1, yytext().length() - 1)); }  
{id} { return symbol(sym.ID, yytext()); }  
{number} { return symbol(sym.NUMBER, Integer.valueOf(yytext())); }  
{whitespace} { /* ignore */ }  
. { System.err.println("Caractere ilegal: '" + yytext() + "'"); }  
  
<<EOF>> { return symbol(sym.EOF); }  
```  
  
2. `query.cup`:  
```cup  
import java_cup.runtime.*;  
  
parser code {:  
    public static void main(String[] args) throws Exception {  
        QueryLexer lexer = new QueryLexer(new java.io.FileReader(args[0]));  
        parser p = new parser(lexer);  
        Object result = p.parse().value;  
        System.out.println("Resultado = "+result);  
    }  
:}  
  
terminal SELECT, FROM, WHERE, AND, EQ, GT, LT, GTEQ, LTEQ, NOTEQ, COMMA, ALL;  
terminal String ID;  
terminal Integer NUMBER;  
terminal String STRING;  
  
non terminal query, opt_where, operator, fields, field_list, table, conditions, condition, value;  
  
query ::= SELECT fields FROM table opt_where {: RESULT = "OK"; :};  
  
opt_where ::= WHERE conditions  
            | /* empty */  
            ;  
  
fields ::= ALL  
         | field_list  
         ;  
  
field_list ::= ID  
             | field_list COMMA ID  
             ;  
  
table ::= ID;  
  
conditions ::= condition  
             | conditions AND condition  
             ;  
  
condition ::= ID operator value;  
  
operator ::= EQ | GT | LT | GTEQ | LTEQ | NOTEQ;  
  
value ::= ID | NUMBER | STRING;  
```  
  
3. `test_query.txt`:  
```  
SELECT name, age FROM users WHERE age > 25 AND status = 'active'  
```  
  
```  
# 1. Gerar Lexer  
jflex query.flex  
  
# 2. Gerar Parser  
java java_cup.Main query.cup  
  
# 3. Compilar tudo  
javac *.java  
  
# 4. Executar  
java parser test_query.txt  
```  
  
------------------------------------------------------------------------------------------  
  
## Exemplo 4: IF/ELSE com código intermediário em C e código objeto executável  
  
**Enunciado**: Desenvolver um compilador que interprete o exemplo abaixo e gere um código intermediário em C e um código objeto executável ao final.  
Programa ifelse:  
  
if (a < 5) a = 10; else b = 20;  
  
### Arquivos:  
  
1. `ifelse.flex`:  
```lex  
import java_cup.runtime.*;  
  
%%  
  
%class IfElseLexer  
%unicode  
%cup  
%line  
%column  
  
%{  
  
StringBuffer string = new StringBuffer();  
  
// Tokens para cup  
private Symbol symbol(int type) {  
    return new Symbol(type, yyline+1, yycolumn+1);  
}  
  
private Symbol symbol(int type, Object value) {  
    return new Symbol(type, yyline+1, yycolumn+1, value);  
}  
  
%}  
  
DIGITO = [0-9]  
ID = [a-zA-Z_][a-zA-Z_0-9]*  
  
%%  
  
"if"        { return symbol(sym.IF); }  
"else"      { return symbol(sym.ELSE); }  
  
"("         { return symbol(sym.LPAREN); }  
")"         { return symbol(sym.RPAREN); }  
";"         { return symbol(sym.PV); }  
"="         { return symbol(sym.ATRIB); }  
  
"=="        { return symbol(sym.EQ); }  
"<"         { return symbol(sym.LT); }  
">"         { return symbol(sym.GT); }  
  
{ID}        { return symbol(sym.ID, yytext()); }  
{DIGITO}+   { return symbol(sym.NUM, Integer.parseInt(yytext())); }  
  
[ \t\r\n]+  { /* ignora espaços em branco */ }  
. {  
  throw new RuntimeException(  
    "Erro léxico: caractere inesperado '" + yytext() +  
    "' na linha " + (yyline + 1) + ", coluna " + (yycolumn + 1));  
}  
  
```  
  
  
2. `ifelse.cup`:  
```cup  
import java.util.*;  
import java.io.*;  
import java_cup.runtime.*;  
  
parser code {:  
  String novaLabel() {  
      return "L" + (++labelCount);  
  }  
  
  int labelCount = 0;  
  
  public static void main(String[] args) throws Exception{  
     try {  
       IfElseLexer lexer = new IfElseLexer(  
                                 new java.io.FileReader(args[0]));  
  
       parser p = new parser(lexer);  
       p.parse();  
  
       p.gerarArquivoC();  
       System.out.println("Arquivo C gerado: saida.c");  
  
       // Compilar o arquivo C usando gcc  
       Process pGcc = Runtime.getRuntime().exec("gcc saida.c -o programa");  
       pGcc.waitFor();  
  
       System.out.println("Executável 'programa' gerado com sucesso!");  
     } catch (Exception e) {  
          System.err.println(e.getMessage());  
     }  
  }  
  
  StringBuilder codigoC = new StringBuilder();  
  
  public void gerarArquivoC() throws IOException {  
      try (PrintWriter out = new PrintWriter("saida.c")) {  
          out.println("#include <stdio.h>");  
          out.println("int main() {");  
          out.println("int a = 0, b = 0, c = 0;"); // declarações genéricas  
          out.println(codigoC.toString());  
          out.println("return 0;");  
          out.println("}");  
      }  
  }  
  
  @Override  
  public void syntax_error(Symbol s) {  
    String nome = nomesTokens.getOrDefault(s.sym, nomeSimbolo(s));  
    System.err.println("Erro sintático: símbolo '" + nome +  
                       "' inesperado na linha " + s.left + ", coluna " + s.right);  
  }  
  
  static final Map<Integer, String> nomesTokens = Map.of(  
    sym.IF, "if",  
    sym.ELSE, "else",  
    sym.LPAREN, "(",  
    sym.RPAREN, ")",  
    sym.PV, ";",  
    sym.ATRIB, "=",  
    sym.EQ, "==",  
    sym.LT, "<",  
    sym.GT, ">"  
  );  
  
  String nomeSimbolo(Symbol s) {  
    try {  
        java.lang.reflect.Field[] fields = sym.class.getFields();  
        for (java.lang.reflect.Field f : fields) {  
            if (f.getType() == int.class && f.getInt(null) == s.sym) {  
                return f.getName();  
            }  
        }  
    } catch (Exception e) {  
        // ignorar  
    }  
    return "desconhecido";  
  }  
:}  
  
  
terminal IF, ELSE, LPAREN, RPAREN, PV, ATRIB;  
terminal EQ, LT, GT;  
terminal String ID;  
terminal Integer NUM;  
  
non terminal programa;  
non terminal String stmt;  
non terminal String cond;  
non terminal String bloco;  
non terminal String op;  
  
programa ::= stmt:s  
    {:  
        codigoC.append(s);  
    :};  
  
stmt ::= IF LPAREN cond:cond RPAREN bloco:b1 ELSE bloco:b2  
    {:  
        RESULT = "if (" + cond + ") {\n" + b1 + "\n} else {\n" + b2 + "\n}\n";  
    :};  
  
cond ::= ID:var op:operador NUM:num  
    {:  
        RESULT = var + " " + operador + " " + num;  
    :};  
  
op ::= EQ {: RESULT = "=="; :}  
     | LT {: RESULT = "<"; :}  
     | GT {: RESULT = ">"; :}  
     ;  
  
bloco ::= ID:var ATRIB NUM:num PV  
    {:  
        RESULT = var + " = " + num + ";\n";  
    :};  
```  
  
3. `ifelse.txt`:  
```txt  
if (a < 5) a = 10; else b = 20;  
  
if (x < 10) y = 5; else y = ;  
```  
  
```  
# 1. Gerar Lexer  
jflex ifelse.flex  
  
# 2. Gerar Parser  
java java_cup.Main ifelse.cup  
  
# 3. Compilar tudo  
javac *.java  
  
# 4. Executar  
java parser ifelse.txt  
```  
  
------------------------------------------------------------------------------------------  
## Exemplo 5: DO com código intermediário em C e código objeto executável  
  
**Enunciado**: Desenvolver um compilador que interprete o exemplo abaixo e gere um código intermediário em C e um código objeto executável ao final.  
Programa do:  
  
do [num = 1; num < 10 num++]  
   out(num)  
  
### Arquivos:  
  
1. `do.flex`:  
```lex  
import java_cup.runtime.*;  
  
%%  
  
%class DoLexer  
%unicode  
%cup  
%line  
%column  
  
%{  
  private Symbol symbol(int type) {  
      return new Symbol(type, yyline + 1, yycolumn + 1);  
  }  
  
  private Symbol symbol(int type, Object value) {  
      return new Symbol(type, yyline + 1, yycolumn + 1, value);  
  }  
%}  
  
DIGITO = [0-9]  
ID = [a-zA-Z_][a-zA-Z0-9_]*  
  
%%  
  
"do"        { return symbol(sym.DO); }  
"["         { return symbol(sym.LCOL); }  
"]"         { return symbol(sym.RCOL); }  
"("         { return symbol(sym.LPAREN); }  
")"         { return symbol(sym.RPAREN); }  
";"         { return symbol(sym.PV); }  
"="         { return symbol(sym.ATRIB); }  
"<"         { return symbol(sym.LT); }  
"++"        { return symbol(sym.INC); }  
"out"       { return symbol(sym.OUT); }  
  
{ID}        { return symbol(sym.ID, yytext()); }  
{DIGITO}+   { return symbol(sym.NUM, Integer.parseInt(yytext())); }  
  
[ \t\r\n]+  { /* ignorar espaços */ }  
  
. {  
  throw new RuntimeException(  
    "Erro léxico: caractere inesperado '" + yytext() +  
    "' na linha " + (yyline + 1) + ", coluna " + (yycolumn + 1));  
}  
```  
  
2. `do.cup`:  
```cup  
import java.util.*;  
import java.io.*;  
import java_cup.runtime.*;  
  
parser code {:  
  public static void main(String[] args) throws Exception {  
    try {  
      DoLexer lexer = new DoLexer(new FileReader(args[0]));  
      parser p = new parser(lexer);  
      p.parse();  
      p.gerarArquivoC();  
  
      // Compilar o arquivo com GCC  
      Process proc = Runtime.getRuntime().exec("gcc output.c -o program.exe");  
      proc.waitFor();  
      System.out.println("Executable 'program.exe' successfully generated!");  
    } catch (Exception e) {  
      System.err.println("Error: " + e.getMessage());  
    }  
  }  
  
  StringBuilder codeC = new StringBuilder();  
  
  public void gerarArquivoC() throws IOException {  
    try (PrintWriter out = new PrintWriter("output.c")) {  
      out.println("#include <stdio.h>");  
      out.println("int main() {");  
      out.println(codeC.toString());  
      out.println("return 0;");  
      out.println("}");  
    }  
  }  
  
  @Override  
  public void syntax_error(Symbol s) {  
    String nome = namesTokens.getOrDefault(s.sym, nameSymbol(s));  
    System.err.println("Syntactic error: symbol '" + nome +  
                       "' unexpected on the line " + s.left + ", column " + s.right);  
  }  
  
  static final Map<Integer, String> namesTokens = new HashMap<>();  
  static {  
    namesTokens.put(sym.EOF, "EOF");  
    namesTokens.put(sym.error, "error");  
    namesTokens.put(sym.LT, "<");  
    namesTokens.put(sym.DO, "do");  
    namesTokens.put(sym.LCOL, "[");  
    namesTokens.put(sym.RCOL, "]");  
    namesTokens.put(sym.LPAREN, "(");  
    namesTokens.put(sym.RPAREN, ")");  
    namesTokens.put(sym.PV, ";");  
    namesTokens.put(sym.ATRIB, "=");  
    namesTokens.put(sym.ID, "ID");  
    namesTokens.put(sym.NUM, "NUM");  
  }  
  
  String nameSymbol(Symbol s) {  
    try {  
        java.lang.reflect.Field[] fields = sym.class.getFields();  
        for (java.lang.reflect.Field f : fields) {  
            if (f.getType() == int.class && f.getInt(null) == s.sym) {  
                return f.getName();  
            }  
        }  
    } catch (Exception e) {  
        // ignore  
    }  
    return "unknown";  
  }  
:}  
  
terminal DO, LCOL, RCOL, LPAREN, RPAREN, PV, ATRIB, LT, INC, OUT;  
terminal String ID;  
terminal Integer NUM;  
  
non terminal programa, stmt, outStmt;  
  
programa ::= stmt:s  
    {:  
      codeC.append(s);  
    :};  
  
stmt ::= DO LCOL ID:var ATRIB NUM:init PV ID:var2 LT NUM:end PV ID:var3 INC RCOL outStmt:out  
    {:  
      RESULT = "for (int " + var + " = " + init + "; " + var2 + " < " + end + "; " + var3 + "++) {\n"  
             + out + "}\n";  
    :};  
  
outStmt ::= OUT LPAREN ID:var RPAREN  
    {:  
      RESULT = "    printf(\"%d\\n\", " + var + ");\n";  
    :};  
```  
  
3. `do.txt`:  
```txt  
do [num = 1; num < 10; num++ ]  
   out(num)  
  
do [num = 1; num < 10 num++]  
   out(num)  
```  
  
```  
# 1. Gerar Lexer  
jflex do.flex  
  
# 2. Gerar Parser  
java java_cup.Main do.cup  
  
# 3. Compilar tudo  
javac *.java  
  
# 4. Executar  
java parser do.txt  
```  
  
------------------------------------------------------------------------------------------  
  
## Exemplo 6: Analisador de HTML Básico  
  
**Enunciado**: Criar um analisador para um subconjunto de HTML que reconheça tags básicas e seus atributos.  
  
### Arquivos:  
  
1. `html.flex`:  
```lex  
import java_cup.runtime.Symbol;  
  
%%  
%public  
%class HtmlLexer  
%cup  
%line  
%column  
  
%{  
  
private Symbol symbol(int type) {  
    return new Symbol(type, yyline + 1, yycolumn + 1);  
}  
  
private Symbol symbol(int type, Object value) {  
    return new Symbol(type, yyline + 1, yycolumn + 1, value);  
}  
%}  
  
%state TAG  
  
TAG_OPEN = "<"  
TAG_CLOSE = ">"  
TAG_END_OPEN = "</"  
EQ = "="  
DQ_ATTR_VALUE = \"[^\"]*\"  
SQ_ATTR_VALUE = \'[^\']*\'  
WS = [ \t\r\n]+  
NAME = [a-zA-Z_:][a-zA-Z0-9_.:-]*  
  
%%  
  
<YYINITIAL> {  
    {WS}                    { /* ignora espaços fora das tags */ }  
    {TAG_END_OPEN}          { yybegin(TAG); return symbol(sym.TAG_END_OPEN, yytext()); }  
    {TAG_OPEN}              { yybegin(TAG); return symbol(sym.TAG_OPEN, yytext()); }  
    [^<]+                   { return symbol(sym.TEXT, yytext()); }  
}  
  
<TAG> {  
    {WS}                    { /* ignora espaços dentro da tag */ }  
    {NAME}                  { return symbol(sym.NAME, yytext()); }  
    {EQ}                    { return symbol(sym.EQ, yytext()); }  
    {DQ_ATTR_VALUE}         { return symbol(sym.ATTR_VALUE, yytext()); }  
    {SQ_ATTR_VALUE}         { return symbol(sym.ATTR_VALUE, yytext()); }  
    {TAG_CLOSE}             { yybegin(YYINITIAL); return symbol(sym.TAG_CLOSE, yytext()); }  
    .                       { return symbol(sym.ERROR, yytext()); }  
}  
```  
  
2. `html.cup`:  
```cup  
import java_cup.runtime.*;  
import java.io.*;  
  
parser code {:  
    public static void main(String[] args) throws Exception {  
        HtmlLexer lexer = new HtmlLexer(new java.io.FileReader(args[0]));  
        parser p = new parser(lexer);  
        p.parse();  
        System.out.println("Parse concluído com sucesso!");  
    }  
:}  
  
terminal TAG_OPEN, TAG_END_OPEN, TAG_CLOSE, EQ;  
terminal String NAME, ATTR_VALUE, TEXT;  
terminal ERROR;  
  
non terminal document, element_list, element, tag, attributes, attribute;  
  
start with document;  
  
document ::= element_list;  
  
element_list ::=  
      /* vazio */  
    | element_list element  
    ;  
  
element ::=  
      tag  
    | TEXT  
    ;  
  
tag ::=  
      TAG_OPEN NAME attributes TAG_CLOSE element_list TAG_END_OPEN NAME TAG_CLOSE  
    ;  
  
attributes ::=  
      /* vazio */  
    | attributes attribute  
    ;  
  
attribute ::=  
    NAME EQ ATTR_VALUE  
    ;  
  
```  
  
3. `html.txt`:  
```html  
<html>  
  <head>  
    <title>Exemplo</title>  
  </head>  
  <body class="conteudo" id='pagina'>  
    <h1>Bem-vindo!</h1>  
    <p>Este é um parágrafo com <a href="https://exemplo.com">link</a>.</p>  
  </body>  
</html>  
```  
  
```  
# 1. Gerar Lexer  
jflex html.flex  
  
# 2. Gerar Parser  
java java_cup.Main html.cup  
  
# 3. Compilar tudo  
javac *.java  
  
# 4. Executar  
java parser html.txt  
```  
  
------------------------------------------------------------------------------------------  
  
## Exemplo 7: Linguagem de Definição de Formas Geométricas  
  
**Enunciado**: Criar uma DSL para definir formas geométricas com propriedades como cor, posição e dimensões.  
  
### Arquivos:  
  
1. `shapes.flex`:  
```lex  
import java_cup.runtime.*;  
  
%%  
  
%class ShapesLexer  
%unicode  
%cup  
%line  
%column  
  
%{  
    private Symbol symbol(int type) {  
        return new Symbol(type, yyline, yycolumn);  
    }  
    private Symbol symbol(int type, Object value) {  
        return new Symbol(type, yyline, yycolumn, value);  
    }  
%}  
  
digit = [0-9]  
number = {digit}+(\.{digit}+)?  
color = #[0-9a-fA-F]{6}  
id = [a-zA-Z][a-zA-Z0-9]*  
whitespace = [ \t\n\r]  
  
%%  
  
"shape" { return symbol(sym.SHAPE); }  
"rectangle" { return symbol(sym.RECTANGLE); }  
"circle" { return symbol(sym.CIRCLE); }  
"triangle" { return symbol(sym.TRIANGLE); }  
"{" { return symbol(sym.LBRACE); }  
"}" { return symbol(sym.RBRACE); }  
":" { return symbol(sym.COLON); }  
"," { return symbol(sym.COMMA); }  
"width" { return symbol(sym.WIDTH); }  
"height" { return symbol(sym.HEIGHT); }  
"radius" { return symbol(sym.RADIUS); }  
"color" { return symbol(sym.COLOR); }  
"position" { return symbol(sym.POSITION); }  
{id} { return symbol(sym.ID, yytext()); }  
{number} { return symbol(sym.NUMBER, Double.parseDouble(yytext())); }  
{color} { return symbol(sym.COLORVAL, yytext()); }  
{whitespace} { /* ignore */ }  
  
<<EOF>> { return symbol(sym.EOF); }  
```  
  
2. `shapes.cup`:  
```cup  
import java_cup.runtime.*;  
  
parser code {:  
    public static void main(String[] args) throws Exception {  
        ShapesLexer lexer = new ShapesLexer(new java.io.FileReader(args[0]));  
        parser p = new parser(lexer);  
        p.parse();  
    }  
:}  
  
terminal SHAPE, RECTANGLE, CIRCLE, TRIANGLE, LBRACE, RBRACE, COLON, COMMA;  
terminal WIDTH, HEIGHT, RADIUS, COLOR, POSITION;  
terminal String ID, COLORVAL;  
terminal Double NUMBER;  
  
non terminal Shapes shapes, shape_def, shape_type, properties, property;  
  
shapes ::= shape_def  
         | shapes shape_def  
         ;  
  
shape_def ::= SHAPE ID COLON shape_type LBRACE properties RBRACE;  
  
shape_type ::= RECTANGLE  
             | CIRCLE  
             | TRIANGLE  
             ;  
  
properties ::= property  
             | properties COMMA property  
             ;  
  
property ::= WIDTH COLON NUMBER  
           | HEIGHT COLON NUMBER  
           | RADIUS COLON NUMBER  
           | COLOR COLON COLORVAL  
           | POSITION COLON NUMBER COMMA NUMBER  
;  
```  
  
3. `Shape.java`:  
  
```java  
public abstract class Shape {  
    protected String id;  
    protected String color;  
  
    public Shape(String id) {  
        this.id = id;  
    }  
  
    public String getId() { return id; }  
  
    public void setColor(String color) { this.color = color; }  
  
    @Override  
    public String toString() {  
        return getClass().getSimpleName() + "(" + id + ", color=" + color + ")";  
    }  
}  
```  
  
4. `Shapes.java`:  
  
```java  
import java.util.ArrayList;  
import java.util.List;  
  
public class Shapes {  
    private List<Shape> shapes = new ArrayList<>();  
  
    public void addShape(Shape shape) {  
        shapes.add(shape);  
    }  
  
    public List<Shape> getShapes() {  
        return shapes;  
    }  
  
    @Override  
    public String toString() {  
        return shapes.toString();  
    }  
}  
```  
  
5. `shapes.txt`:  
```  
shape rect1: rectangle {  
    width: 100,  
    height: 50,  
    color: #ff0000,  
    position: 10, 20  
}  
  
shape circle1: circle {  
    radius: 30,  
    color: #00ff00,  
    position: 50, 50  
}  
```  
  
```  
# 1. Gerar Lexer  
jflex shapes.flex  
  
# 2. Gerar Parser  
java java_cup.Main shapes.cup  
  
# 3. Compilar tudo  
javac *.java  
  
# 4. Executar  
java parser shapes.txt  
  
```  
  
------------------------------------------------------------------------------------------  
  
## Exemplo 8: Interpretador de Comandos de Robô  
  
**Enunciado**: Criar uma linguagem para controlar movimentos de um robô em um grid 2D.  
  
### Arquivos:  
  
1. `robot.flex`:  
```lex  
import java_cup.runtime.*;  
  
%%  
  
%class RobotLexer  
%unicode  
%cup  
%line  
%column  
  
%{  
    private Symbol symbol(int type) {  
        return new Symbol(type, yyline, yycolumn);  
    }  
    private Symbol symbol(int type, Object value) {  
        return new Symbol(type, yyline, yycolumn, value);  
    }  
%}  
  
digit = [0-9]  
number = {digit}+  
whitespace = [ \t\n\r]  
  
%%  
  
"MOVE" { return symbol(sym.MOVE); }  
"LEFT" { return symbol(sym.LEFT); }  
"RIGHT" { return symbol(sym.RIGHT); }  
"LOOP" { return symbol(sym.LOOP); }  
"TIMES" { return symbol(sym.TIMES); }  
"{" { return symbol(sym.LBRACE); }  
"}" { return symbol(sym.RBRACE); }  
{number} { return symbol(sym.NUMBER, Integer.parseInt(yytext())); }  
{whitespace} { /* ignore */ }  
  
<<EOF>> { return symbol(sym.EOF); }  
```  
  
2. `robot.cup`:  
```cup  
import java_cup.runtime.*;  
  
parser code {:  
    public static void main(String[] args) throws Exception {  
        RobotLexer lexer = new RobotLexer(new java.io.FileReader(args[0]));  
        parser p = new parser(lexer);  
        p.parse();  
    }  
:}  
  
terminal MOVE, LEFT, RIGHT, LOOP, TIMES, LBRACE, RBRACE;  
terminal Integer NUMBER;  
  
non terminal program, command, commands, loop;  
  
program ::= commands;  
  
commands ::= command  
           | commands command  
           ;  
  
command ::= MOVE  
          | LEFT  
          | RIGHT  
          | loop  
          ;  
  
loop ::= LOOP NUMBER TIMES LBRACE commands RBRACE;  
```  
  
3. `test_robot.txt`:  
```  
MOVE  
LEFT  
LOOP 3 TIMES {  
    MOVE  
    RIGHT  
    MOVE  
}  
RIGHT  
MOVE  
```  
  
```  
# 1. Gerar Lexer  
jflex robot.flex  
  
# 2. Gerar Parser  
java java_cup.Main robot.cup  
  
# 3. Compilar tudo  
javac *.java  
  
# 4. Executar  
java parser robot.txt  
  
```  
  
------------------------------------------------------------------------------------------  
  
## Exemplo 9: Linguagem de Marcação Personalizada  
  
**Enunciado**: Criar uma linguagem de marcação simples para formatar texto com estilos básicos.  
  
### Arquivos:  
  
1. `markup.flex`:  
```lex  
import java_cup.runtime.*;  
  
%%  
  
%class MarkupLexer  
%unicode  
%cup  
%line  
%column  
  
%{  
    private Symbol symbol(int type) {  
        return new Symbol(type, yyline, yycolumn);  
    }  
    private Symbol symbol(int type, Object value) {  
        return new Symbol(type, yyline, yycolumn, value);  
    }  
%}  
  
text = [^\\[*_`\n]+  
escape = \\.  
whitespace = [ \t\n\r]  
  
%%  
  
"*" { return symbol(sym.ASTERISK); }  
"_" { return symbol(sym.UNDERSCORE); }  
"`" { return symbol(sym.BACKTICK); }  
"[" { return symbol(sym.LBRACKET); }  
"]" { return symbol(sym.RBRACKET); }  
"(" { return symbol(sym.LPAREN); }  
")" { return symbol(sym.RPAREN); }  
{text} { return symbol(sym.TEXT, yytext()); }  
{escape} { return symbol(sym.TEXT, yytext().substring(1)); }  
{whitespace} { /* ignore */ }  
  
<<EOF>> { return symbol(sym.EOF); }  
```  
  
2. `markup.cup`:  
```cup  
import java_cup.runtime.*;  
  
parser code {:  
    public static void main(String[] args) throws Exception {  
        MarkupLexer lexer = new MarkupLexer(new java.io.FileReader(args[0]));  
        Parser parser = new Parser(lexer);  
        parser.parse();  
    }  
:}  
  
terminal ASTERISK, UNDERSCORE, BACKTICK, LBRACKET, RBRACKET, LPAREN, RPAREN, EOF;  
terminal String TEXT;  
  
non terminal Document document, element, text, style, link;  
  
document ::= element  
           | document element  
           ;  
  
element ::= text  
          | style  
          | link  
          ;  
  
text ::= TEXT  
       | text TEXT  
       ;  
  
style ::= ASTERISK element ASTERISK  
        | UNDERSCORE element UNDERSCORE  
        | BACKTICK element BACKTICK  
        ;  
  
link ::= LBRACKET text RBRACKET LPAREN text RPAREN;  
```  
  
3. `test_markup.txt`:  
```  
Este é um *texto em negrito* com _itálico_ e um `código`.  
[Link](para um site)  
```  
  
------------------------------------------------------------------------------------------  
  
## Exemplo 10: Analisador de Expressões Booleanas  
  
**Enunciado**: Criar um analisador para expressões booleanas com operadores AND, OR, NOT e parênteses.  
  
### Arquivos:  
  
1. `bool.flex`:  
```lex  
import java_cup.runtime.*;  
  
%%  
  
%class BoolLexer  
%unicode  
%cup  
%line  
%column  
  
%{  
    private Symbol symbol(int type) {  
        return new Symbol(type, yyline, yycolumn);  
    }  
    private Symbol symbol(int type, Object value) {  
        return new Symbol(type, yyline, yycolumn, value);  
    }  
%}  
  
letter = [a-zA-Z]  
id = {letter}+  
whitespace = [ \t\n\r]  
  
%%  
  
"AND" { return symbol(sym.AND); }  
"OR" { return symbol(sym.OR); }  
"NOT" { return symbol(sym.NOT); }  
"(" { return symbol(sym.LPAREN); }  
")" { return symbol(sym.RPAREN); }  
"true" { return symbol(sym.TRUE); }  
"false" { return symbol(sym.FALSE); }  
{id} { return symbol(sym.ID, yytext()); }  
{whitespace} { /* ignore */ }  
  
<<EOF>> { return symbol(sym.EOF); }  
```  
  
2. `bool.cup`:  
```cup  
import java_cup.runtime.*;  
  
parser code {:  
    public static void main(String[] args) throws Exception {  
        BoolLexer lexer = new BoolLexer(new java.io.FileReader(args[0]));  
        Parser parser = new Parser(lexer);  
        parser.parse();  
    }  
:}  
  
terminal AND, OR, NOT, LPAREN, RPAREN, TRUE, FALSE, EOF;  
terminal String ID;  
  
non terminal BooleanExpression expr, term, factor;  
  
expr ::= term  
       | expr OR term  
       ;  
  
term ::= factor  
       | term AND factor  
       ;  
  
factor ::= NOT factor  
         | LPAREN expr RPAREN  
         | TRUE  
         | FALSE  
         | ID  
         ;  
```  
  
3. `test_bool.txt`:  
```  
(NOT a OR b) AND (c OR NOT false)  
```  
  
------------------------------------------------------------------------------------------  
  
## Exemplo 11: Linguagem de Definição de Autômatos Finitos  
  
**Enunciado**: Criar uma DSL para definir autômatos finitos determinísticos.  
  
### Arquivos:  
  
1. `automata.flex`:  
```lex  
import java_cup.runtime.*;  
  
%%  
  
%class AutomataLexer  
%unicode  
%cup  
%line  
%column  
  
%{  
    private Symbol symbol(int type) {  
        return new Symbol(type, yyline, yycolumn);  
    }  
    private Symbol symbol(int type, Object value) {  
        return new Symbol(type, yyline, yycolumn, value);  
    }  
%}  
  
letter = [a-zA-Z]  
digit = [0-9]  
id = {letter}({letter}|{digit})*  
number = {digit}+  
whitespace = [ \t\n\r]  
  
%%  
  
"automaton" { return symbol(sym.AUTOMATON); }  
"states" { return symbol(sym.STATES); }  
"alphabet" { return symbol(sym.ALPHABET); }  
"transitions" { return symbol(sym.TRANSITIONS); }  
"initial" { return symbol(sym.INITIAL); }  
"final" { return symbol(sym.FINAL); }  
":" { return symbol(sym.COLON); }  
";" { return symbol(sym.SEMI); }  
"->" { return symbol(sym.ARROW); }  
"," { return symbol(sym.COMMA); }  
"{" { return symbol(sym.LBRACE); }  
"}" { return symbol(sym.RBRACE); }  
{id} { return symbol(sym.ID, yytext()); }  
{number} { return symbol(sym.NUMBER, new Integer(yytext())); }  
{whitespace} { /* ignore */ }  
  
<<EOF>> { return symbol(sym.EOF); }  
```  
  
2. `automata.cup`:  
```cup  
import java_cup.runtime.*;  
  
parser code {:  
    public static void main(String[] args) throws Exception {  
        AutomataLexer lexer = new AutomataLexer(new java.io.FileReader(args[0]));  
        Parser parser = new Parser(lexer);  
        parser.parse();  
    }  
:}  
  
terminal AUTOMATON, STATES, ALPHABET, TRANSITIONS, INITIAL, FINAL;  
terminal COLON, SEMI, ARROW, COMMA, LBRACE, RBRACE, EOF;  
terminal String ID;  
terminal Integer NUMBER;  
  
non terminal Automaton automaton, states, alphabet, transitions, transition, state_list, symbol_list;  
  
automaton ::= AUTOMATON ID LBRACE states alphabet initial final transitions RBRACE;  
  
states ::= STATES COLON state_list SEMI;  
  
alphabet ::= ALPHABET COLON symbol_list SEMI;  
  
initial ::= INITIAL COLON ID SEMI;  
  
final ::= FINAL COLON state_list SEMI;  
  
transitions ::= TRANSITIONS COLON LBRACE transition_list RBRACE SEMI;  
  
transition_list ::= transition  
                  | transition_list transition  
                  ;  
  
transition ::= ID COMMA ID ARROW ID SEMI;  
  
state_list ::= ID  
             | state_list COMMA ID  
             ;  
  
symbol_list ::= ID  
              | symbol_list COMMA ID  
              ;  
```  
  
3. `test_automata.txt`:  
```  
automaton DFA1 {  
    states: q0, q1, q2;  
    alphabet: a, b;  
    initial: q0;  
    final: q2;  
    transitions: {  
        q0, a -> q1;  
        q0, b -> q0;  
        q1, a -> q1;  
        q1, b -> q2;  
        q2, a -> q2;  
        q2, b -> q2;  
    }  
}  
```  
  
------------------------------------------------------------------------------------------  
  
## Exemplo 12: Processador de Templates Simples  
  
**Enunciado**: Criar uma linguagem de templates com substituição de variáveis e estruturas condicionais básicas.  
  
### Arquivos:  
  
1. `template.flex`:  
```lex  
import java_cup.runtime.*;  
  
%%  
  
%class TemplateLexer  
%unicode  
%cup  
%line  
%column  
  
%{  
    private Symbol symbol(int type) {  
        return new Symbol(type, yyline, yycolumn);  
    }  
    private Symbol symbol(int type, Object value) {  
        return new Symbol(type, yyline, yycolumn, value);  
    }  
%}  
  
text = [^{}]+  
var = [a-zA-Z_][a-zA-Z0-9_]*  
whitespace = [ \t\n\r]  
  
%%  
  
"{{" { return symbol(sym.LDBRACE); }  
"}}" { return symbol(sym.RDBRACE); }  
"{%" { return symbol(sym.LTBRACE); }  
"%}" { return symbol(sym.RTBRACE); }  
"if" { return symbol(sym.IF); }  
"endif" { return symbol(sym.ENDIF); }  
{var} { return symbol(sym.VAR, yytext()); }  
{text} { return symbol(sym.TEXT, yytext()); }  
{whitespace} { /* ignore */ }  
  
<<EOF>> { return symbol(sym.EOF); }  
```  
  
2. `template.cup`:  
```cup  
import java_cup.runtime.*;  
  
parser code {:  
    public static void main(String[] args) throws Exception {  
        TemplateLexer lexer = new TemplateLexer(new java.io.FileReader(args[0]));  
        Parser parser = new Parser(lexer);  
        parser.parse();  
    }  
:}  
  
terminal LDBRACE, RDBRACE, LTBRACE, RTBRACE, IF, ENDIF, EOF;  
terminal String VAR, TEXT;  
  
non terminal Template template, part, var_block, if_block;  
  
template ::= part  
           | template part  
           ;  
  
part ::= TEXT  
       | var_block  
       | if_block  
       ;  
  
var_block ::= LDBRACE VAR RDBRACE;  
  
if_block ::= LTBRACE IF VAR RTBRACE template LTBRACE ENDIF RTBRACE;  
```  
  
3. `test_template.txt`:  
```  
Olá {{nome}},  
  
{% if premium %}  
Obrigado por ser um assinante premium!  
{% endif %}  
  
Seu saldo é: {{saldo}}  
```  
  
Estes exemplos cobrem uma variedade de aplicações para JFlex e CUP, desde linguagens simples até DSLs mais complexas. Cada exemplo inclui:  
  
1. Arquivo .flex com definições léxicas  
2. Arquivo .cup com gramática e ações semânticas  
3. Arquivo de teste correspondente  
  
Para compilar e executar qualquer um desses exemplos, você precisará:  
1. Gerar o analisador léxico com JFlex  
2. Gerar o analisador sintático com CUP  
3. Compilar os arquivos Java resultantes  
4. Executar com o arquivo de teste como entrada  
  
Deseja que eu detalhe algum aspecto específico de algum dos exemplos ou explique como configurar o ambiente para compilação e execução?  