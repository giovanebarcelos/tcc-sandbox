# Enunciado 04 — Calculadora

**Teoria da Computação e Compiladores — 6M (Digital) · Prof. Giovane Barcelos**

| | |
|---|---|
| **Nível** | ⭐⭐ (básico) |
| **Aulas relacionadas** | 10 (GLC e ambiguidade), 17, 19–21 (LR, JCup) |
| **Ferramentas** | ANTLR 4 **ou** JFlex + JCup (implementar com as duas vale como desafio extra) |
| **Testes fornecidos** | 1 válido(s) (`ok_*`) e 3 inválido(s) (`erro_*`), na pasta [`testes/`](testes/) |

> Leia antes as **regras gerais** em [`../README.md`](../README.md): formato das mensagens de erro, códigos de
> saída, como usar o `testar.sh`, o que entregar e como será avaliado.

---

## 1. Objetivo

Primeiro analisador **sintático**: uma calculadora de expressões inteiras. Cada expressão termina
com `;` e o programa imprime a expressão **totalmente parentizada** e o seu valor. A parentização mostra
a **precedência e a associatividade** escolhidas pelo parser.

## 2. Análise léxica

Números inteiros (`[0-9]+`), `+ - * /`, `(`, `)` e `;`. Espaços são ignorados.

## 3. Análise sintática

```ebnf
programa ::= { expr ";" } EOF
expr     ::= "-" expr | expr ("*" | "/") expr | expr ("+" | "-") expr | NUM | "(" expr ")"
```
A gramática acima é **ambígua**. Resolva com esta tabela (da maior para a menor precedência):

| Precedência | Operador | Associatividade |
|---|---|---|
| 1 | `-` unário | — |
| 2 | `*` `/` | esquerda |
| 3 | `+` `-` | esquerda |

- **ANTLR**: a precedência é a ordem das alternativas de `expr`.
- **JCup**: declare `precedence left ...` e use `%prec UMINUS` no menos unário.

## 4. Análise semântica e regras da linguagem

- Divisão inteira (trunca em direção a zero).
- **Divisão por zero** é um **erro de execução**, e a mensagem mostra a subexpressão: `divisão por zero em (10 / (5 - 5))`.

## 5. Saída

Para cada expressão: `<expressão totalmente parentizada> = <valor>`. O menos unário aparece sem parênteses próprios: `(-5 + 10) = 5`.

## 6. Dicas

- Monte uma **AST** (não avalie direto nas ações) e escreva duas funções sobre ela: `texto(no)` e `valor(no)`.
- Compare a árvore de derivação (parse tree, `grun -tree`) com a sua AST.

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
2 + 3;
10 - 5 * 2;
(10 - 5) * 2;
100 / (2 + 3);
-5 + 10;
2 * (3 + 4) - 5;
10 - 4 - 3;
-2 * -3;
```

**Saída esperada:**

```text
(2 + 3) = 5
(10 - (5 * 2)) = 0
((10 - 5) * 2) = 10
(100 / (2 + 3)) = 20
(-5 + 10) = 5
((2 * (3 + 4)) - 5) = 9
((10 - 4) - 3) = 3
(-2 * -3) = 6
```


### 7.2 Programas com erro

#### `erro_1.txt`

```text
1 + 2;
(1 + 2 * 3;
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro sintático [2:11]: token inesperado ';'
```

#### `erro_2.txt`

```text
7 * 2;
10 / (5 - 5);
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro de execução [2:4]: divisão por zero em (10 / (5 - 5))
```

#### `erro_3.txt`

```text
2 $ 3;
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro léxico [1:3]: caractere inválido '$'
```


Escreva também **pelo menos 2 testes `ok_` e 2 testes `erro_` seus**, cobrindo casos que estes não cobrem.

## 8. Desafios extras (opcionais)

- Acrescente `%` (resto) e `^` (potência, associativa à **direita**). Veja o enunciado 05.
- Mostre a AST desenhada como árvore no terminal.

## 9. Critérios de avaliação

| Critério | Peso |
|---|---|
| Análise léxica (tokens, posições, erros léxicos) | 20% |
| Análise sintática (gramática sem conflitos / avisos, precedência, erros sintáticos) | 25% |
| Semântica e back-end (regras da seção 4, saída da seção 5) | 35% |
| Mensagens de erro (tipo, `[linha:coluna]`, clareza) | 10% |
| Testes próprios e README (como compilar, executar e testar) | 10% |
