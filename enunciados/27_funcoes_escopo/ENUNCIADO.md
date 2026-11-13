# Enunciado 27 — Funções, escopo e pilha de chamadas

**Teoria da Computação e Compiladores — 6M (Digital) · Prof. Giovane Barcelos**

| | |
|---|---|
| **Nível** | ⭐⭐⭐⭐ (avançado) |
| **Aulas relacionadas** | 22 (escopo) e 27 |
| **Ferramentas** | ANTLR 4 **ou** JFlex + JCup (implementar com as duas vale como desafio extra) |
| **Testes fornecidos** | 2 válido(s) (`ok_*`) e 6 inválido(s) (`erro_*`), na pasta [`testes/`](testes/) |

> Leia antes as **regras gerais** em [`../README.md`](../README.md): formato das mensagens de erro, códigos de
> saída, como usar o `testar.sh`, o que entregar e como será avaliado.

---

## 1. Objetivo

Acrescentar **funções** com parâmetros, recursão (inclusive mútua), variáveis locais e globais e blocos
que **sombreiam** nomes. O trabalho tem duas fases: **análise estática** (antes de executar) e **execução** com
uma **pilha de registros de ativação**.

## 2. Análise léxica

`func var if else while return print`, `ID`, `NUM` (inteiro), `STRING`, operadores e pontuação como no
enunciado 24, e `,`; comentários `// ...`.

## 3. Análise sintática

```ebnf
programa ::= { funcao | comando } EOF
funcao   ::= "func" ID "(" [ ID { "," ID } ] ")" bloco
bloco    ::= "{" { comando } "}"
comando  ::= "var" ID "=" expr ";" | ID "=" expr ";" | "if" "(" expr ")" bloco [ "else" bloco ]
           | "while" "(" expr ")" bloco | "return" expr ";" | "print" "(" [ expr { "," expr } ] ")" ";"
           | chamada ";" | bloco
chamada  ::= ID "(" [ expr { "," expr } ] ")"
expr     ::= ("-" | "!") expr | expr op expr | chamada | NUM | STRING | ID | "(" expr ")"
```

## 4. Análise semântica e regras da linguagem

**Análise estática** (antes de executar qualquer coisa):
- função inexistente; número de argumentos errado; parâmetro repetido; `return` fora de função;
- variável fora de escopo. Funções enxergam seus parâmetros, suas locais e as **globais** declaradas antes.

**Execução**:
- cada chamada empilha um registro de ativação; mostre a **profundidade máxima** atingida;
- recursão sem fim deve ser detectada como **estouro da pilha** (erro de execução), sem derrubar o interpretador.

## 5. Saída

A tabela de funções, a confirmação da análise, a saída do programa e a profundidade máxima da pilha (veja os testes).

## 6. Dicas

- Duas passadas: a primeira só registra as funções (permite chamar uma função definida mais abaixo).

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
func fat(n) {
    if (n <= 1) { return 1; }
    return n * fat(n - 1);
}
func fib(n) {
    if (n < 2) { return n; }
    return fib(n - 1) + fib(n - 2);
}
func mdc(a, b) {
    while (b != 0) { var r = a % b; a = b; b = r; }
    return a;
}
print("5! =", fat(5), " fib(15) =", fib(15), " mdc(48,18) =", mdc(48, 18));
```

**Saída esperada:**

```text
Tabela de funções:
  fat(n)  [linha 1]
  fib(n)  [linha 5]
  mdc(a, b)  [linha 9]
Análise semântica: OK. Execução:
5! = 120  fib(15) = 610  mdc(48,18) = 6
Profundidade máxima da pilha de chamadas: 15
```

#### `ok_2.txt`

**Entrada:**

```text
// escopo: global x, parâmetro x e local x são variáveis diferentes
var x = 10;
func mostra() { print("x global =", x); }
func sombra(x) {
    print("parâmetro x =", x);
    { var x = 99; print("x do bloco =", x); }
    print("parâmetro x de novo =", x);
    return x * 2;
}
func incrementa() { x = x + 1; }      // altera a global
mostra();
print("sombra(3) devolveu", sombra(3));
incrementa();
incrementa();
mostra();
func par(n) { if (n == 0) { return 1; } return impar(n - 1); }   // recursão mútua
func impar(n) { if (n == 0) { return 0; } return par(n - 1); }
print("par(10)?", par(10), " impar(7)?", impar(7));
```

**Saída esperada:**

```text
Tabela de funções:
  mostra()  [linha 3]
  sombra(x)  [linha 4]
  incrementa()  [linha 10]
  par(n)  [linha 16]
  impar(n)  [linha 17]
Análise semântica: OK. Execução:
x global = 10
parâmetro x = 3
x do bloco = 99
parâmetro x de novo = 3
sombra(3) devolveu 6
x global = 12
par(10)? 1  impar(7)? 1
Profundidade máxima da pilha de chamadas: 11
```


### 7.2 Programas com erro

#### `erro_1.txt`

```text
func f(a) { return a; }
print(f(1, 2));
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro semântico [2:7]: 'f' espera 1 argumento(s), recebeu 2
```

#### `erro_2.txt`

```text
print(g(1));
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro semântico [1:7]: função 'g' não definida
```

#### `erro_3.txt`

```text
func f() { return y; }
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro semântico [1:19]: variável 'y' não declarada neste escopo
```

#### `erro_4.txt`

```text
var a = 1;
return a;
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro semântico [2:1]: 'return' fora de uma função
```

#### `erro_5.txt`

```text
func f(n) { return f(n + 1); }
print(f(0));
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro de execução [1:20]: estouro da pilha de chamadas (recursão sem fim?) em 'f'
```

#### `erro_6.txt`

```text
func f(a, a) { return a; }
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro semântico [1:11]: parâmetro 'a' repetido
```


Escreva também **pelo menos 2 testes `ok_` e 2 testes `erro_` seus**, cobrindo casos que estes não cobrem.

## 8. Desafios extras (opcionais)

- Funções como valores (passar uma função como argumento).
- Mostrar o conteúdo da pilha no momento do erro (*stack trace*).

## 9. Critérios de avaliação

| Critério | Peso |
|---|---|
| Análise léxica (tokens, posições, erros léxicos) | 20% |
| Análise sintática (gramática sem conflitos / avisos, precedência, erros sintáticos) | 25% |
| Semântica e back-end (regras da seção 4, saída da seção 5) | 35% |
| Mensagens de erro (tipo, `[linha:coluna]`, clareza) | 10% |
| Testes próprios e README (como compilar, executar e testar) | 10% |
