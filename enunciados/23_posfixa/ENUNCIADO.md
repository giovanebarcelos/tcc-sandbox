# Enunciado 23 — Infixa → pós-fixa e pré-fixa

**Teoria da Computação e Compiladores — 6M (Digital) · Prof. Giovane Barcelos**

| | |
|---|---|
| **Nível** | ⭐⭐ (básico) |
| **Aulas relacionadas** | 18–20 (LL × LR) e 25 |
| **Ferramentas** | ANTLR 4 **ou** JFlex + JCup (implementar com as duas vale como desafio extra) |
| **Testes fornecidos** | 1 válido(s) (`ok_*`) e 3 inválido(s) (`erro_*`), na pasta [`testes/`](testes/) |

> Leia antes as **regras gerais** em [`../README.md`](../README.md): formato das mensagens de erro, códigos de
> saída, como usar o `testar.sh`, o que entregar e como será avaliado.

---

## 1. Objetivo

Traduzir expressões **infixas** para as notações **pós-fixa** (RPN) e **pré-fixa** e avaliar a pós-fixa
numa **máquina de pilha**, mostrando a pilha a cada passo. É um exercício de **tradução dirigida pela sintaxe**.

## 2. Análise léxica

Números inteiros, variáveis (`[a-z][a-z0-9]*`), `+ - * / ^ ( )` e `;`.

## 3. Análise sintática

```ebnf
programa ::= { expr ";" } EOF
expr     ::= expr ("+" | "-") termo | termo
termo    ::= termo ("*" | "/") fator | fator
fator    ::= base "^" fator | base          (* ^ associativo à direita *)
base     ::= NUM | ID | "(" expr ")"
```
- **JCup**: use a gramática acima (estilo "livro do dragão", recursão à esquerda).
- **ANTLR**: reescreva no estilo **LL** com EBNF (`termo (op termo)*`) e faça a associatividade à esquerda no visitor.

## 4. Análise semântica e regras da linguagem

- Expressões **sem variáveis** são avaliadas (divisão inteira); divisão por zero é erro de execução.
- Expressões **com variáveis** são só traduzidas, não avaliadas (a saída avisa isso).

## 5. Saída

Para cada expressão: a forma infixa totalmente parentizada, as formas pós-fixa e pré-fixa e, se for avaliável,
o conteúdo da pilha depois de cada passo e o valor. Veja o formato exato nos casos de teste.

## 6. Dicas

- Pós-fixa = percurso em **pós-ordem** da AST; pré-fixa = **pré-ordem**.

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
(2 + 3) * 4;
2 + 3 * 4;
10 - 4 - 3;
2 ^ 3 ^ 2;
(1 + 2) * (3 + 4) / 7;
a * (b + c) - d / e;
```

**Saída esperada:**

```text
Expressão 1: ((2 + 3) * 4)
  pós-fixa : 2 3 + 4 *
  pré-fixa : * + 2 3 4
  pilha    : [2] [2, 3] [5] [5, 4] [20]
  valor    = 20
Expressão 2: (2 + (3 * 4))
  pós-fixa : 2 3 4 * +
  pré-fixa : + 2 * 3 4
  pilha    : [2] [2, 3] [2, 3, 4] [2, 12] [14]
  valor    = 14
Expressão 3: ((10 - 4) - 3)
  pós-fixa : 10 4 - 3 -
  pré-fixa : - - 10 4 3
  pilha    : [10] [10, 4] [6] [6, 3] [3]
  valor    = 3
Expressão 4: (2 ^ (3 ^ 2))
  pós-fixa : 2 3 2 ^ ^
  pré-fixa : ^ 2 ^ 3 2
  pilha    : [2] [2, 3] [2, 3, 2] [2, 9] [512]
  valor    = 512
Expressão 5: (((1 + 2) * (3 + 4)) / 7)
  pós-fixa : 1 2 + 3 4 + * 7 /
  pré-fixa : / * + 1 2 + 3 4 7
  pilha    : [1] [1, 2] [3] [3, 3] [3, 3, 4] [3, 7] [21] [21, 7] [3]
  valor    = 3
Expressão 6: ((a * (b + c)) - (d / e))
  pós-fixa : a b c + * d e / -
  pré-fixa : - * a + b c / d e
  (contém variáveis: só traduzida, não avaliada)
```


### 7.2 Programas com erro

#### `erro_1.txt`

```text
2 + * 3;
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro sintático [1:5]: token inesperado '*'
```

#### `erro_2.txt`

```text
(2 + 3;
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro sintático [1:7]: token inesperado ';'
```

#### `erro_3.txt`

```text
8 / (4 - 4);
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro de execução [1:1]: divisão por zero na expressão 1
```


Escreva também **pelo menos 2 testes `ok_` e 2 testes `erro_` seus**, cobrindo casos que estes não cobrem.

## 8. Desafios extras (opcionais)

- O caminho inverso: ler pós-fixa e reconstruir a infixa com o mínimo de parênteses.

## 9. Critérios de avaliação

| Critério | Peso |
|---|---|
| Análise léxica (tokens, posições, erros léxicos) | 20% |
| Análise sintática (gramática sem conflitos / avisos, precedência, erros sintáticos) | 25% |
| Semântica e back-end (regras da seção 4, saída da seção 5) | 35% |
| Mensagens de erro (tipo, `[linha:coluna]`, clareza) | 10% |
| Testes próprios e README (como compilar, executar e testar) | 10% |
