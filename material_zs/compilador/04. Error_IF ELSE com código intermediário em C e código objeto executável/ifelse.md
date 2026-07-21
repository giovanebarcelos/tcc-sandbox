Claro! A seguir, apresento um **enunciado didático completo** e uma **solução funcional** usando **JFlex** e **JCup**. O objetivo é criar um analisador léxico e sintático que reconheça estruturas `if-else`, com **verificação de erros de sintaxe** e **geração de código intermediário** simples (tradução para tripletas ou instruções tipo "goto").  
  
---  
  
## ✅ **Enunciado**  
  
Implemente um analisador léxico e sintático para uma linguagem imperativa simples que contenha:  
  
* A estrutura condicional `if-else`.  
* Expressões booleanas com operadores relacionais (`<`, `>`, `==`).  
* Blocos `if` e `else` contendo apenas uma instrução de atribuição do tipo `id = número;`.  
* Notificação clara de erros de sintaxe com posição (linha e coluna).  
* Geração de código intermediário (por exemplo, em forma de instruções tipo `IF`, `GOTO`, `LABEL`, `:=`).  
  
---  
  
## 🎯 **Exemplo de código de entrada válido**  
  
```c  
if (x > 0)  
    y = 1;  
else  
    y = 2;  
```  
  
---  
  
## 🧠 **Saída esperada (código intermediário)**  
  
```plaintext  
IF x > 0 GOTO L1  
GOTO L2  
LABEL L1  
y := 1  
GOTO L3  
LABEL L2  
y := 2  
LABEL L3  
```  
  
---  
  
## 📁 Estrutura dos arquivos  
  
* `Lexer.flex`: definições léxicas com JFlex.  
* `Parser.cup`: regras sintáticas e ações semânticas com JCup.  
* `Main.java`: ponto de entrada.  
  
---  
  
## 🧾 **Lexer.flex**  
  
```lex  
%%  
  
%class Lexer  
%unicode  
%cup  
%line  
%column  
  
%{  
  
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
.           { throw new Error("Caractere inesperado: " + yytext() + " na linha " + (yyline+1) + ", coluna " + (yycolumn+1)); }  
```  
  
---  
  
## 📘 **Parser.cup**  
  
```cup  
parser code {:  
    import java.util.*;  
    String novaLabel() {  
        return "L" + (++labelCount);  
    }  
  
    int labelCount = 0;  
:};  
  
terminal IF, ELSE, LPAREN, RPAREN, PV, ATRIB;  
terminal EQ, LT, GT;  
terminal String ID;  
terminal Integer NUM;  
  
non terminal programa;  
non terminal stmt;  
non terminal cond;  
non terminal bloco;  
non terminal expr;  
  
non terminal List<String> codigo;  
  
start with programa;  
  
programa ::= stmt:c {:  
    for (String linha : c) System.out.println(linha);  
:};  
  
stmt ::=  
    IF LPAREN cond:cond RPAREN bloco:b1 ELSE bloco:b2 {:  
        String l1 = novaLabel();  
        String l2 = novaLabel();  
        String l3 = novaLabel();  
        List<String> cod = new ArrayList<>();  
        cod.add("IF " + cond.get(0) + " GOTO " + l1);  
        cod.add("GOTO " + l2);  
        cod.add("LABEL " + l1);  
        cod.addAll(b1);  
        cod.add("GOTO " + l3);  
        cod.add("LABEL " + l2);  
        cod.addAll(b2);  
        cod.add("LABEL " + l3);  
        RESULT = cod;  
    :};  
  
cond ::= ID:i op:op NUM:n {:  
    List<String> cond = new ArrayList<>();  
    cond.add(i + " " + op + " " + n);  
    RESULT = cond;  
:};  
  
op ::= EQ {: RESULT = "=="; :}  
    | LT {: RESULT = "<"; :}  
    | GT {: RESULT = ">"; :};  
  
bloco ::= ID:i ATRIB NUM:n PV {:  
    List<String> l = new ArrayList<>();  
    l.add(i + " := " + n);  
    RESULT = l;  
:};  
```  
  
---  
  
## 🏁 **Main.java**  
  
```java  
import java.io.*;  
  
public class Main {  
    public static void main(String[] args) throws Exception {  
        Reader reader = new FileReader("entrada.txt");  
        Lexer lexer = new Lexer(reader);  
        Parser parser = new Parser(lexer);  
        parser.parse();  
    }  
}  
```  
  
---  
  
## 📄 **entrada.txt (exemplo)**  
  
```c  
if (x > 0)  
    y = 1;  
else  
    y = 2;  
```  
  
---  
  
## 🛠️ **Compilação e Execução**  
  
1. Gere o scanner:  
  
```bash  
jflex Lexer.flex  
```  
  
2. Gere o parser:  
  
```bash  
java -jar java-cup-11b.jar -parser Parser Parser.cup  
```  
  
3. Compile tudo:  
  
```bash  
javac *.java  
```  
  
4. Execute:  
  
```bash  
java Main  
```  
  
---  
  
## 🧪 **Exemplo de erro léxico**  
  
```c  
if (x > 0) {  
    y = 1;  
}  
```  
  
Saída esperada:  
  
```  
Caractere inesperado: { na linha 1, coluna 12  
```  
  
---  
  
Se quiser, posso te entregar um `.zip` com todos esses arquivos ou gerar variações com `while`, `for` ou expressões mais complexas. Deseja expandir esse analisador?  
