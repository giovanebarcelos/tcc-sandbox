# Enunciado 31 — Mini-compilador Lingo → assembly x86-64

**Teoria da Computação e Compiladores — 6M (Digital) · Prof. Giovane Barcelos**

| | |
|---|---|
| **Nível** | ⭐⭐⭐⭐⭐ (projeto completo) |
| **Aulas relacionadas** | 25 (geração de código: Slides 12–14) |
| **Ferramentas** | ANTLR 4 **ou** JFlex + JCup (implementar com as duas vale como desafio extra) |
| **Testes fornecidos** | 3 válido(s) (`ok_*`) e 6 inválido(s) (`erro_*`), na pasta [`testes/`](testes/) |

> Leia antes as **regras gerais** em [`../README.md`](../README.md): formato das mensagens de erro, códigos de
> saída, como usar o `testar.sh`, o que entregar e como será avaliado.

---

## 1. Objetivo

A mesma linguagem **Lingo** do enunciado 30, mas o back-end gera **assembly x86-64** (sintaxe AT&T do
montador GNU `as`) em vez de C. O `gcc` só **monta e liga** o arquivo `.s` (`gcc -o prog prog.s`). É o caminho
completo até o código de máquina: registradores, quadro de pilha, rótulos e saltos, e a convenção de chamada
do sistema operacional para usar o `printf`.

## 2. Análise léxica

Palavras-chave `func int float if else while return print`; `ID`; literais inteiros, reais (`8.5`) e de texto
(entre aspas duplas, só usados no `print`); `= + - * / % < > <= >= == != && || ! ( ) { } , ;`; comentários `// ...`.

## 3. Análise sintática

```ebnf
programa ::= { funcao | comando } EOF          (* comandos fora de funções formam o programa principal *)
funcao   ::= "func" tipo ID "(" [ param { "," param } ] ")" bloco
param    ::= tipo ID
tipo     ::= "int" | "float"
bloco    ::= "{" { comando } "}"
comando  ::= tipo ID "=" expr ";" | ID "=" expr ";" | "if" "(" expr ")" bloco [ "else" bloco ]
           | "while" "(" expr ")" bloco | "return" expr ";" | "print" "(" expr { "," expr } ")" ";"
           | chamada ";" | bloco
chamada  ::= ID "(" [ expr { "," expr } ] ")"
expr     ::= ("-" | "!") expr | expr op expr | chamada | INT | FLOAT | STRING | ID | "(" expr ")"
```
Precedência (da maior para a menor): unários; `* / %`; `+ -`; comparações; `&&`; `||`.

## 4. Análise semântica e regras da linguagem

- **Tabela de funções** com as assinaturas; função repetida, inexistente ou chamada com número/tipo de argumentos
  errado é erro. Funções podem ser chamadas antes de serem definidas (recursão mútua).
- **Tabelas de símbolos por escopo**: cada bloco abre um escopo; funções **não** enxergam as variáveis do programa principal.
- **Tipos**: `int`, `float` e `bool` (resultado de comparações e operadores lógicos). `int → float` é implícito;
  `float → int` é erro. `%` só com `int`. Condições de `if`/`while` devem ser `bool`. `print` não aceita `bool`.
- `return` fora de função é erro; o tipo do valor retornado deve ser compatível com o da função.
- `print(a, b, ...)` imprime os valores separados por um espaço e termina a linha (inteiros com `%d`, reais com `%g` do C).
- Como não há C no caminho, nomes como `printf` e `main` são **permitidos** para funções Lingo (dê aos rótulos um prefixo).

## 5. Saída

O assembly gerado (formato livre) gravado em um arquivo `.s`. Nos testes é comparada a **saída da execução**
do programa (que deve ser igual à do enunciado 30 para os mesmos programas).

## 6. Dicas

- Modelo mais simples: **máquina de pilha**. Toda expressão deixa o resultado em `%rax` (`int`/`bool`) ou `%xmm0`
  (`float` = `double`); numa operação binária, avalie a esquerda, `pushq`, avalie a direita, `popq` e opere.
- Variáveis locais em `-8(%rbp)`, `-16(%rbp)`, ...; para as suas funções, você pode usar uma convenção própria
  (ex.: argumentos empilhados).
- O `printf` exige a ABI do sistema: Linux (System V) → formato em `%rdi`, inteiro em `%rsi`, double em `%xmm0`,
  `%al` = nº de registradores vetoriais, `%rsp` alinhado em 16 bytes, `call printf@PLT`. Windows/MinGW (Microsoft x64)
  → `%rcx`, `%rdx` (double em `%xmm1` **e** `%rdx`), 32 bytes de *shadow space*, `call printf`.
- Compare o seu assembly com o do `gcc -S -O0` e `-O2` para um programa C equivalente (Aula 25, Slides 13 e 14).

## 7. Casos de teste

O `testar.sh` compara só a **saída da execução** do programa gerado (o código gerado é livre). Os testes `erro_*` passam se o compilador terminar com código de saída
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
// funções recursivas, laços e conversão int -> float
func int fat(int n) {
    if (n <= 1) { return 1; }
    return n * fat(n - 1);
}
func float media(float a, float b) {
    return (a + b) / 2.0;
}
func int mdc(int a, int b) {
    while (b != 0) { int r = a % b; a = b; b = r; }
    return a;
}
int i = 1;
while (i <= 6) {
    print("fat(", i, ") =", fat(i));
    i = i + 1;
}
float m = media(7, 8.5);
print("média(7, 8.5) =", m);
print("mdc(84, 36) =", mdc(84, 36));

print("100% ok:", fat(3));
```

**Saída da execução do programa gerado** (é o que o `testar.sh` compara):

```text
fat( 1 ) = 1
fat( 2 ) = 2
fat( 3 ) = 6
fat( 4 ) = 24
fat( 5 ) = 120
fat( 6 ) = 720
média(7, 8.5) = 7.75
mdc(84, 36) = 12
100% ok: 6
```

#### `ok_2.txt`

**Entrada:**

```text
// números primos e conjectura de Collatz
func int primo(int n) {
    if (n < 2) { return 0; }
    int d = 2;
    while (d * d <= n) {
        if (n % d == 0) { return 0; }
        d = d + 1;
    }
    return 1;
}
func int collatz(int n) {
    int passos = 0;
    while (n != 1) {
        if (n % 2 == 0) { n = n / 2; } else { n = 3 * n + 1; }
        passos = passos + 1;
    }
    return passos;
}
int k = 2;
int qtd = 0;
while (k <= 50) {
    if (primo(k) == 1) { qtd = qtd + 1; }
    k = k + 1;
}
print("primos até 50:", qtd);
print("collatz(27) =", collatz(27), "passos");
float x = 1.0;
if (qtd > 10 && !(x == 0.0)) { print("x/3 =", x / 3); }
```

**Saída da execução do programa gerado** (é o que o `testar.sh` compara):

```text
primos até 50: 15
collatz(27) = 111 passos
x/3 = 0.333333
```

#### `ok_3.txt`

**Entrada:**

```text
// nomes da biblioteca C viram lingo_<nome> no assembly; float, negação e curto-circuito
func int printf(int a) { return a * 2; }
func float area(float r) { return 3.14159 * r * r; }
func int max(int a, int b) {
    if (a > b) { return a; } else { return b; }
}
float r = 2;
print("area(2) =", area(r), "e printf(21) =", printf(21));
print("max(-5, 3) =", max(-5, 3), "| -r =", -r);
int i = 0;
while (i < 3 && !(area(1.0) < 3.0)) { i = i + 1; }
print("i =", i, "| 7 / 2 =", 7 / 2, "| -7 % 3 =", -7 % 3, "| 7 / 2.0 =", 7 / 2.0);
if (r >= 2.0 && r != 3.0) { print("r >= 2.0 ok, 100% assembly"); }
```

**Saída da execução do programa gerado** (é o que o `testar.sh` compara):

```text
area(2) = 12.5664 e printf(21) = 42
max(-5, 3) = 3 | -r = -2
i = 3 | 7 / 2 = 3 | -7 % 3 = -1 | 7 / 2.0 = 3.5
r >= 2.0 ok, 100% assembly
```


### 7.2 Programas com erro

#### `erro_1.txt`

```text
func int f(int a) { return a; }
print(f(2.5));
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro semântico [2:9]: tipos incompatíveis no argumento 1 de 'f': esperado int, encontrado float
```

#### `erro_2.txt`

```text
int x = 1;
func int g() { return x; }
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro semântico [2:23]: variável 'x' não declarada na função 'g' (funções não enxergam as variáveis do programa principal)
```

#### `erro_3.txt`

```text
func float h() { return 1.5; }
int y = h();
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro semântico [2:9]: tipos incompatíveis na inicialização de 'y': esperado int, encontrado float
```

#### `erro_4.txt`

```text
int z = 1;
if (z) { print(z); }
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro semântico [2:5]: condição do if deve ser lógica (comparação), mas é int
```

#### `erro_5.txt`

```text
// return fora de uma função
return 1;
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro semântico [2:1]: 'return' fora de uma função
```

#### `erro_6.txt`

```text
int a = 1;
print(a +);
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro sintático [2:10]: token inesperado ')'
```


Escreva também **pelo menos 2 testes `ok_` e 2 testes `erro_` seus**, cobrindo casos que estes não cobrem.

## 8. Desafios extras (opcionais)

- Alocação de registradores simples para as variáveis locais (em vez de sempre usar a pilha).
- Gerar também a versão para a outra ABI (Linux ↔ Windows) com uma opção de linha de comando.

## 9. Critérios de avaliação

| Critério | Peso |
|---|---|
| Análise léxica (tokens, posições, erros léxicos) | 20% |
| Análise sintática (gramática sem conflitos / avisos, precedência, erros sintáticos) | 25% |
| Semântica e back-end (regras da seção 4, saída da seção 5) | 35% |
| Mensagens de erro (tipo, `[linha:coluna]`, clareza) | 10% |
| Testes próprios e README (como compilar, executar e testar) | 10% |
