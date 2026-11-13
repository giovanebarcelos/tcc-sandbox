# Enunciado 26 — Interpretador da mini-linguagem (PP3)

**Teoria da Computação e Compiladores — 6M (Digital) · Prof. Giovane Barcelos**

| | |
|---|---|
| **Nível** | ⭐⭐⭐ (intermediário) |
| **Aulas relacionadas** | 21 (Projeto Parcial 3) |
| **Ferramentas** | ANTLR 4 **ou** JFlex + JCup (implementar com as duas vale como desafio extra) |
| **Testes fornecidos** | 2 válido(s) (`ok_*`) e 5 inválido(s) (`erro_*`), na pasta [`testes/`](testes/) |

> Leia antes as **regras gerais** em [`../README.md`](../README.md): formato das mensagens de erro, códigos de
> saída, como usar o `testar.sh`, o que entregar e como será avaliado.

---

## 1. Objetivo

Um **interpretador** que percorre a AST de uma mini-linguagem com variáveis, `if / else if / else`,
`while`, `for`, `print` e valores numéricos, textuais e booleanos. Há **escopo de bloco** e os erros de tipo
são detectados **durante a execução**.

## 2. Análise léxica

Palavras-chave `var print if else while for to and or not true false`; `ID`; `NUM` (inteiro ou real);
`STRING` entre aspas duplas (escapes `\n \" \\`); `= + - * / % < > <= >= == != ( ) { } , ;`; comentários `# ...`.

## 3. Análise sintática

```ebnf
programa ::= { comando } EOF
comando  ::= "var" ID "=" expr ";" | ID "=" expr ";" | "print" "(" [ expr { "," expr } ] ")" ";"
           | se | "while" "(" expr ")" bloco | "for" ID "=" expr "to" expr bloco | bloco
se       ::= "if" "(" expr ")" bloco [ "else" ( bloco | se ) ]
bloco    ::= "{" { comando } "}"
expr     ::= ("-" | "not") expr | expr op expr | NUM | STRING | "true" | "false" | ID | "(" expr ")"
```
Precedência: unários; `* / %`; `+ -`; comparações; `and`; `or`.

## 4. Análise semântica e regras da linguagem

- `var` declara no escopo atual (repetir no mesmo escopo é erro); usar variável não declarada é erro.
- Cada bloco abre um escopo novo (pilha de tabelas de símbolos).
- `+` soma números ou concatena textos; os demais operadores aritméticos exigem números.
- Condições exigem booleanos; `and`/`or` com **curto-circuito**.
- `for i = a to b` repete com `i` de `a` até `b` (inclusive), num escopo próprio.
- `print(a, b, ...)` imprime os valores separados por espaço; números inteiros sem `.0`.

## 5. Saída

O que os comandos `print` imprimirem. Veja os casos de teste (Fibonacci, primos e FizzBuzz).

## 6. Dicas

- Use um tipo de valor dinâmico (`Object` com `Double`, `String`, `Boolean`) e verifique o tipo em cada operação.

## 7. Casos de teste

O `testar.sh` compara a saída padrão inteira, linha a linha. Os testes `erro_*` passam se o compilador terminar com código de saída
diferente de 0 e escrever uma mensagem na saída de erro; o texto da mensagem de referência é um **modelo**
(o tipo do erro e a posição `[linha:coluna]` devem bater; a redação pode variar).

```bash
../testar.sh . <comando que executa o seu compilador>
# ex.: ../testar.sh . java -cp "build/classes:$ANTLR_JAR" Main
```

### 7.1 Programas válidos

#### `ok_1.txt`

**Entrada:**

```text
# Fibonacci, primos e FizzBuzz
var a = 0;
var b = 1;
var n = 0;
while (n < 10) {
    print("fib(" + n + ") =", a);
    var t = a + b;
    a = b;
    b = t;
    n = n + 1;
}
var primos = "";
for i = 2 to 30 {
    var ehPrimo = true;
    var d = 2;
    while (d * d <= i and ehPrimo) {
        if (i % d == 0) { ehPrimo = false; }
        d = d + 1;
    }
    if (ehPrimo) { primos = primos + i + " "; }
}
print("primos até 30:", primos);
```

**Saída esperada:**

```text
fib(0) = 0
fib(1) = 1
fib(2) = 1
fib(3) = 2
fib(4) = 3
fib(5) = 5
fib(6) = 8
fib(7) = 13
fib(8) = 21
fib(9) = 34
primos até 30: 2 3 5 7 11 13 17 19 23 29
```

#### `ok_2.txt`

**Entrada:**

```text
for i = 1 to 15 {
    if (i % 15 == 0) { print("FizzBuzz"); }
    else if (i % 3 == 0) { print("Fizz"); }
    else if (i % 5 == 0) { print("Buzz"); }
    else { print(i); }
}
var media = (7.5 + 8 + 9.25) / 3;
print("média:", media, "aprovado?", media >= 7 and not (media > 10));
```

**Saída esperada:**

```text
1
2
Fizz
4
Buzz
Fizz
7
8
Fizz
Buzz
11
Fizz
13
14
FizzBuzz
média: 8.25 aprovado? true
```


### 7.2 Programas com erro

#### `erro_1.txt`

```text
var x = 1;
if (x > 0) print(x);
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro sintático [2:12]: token inesperado 'print'
```

#### `erro_2.txt`

```text
var x = 1;
{ var y = 2; }
print(y);
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro semântico [3:7]: variável 'y' não declarada
```

#### `erro_3.txt`

```text
var x = "texto";
print(x * 2);
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro de execução [2:7]: esperado número, encontrado texto (texto)
```

#### `erro_4.txt`

```text
var x = 1;
var x = 2;
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro semântico [2:5]: variável 'x' já declarada neste escopo
```

#### `erro_5.txt`

```text
var x = 10;
while (x) { x = x - 1; }
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro de execução [2:8]: esperado booleano, encontrado número (10)
```


Escreva também **pelo menos 2 testes `ok_` e 2 testes `erro_` seus**, cobrindo casos que estes não cobrem.

## 8. Desafios extras (opcionais)

- Funções (veja o enunciado 27) e listas `[1, 2, 3]` com `for x in lista`.

## 9. Critérios de avaliação

| Critério | Peso |
|---|---|
| Análise léxica (tokens, posições, erros léxicos) | 20% |
| Análise sintática (gramática sem conflitos / avisos, precedência, erros sintáticos) | 25% |
| Semântica e back-end (regras da seção 4, saída da seção 5) | 35% |
| Mensagens de erro (tipo, `[linha:coluna]`, clareza) | 10% |
| Testes próprios e README (como compilar, executar e testar) | 10% |
