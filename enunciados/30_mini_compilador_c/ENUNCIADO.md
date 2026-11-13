# Enunciado 30 — Mini-compilador Lingo → C

**Teoria da Computação e Compiladores — 6M (Digital) · Prof. Giovane Barcelos**

| | |
|---|---|
| **Nível** | ⭐⭐⭐⭐⭐ (projeto completo) |
| **Aulas relacionadas** | 25 (geração de código) e 27 (Projeto Integrador A3) |
| **Ferramentas** | ANTLR 4 **ou** JFlex + JCup (implementar com as duas vale como desafio extra) |
| **Testes fornecidos** | 2 válido(s) (`ok_*`) e 6 inválido(s) (`erro_*`), na pasta [`testes/`](testes/) |

> Leia antes as **regras gerais** em [`../README.md`](../README.md): formato das mensagens de erro, códigos de
> saída, como usar o `testar.sh`, o que entregar e como será avaliado.

---

## 1. Objetivo

Um **compilador completo** — o esqueleto do projeto da A3. A linguagem **Lingo** tem funções tipadas
(`int`/`float`), variáveis, `if/else`, `while`, `return` e `print`. O compilador faz todas as fases (léxica,
sintática, tabelas de símbolos, verificação de tipos) e **gera código C**, que é compilado com o `gcc` e executado.

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
- Nomes que colidem com o C gerado (`main`, `printf`, palavras reservadas do C) devem ser rejeitados.

## 5. Saída

A tabela de funções e o código C gerado (formato livre), gravado em um arquivo `.c`. Nos testes é comparada a
**saída da execução** do programa C.

## 6. Dicas

- `int` da Lingo → `int` do C; `float` → `double`. Gere os **protótipos** de todas as funções antes dos corpos.
- Comandos fora de funções vão para o `main()` do C.

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

<details><summary>Saída completa da solução de referência (o código gerado pode ser diferente)</summary>

```text
=== Tabela de funções ===
  int fat(int)
  float media(float, float)
  int mdc(int, int)
=== Código C gerado ===
/* Gerado pelo mini-compilador Lingo (exemplo 30) */
#include <stdio.h>

int fat(int n);
double media(double a, double b);
int mdc(int a, int b);

int fat(int n) {
    if (n <= 1) {
        return 1;
    }
    return (n * fat((n - 1)));
    return 0;  /* caso nenhum return seja executado */
}

double media(double a, double b) {
    return ((a + b) / 2.0);
    return 0;  /* caso nenhum return seja executado */
}

int mdc(int a, int b) {
    while (b != 0) {
        int r = (a % b);
        a = b;
        b = r;
    }
    return a;
    return 0;  /* caso nenhum return seja executado */
}

int main(void) {
    int i = 1;
    while (i <= 6) {
        printf("fat( %d ) = %d\n", i, fat(i));
        i = (i + 1);
    }
    double m = media((double)(7), 8.5);
    printf("média(7, 8.5) = %g\n", m);
    printf("mdc(84, 36) = %d\n", mdc(84, 36));
    printf("100%% ok: %d\n", fat(3));
    return 0;
}
=== Execução do programa C gerado ===
fat( 1 ) = 1
fat( 2 ) = 2
fat( 3 ) = 6
fat( 4 ) = 24
fat( 5 ) = 120
fat( 6 ) = 720
média(7, 8.5) = 7.75
mdc(84, 36) = 12
100% ok: 6
(código de saída: 0)
```

</details>

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

<details><summary>Saída completa da solução de referência (o código gerado pode ser diferente)</summary>

```text
=== Tabela de funções ===
  int primo(int)
  int collatz(int)
=== Código C gerado ===
/* Gerado pelo mini-compilador Lingo (exemplo 30) */
#include <stdio.h>

int primo(int n);
int collatz(int n);

int primo(int n) {
    if (n < 2) {
        return 0;
    }
    int d = 2;
    while ((d * d) <= n) {
        if ((n % d) == 0) {
            return 0;
        }
        d = (d + 1);
    }
    return 1;
    return 0;  /* caso nenhum return seja executado */
}

int collatz(int n) {
    int passos = 0;
    while (n != 1) {
        if ((n % 2) == 0) {
            n = (n / 2);
        } else {
            n = ((3 * n) + 1);
        }
        passos = (passos + 1);
    }
    return passos;
    return 0;  /* caso nenhum return seja executado */
}

int main(void) {
    int k = 2;
    int qtd = 0;
    while (k <= 50) {
        if (primo(k) == 1) {
            qtd = (qtd + 1);
        }
        k = (k + 1);
    }
    printf("primos até 50: %d\n", qtd);
    printf("collatz(27) = %d passos\n", collatz(27));
    double x = 1.0;
    if ((qtd > 10) && (!(x == 0.0))) {
        printf("x/3 = %g\n", (x / (double)(3)));
    }
    return 0;
}
=== Execução do programa C gerado ===
primos até 50: 15
collatz(27) = 111 passos
x/3 = 0.333333
(código de saída: 0)
```

</details>


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
func int printf(int a) { return a; }
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro semântico [1:10]: 'printf' é reservado no C gerado; escolha outro nome
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

- Arrays (`int v[10];`) e laço `for`.
- Otimizar o C gerado (dobramento de constantes) e comparar com `gcc -O2`.

## 9. Critérios de avaliação

| Critério | Peso |
|---|---|
| Análise léxica (tokens, posições, erros léxicos) | 20% |
| Análise sintática (gramática sem conflitos / avisos, precedência, erros sintáticos) | 25% |
| Semântica e back-end (regras da seção 4, saída da seção 5) | 35% |
| Mensagens de erro (tipo, `[linha:coluna]`, clareza) | 10% |
| Testes próprios e README (como compilar, executar e testar) | 10% |
