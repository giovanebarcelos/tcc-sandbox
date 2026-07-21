# Compilador com JFlex e JCup  
  
## Descrição  
  
Este compilador implementa uma linguagem de programação simples com:  
- Declaração de variáveis (int, float)  
- Operações aritméticas (+, -, *, /)  
- Estruturas de controle (if-then-else, while-do)  
- Entrada/saída (read, write)  
- Operadores relacionais (==, !=, <, >, <=, >=)  
  
## Estrutura do Projeto  
  
```  
Lexer.flex          - Analisador léxico (JFlex)  
Parser.cup          - Analisador sintático (JCup)  
Compiler.java       - Programa principal  
exemplo1.txt        - Exemplo de programa correto 1  
exemplo2.txt        - Exemplo de programa correto 2  
exemplo_erro.txt    - Exemplo com erros para teste  
```  
  
## Instalação e Compilação  
  
### Pré-requisitos  
- Java JDK 8 ou superior  
- JFlex (https://jflex.de/)  
- JCup (http://www2.cs.tum.edu/projects/cup/)  
  
### Passos para compilar:  
  
1. **Gerar o analisador léxico:**  
```bash  
jflex Lexer.flex  
```  
  
2. **Gerar o analisador sintático:**  
```bash  
java -jar java-cup-11b.jar Parser.cup  
```  
  
3. **Compilar os arquivos Java:**  
```bash  
javac -cp java-cup-11b-runtime.jar:. Lexer.java sym.java parser.java Compiler.java  
```  
  
## Execução  
  
Execute o compilador com um arquivo de entrada:  
  
```bash  
java -cp java-cup-11b-runtime.jar:. Compiler exemplo1.txt  
```  
  
### Exemplos de uso:  
  
**Exemplo 1 - Programa correto:**  
```bash  
java -cp java-cup-11b-runtime.jar:. Compiler exemplo1.txt  
```  
Saída esperada:  
```  
============================================================  
COMPILANDO: exemplo1.txt  
============================================================  
Programa compilado com sucesso!  
============================================================  
```  
  
**Exemplo 2 - Programa correto:**  
```bash  
java -cp java-cup-11b-runtime.jar:. Compiler exemplo2.txt  
```  
  
**Exemplo 3 - Programa com erros:**  
```bash  
java -cp java-cup-11b-runtime.jar:. Compiler exemplo_erro.txt  
```  
Saída esperada:  
```  
============================================================  
COMPILANDO: exemplo_erro.txt  
============================================================  
ERRO LÉXICO na linha 4, coluna 7: caractere inválido '@'  
ERRO SINTÁTICO na linha 8, coluna 11: token inesperado 'write' - ...  
============================================================  
```  
  
## Tratamento de Erros  
  
O compilador detecta e reporta:  
  
### Erros Léxicos:  
- Caracteres inválidos  
- Mostra: linha, coluna e o caractere que causou o erro  
  
### Erros Sintáticos:  
- Tokens inesperados  
- Falta de pontuação (ponto e vírgula, parênteses, etc.)  
- Estruturas incompletas  
- Mostra: linha, coluna e o token que causou o erro  
  
## Gramática da Linguagem  
  
### Estrutura do programa:  
```  
program <nome>;  
<declarações>  
begin  
    <comandos>  
end  
```  
  
### Declarações:  
```  
int x, y, z;  
float valor;  
```  
  
### Comandos:  
```  
x = 5;                          // Atribuição  
read(x);                        // Entrada  
write(x + 5);                   // Saída  
if x > 0 then write(x);        // Condicional  
while x < 10 do x = x + 1;     // Repetição  
```  
  
### Operadores:  
- Aritméticos: `+`, `-`, `*`, `/`  
- Relacionais: `==`, `!=`, `<`, `>`, `<=`, `>=`  
- Atribuição: `=`  
  
## Exemplos de Programas Válidos  
  
### Calculadora simples:  
```  
program calc;  
int a, b, soma;  
begin  
    read(a);  
    read(b);  
    soma = a + b;  
    write(soma);  
end  
```  
  
### Fatorial:  
```  
program fatorial;  
int n, fat, i;  
begin  
    read(n);  
    fat = 1;  
    i = 1;  
    while i <= n do {  
        fat = fat * i;  
        i = i + 1;  
    }  
    write(fat);  
end  
```  
  
## Observações  
  
- Comentários de linha começam com `//`  
- Comandos devem terminar com ponto e vírgula  
- Blocos de comandos podem ser agrupados com `{ }`  
- Identificadores devem começar com letra  
- A palavra-chave `program` é obrigatória  
