# Enunciado 08 — IF/ELSE → código de 3 endereços e C

**Teoria da Computação e Compiladores — 6M (Digital) · Prof. Giovane Barcelos**

| | |
|---|---|
| **Nível** | ⭐⭐⭐ (intermediário) |
| **Aulas relacionadas** | 10 (ambiguidade), 19–20 (shift/reduce) e 25 (geração de código) |
| **Ferramentas** | ANTLR 4 **ou** JFlex + JCup (implementar com as duas vale como desafio extra) |
| **Testes fornecidos** | 2 válido(s) (`ok_*`) e 3 inválido(s) (`erro_*`), na pasta [`testes/`](testes/) |

> Leia antes as **regras gerais** em [`../README.md`](../README.md): formato das mensagens de erro, códigos de
> saída, como usar o `testar.sh`, o que entregar e como será avaliado.

---

## 1. Objetivo

Gerar código para uma linguagem com atribuição, `if`/`else`, `print` e blocos, em duas formas:
**código de três endereços** (com rótulos e `goto`) e **código C**, que deve ser compilado com o `gcc` e
**executado**. A gramática tem o famoso ***dangling else***: em `if (a) if (b) x; else y;` o `else`
pertence ao `if` mais próximo.

## 2. Análise léxica

`if else print`, `ID`, `NUM` (inteiro), `= + - * / == != < > <= >= ( ) { } ;` e comentários `// ...`.

## 3. Análise sintática

```ebnf
programa ::= { stmt } EOF
stmt     ::= ID "=" expr ";" | "if" "(" cond ")" stmt [ "else" stmt ] | "print" "(" expr ")" ";" | "{" { stmt } "}"
cond     ::= expr ("==" | "!=" | "<" | ">" | "<=" | ">=") expr
expr     ::= expr ("*" | "/") expr | expr ("+" | "-") expr | NUM | ID | "(" expr ")"
```
- **JCup**: resolva o conflito *shift/reduce* do `else` com precedência (`%prec`), sem deixar o CUP escolher sozinho.
- **ANTLR**: verifique na árvore (`grun -tree`) a qual `if` o `else` foi ligado.

## 4. Análise semântica e regras da linguagem

- Variável usada **antes de receber valor** é erro semântico (análise de fluxo simples, na ordem do programa).

## 5. Saída

O compilador imprime o código de 3 endereços e o código C gerado, e grava o C em um arquivo `.c`.
**O formato do código gerado é livre.** O que é comparado nos testes é a **saída da execução** do programa C
(compile com `gcc` e execute; veja *Como testar*).

## 6. Dicas

- Gere rótulos novos com um contador (`L1`, `L2`, ...) e temporários `t1`, `t2`, ...
- O C gerado pode usar `goto` e rótulos, imitando o código de 3 endereços.

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
x = 5;
if (x > 0)
    y = 1;
else
    y = 2;
print(y);
```

**Saída da execução do programa gerado** (é o que o `testar.sh` compara):

```text
1
```

<details><summary>Saída completa da solução de referência (o código gerado pode ser diferente)</summary>

```text
=== Código de 3 endereços ===
    x = 5
    if x > 0 goto L1
    goto L2
L1:
    y = 1
    goto L3
L2:
    y = 2
L3:
    print y
=== Código C gerado ===
#include <stdio.h>

int main(void) {
    int x = 0, y = 0;
    x = 5;
    if (x > 0) {
        y = 1;
    } else {
        y = 2;
    }
    printf("%d\n", y);
    return 0;
}
=== Execução do programa C gerado ===
1
(código de saída: 0)
```

</details>

#### `ok_2.txt`

**Entrada:**

```text
// blocos, expressões e o "dangling else"
a = 7;
b = a * 2 - 4;
if (b >= 10) { c = a + b; print(c); }
if (a == 7) if (b > 100) print(a); else print(b);   // o else pertence ao if (b > 100)
if (a != 7) print(1); else { print(a / 2); print(a - b); }
```

**Saída da execução do programa gerado** (é o que o `testar.sh` compara):

```text
17
10
3
-3
```

<details><summary>Saída completa da solução de referência (o código gerado pode ser diferente)</summary>

```text
=== Código de 3 endereços ===
    a = 7
    t1 = a * 2
    t2 = t1 - 4
    b = t2
    if b >= 10 goto L1
    goto L2
L1:
    t3 = a + b
    c = t3
    print c
L2:
    if a == 7 goto L3
    goto L4
L3:
    if b > 100 goto L5
    goto L6
L5:
    print a
    goto L7
L6:
    print b
L7:
L4:
    if a != 7 goto L8
    goto L9
L8:
    print 1
    goto L10
L9:
    t4 = a / 2
    print t4
    t5 = a - b
    print t5
L10:
=== Código C gerado ===
#include <stdio.h>

int main(void) {
    int a = 0, b = 0, c = 0;
    a = 7;
    b = ((a * 2) - 4);
    if (b >= 10) {
        c = (a + b);
        printf("%d\n", c);
    }
    if (a == 7) {
        if (b > 100) {
            printf("%d\n", a);
        } else {
            printf("%d\n", b);
        }
    }
    if (a != 7) {
        printf("%d\n", 1);
    } else {
        printf("%d\n", (a / 2));
        printf("%d\n", (a - b));
    }
    return 0;
}
=== Execução do programa C gerado ===
17
10
3
-3
(código de saída: 0)
```

</details>


### 7.2 Programas com erro

#### `erro_1.txt`

```text
if (x < 10) y = 5; else y = ;
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro sintático [1:29]: token inesperado ';'
```

#### `erro_2.txt`

```text
x = 0;
if (x > 0)
    y = 1
else
    y = 2;
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro sintático [4:1]: token inesperado 'else'
```

#### `erro_3.txt`

```text
x = 1;
if (x > 0) print(z);
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro semântico [2:18]: variável 'z' usada antes de receber valor
```


Escreva também **pelo menos 2 testes `ok_` e 2 testes `erro_` seus**, cobrindo casos que estes não cobrem.

## 8. Desafios extras (opcionais)

- Operadores lógicos `&&`, `||` e `!` com curto-circuito.
- Otimize saltos: `goto L1` seguido de `L1:` pode ser removido.

## 9. Critérios de avaliação

| Critério | Peso |
|---|---|
| Análise léxica (tokens, posições, erros léxicos) | 20% |
| Análise sintática (gramática sem conflitos / avisos, precedência, erros sintáticos) | 25% |
| Semântica e back-end (regras da seção 4, saída da seção 5) | 35% |
| Mensagens de erro (tipo, `[linha:coluna]`, clareza) | 10% |
| Testes próprios e README (como compilar, executar e testar) | 10% |
