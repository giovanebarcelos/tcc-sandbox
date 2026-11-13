# Enunciado 09 — Repetição DO → código de 3 endereços e C

**Teoria da Computação e Compiladores — 6M (Digital) · Prof. Giovane Barcelos**

| | |
|---|---|
| **Nível** | ⭐⭐⭐ (intermediário) |
| **Aulas relacionadas** | 25 |
| **Ferramentas** | ANTLR 4 **ou** JFlex + JCup (implementar com as duas vale como desafio extra) |
| **Testes fornecidos** | 2 válido(s) (`ok_*`) e 3 inválido(s) (`erro_*`), na pasta [`testes/`](testes/) |

> Leia antes as **regras gerais** em [`../README.md`](../README.md): formato das mensagens de erro, códigos de
> saída, como usar o `testar.sh`, o que entregar e como será avaliado.

---

## 1. Objetivo

Gerar código para um laço com sintaxe própria, `do [inicialização; condição; passo] comando`,
equivalente ao `for` do C. O compilador produz código de 3 endereços (`ifFalse ... goto`) e um programa
C que é compilado com o `gcc` e executado. Laços podem ser aninhados.

## 2. Análise léxica

`do out`, `ID`, `NUM`, `= + - * / < > <= >= == != ++ -- += -= [ ] ( ) { } ;`.

## 3. Análise sintática

```ebnf
programa ::= { stmt } EOF
stmt     ::= "do" "[" ID "=" expr ";" cond ";" passo "]" stmt
           | "out" "(" expr ")" [ ";" ]
           | ID "=" expr ";"
           | "{" { stmt } "}"
cond     ::= expr ("<" | ">" | "<=" | ">=" | "==" | "!=") expr
passo    ::= ID ("++" | "--") | ID ("+=" | "-=") expr
expr     ::= expr ("*" | "/") expr | expr ("+" | "-") expr | NUM | ID | "(" expr ")"
```
Exemplo: `do [num = 1; num < 7; num++] out(num)`.

## 4. Análise semântica e regras da linguagem

- O **passo deve usar a variável do laço**: `do [i = 0; i < 3; j++]` é erro semântico.
- `out(expr)` imprime o valor seguido de quebra de linha.

## 5. Saída

Imprima o código de 3 endereços e o C gerado (formato livre) e grave o C em um arquivo `.c`. Nos testes, é
comparada só a **saída da execução** do programa C.

## 6. Dicas

- Esquema de tradução do laço: `init; Linicio: ifFalse cond goto Lfim; corpo; passo; goto Linicio; Lfim:`.

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
do [num = 1; num < 7; num++]
     out(num)
```

**Saída da execução do programa gerado** (é o que o `testar.sh` compara):

```text
1
2
3
4
5
6
```

<details><summary>Saída completa da solução de referência (o código gerado pode ser diferente)</summary>

```text
=== Código de 3 endereços ===
    num = 1
L1:
    ifFalse num < 7 goto L2
    out num
    num = num + 1
    goto L1
L2:
=== Código C gerado ===
#include <stdio.h>

int main(void) {
    int num = 0;
    for (num = 1; num < 7; num++) {
        printf("%d\n", num);
    }
    return 0;
}
=== Execução do programa C gerado ===
1
2
3
4
5
6
(código de saída: 0)
```

</details>

#### `ok_2.txt`

**Entrada:**

```text
soma = 0;
do [i = 1; i <= 3; i++] {
    do [j = 1; j <= 3; j++] out(i * j);
    soma = soma + i;
}
out(soma);
do [k = 10; k > 0; k -= 3] out(k)
```

**Saída da execução do programa gerado** (é o que o `testar.sh` compara):

```text
1
2
3
2
4
6
3
6
9
6
10
7
4
1
```

<details><summary>Saída completa da solução de referência (o código gerado pode ser diferente)</summary>

```text
=== Código de 3 endereços ===
    soma = 0
    i = 1
L1:
    ifFalse i <= 3 goto L2
    j = 1
L3:
    ifFalse j <= 3 goto L4
    t1 = i * j
    out t1
    j = j + 1
    goto L3
L4:
    t2 = soma + i
    soma = t2
    i = i + 1
    goto L1
L2:
    out soma
    k = 10
L5:
    ifFalse k > 0 goto L6
    out k
    k = k - 3
    goto L5
L6:
=== Código C gerado ===
#include <stdio.h>

int main(void) {
    int soma = 0, i = 0, j = 0, k = 0;
    soma = 0;
    for (i = 1; i <= 3; i++) {
        for (j = 1; j <= 3; j++) {
            printf("%d\n", (i * j));
        }
        soma = (soma + i);
    }
    printf("%d\n", soma);
    for (k = 10; k > 0; k -= 3) {
        printf("%d\n", k);
    }
    return 0;
}
=== Execução do programa C gerado ===
1
2
3
2
4
6
3
6
9
6
10
7
4
1
(código de saída: 0)
```

</details>


### 7.2 Programas com erro

#### `erro_1.txt`

```text
do [num = 1; num < 10 num++]
   out(num)
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro sintático [1:23]: token inesperado 'num'
```

#### `erro_2.txt`

```text
do [i = 0; i < 3; j++]
   out(i)
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro semântico [1:19]: o passo usa 'j', mas a variável do laço é 'i'
```

#### `erro_3.txt`

```text
do [i = 0; i < 3; i**]
   out(i)
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro sintático [1:20]: token inesperado '*'
```


Escreva também **pelo menos 2 testes `ok_` e 2 testes `erro_` seus**, cobrindo casos que estes não cobrem.

## 8. Desafios extras (opcionais)

- `break` e `continue` (exigem uma pilha de rótulos de laço).
- Avisar laços que nunca executam (`do [i = 10; i < 5; i++]`).

## 9. Critérios de avaliação

| Critério | Peso |
|---|---|
| Análise léxica (tokens, posições, erros léxicos) | 20% |
| Análise sintática (gramática sem conflitos / avisos, precedência, erros sintáticos) | 25% |
| Semântica e back-end (regras da seção 4, saída da seção 5) | 35% |
| Mensagens de erro (tipo, `[linha:coluna]`, clareza) | 10% |
| Testes próprios e README (como compilar, executar e testar) | 10% |
