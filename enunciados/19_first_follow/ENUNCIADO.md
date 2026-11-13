# Enunciado 19 — FIRST, FOLLOW e tabela LL(1)

**Teoria da Computação e Compiladores — 6M (Digital) · Prof. Giovane Barcelos**

| | |
|---|---|
| **Nível** | ⭐⭐⭐⭐ (avançado) |
| **Aulas relacionadas** | 18 (FIRST, FOLLOW, LL(1)) |
| **Ferramentas** | ANTLR 4 **ou** JFlex + JCup (implementar com as duas vale como desafio extra) |
| **Testes fornecidos** | 3 válido(s) (`ok_*`) e 3 inválido(s) (`erro_*`), na pasta [`testes/`](testes/) |

> Leia antes as **regras gerais** em [`../README.md`](../README.md): formato das mensagens de erro, códigos de
> saída, como usar o `testar.sh`, o que entregar e como será avaliado.

---

## 1. Objetivo

A entrada é uma **gramática livre de contexto**. O programa calcula os **anuláveis**, os conjuntos
**FIRST** e **FOLLOW**, monta a **tabela LL(1)** e aponta os **conflitos** (quando a gramática não é LL(1)).

## 2. Análise léxica

- Não-terminal: maiúscula seguida de letras/dígitos/`_` e apóstrofos opcionais (`E`, `E'`, `Lista`).
- Terminal: minúsculas (`id`, `num`), texto entre aspas (`"=="`) ou um símbolo de pontuação (`+ - * / ( ) ^ , . < > = ! & ? # % [ ] { }`).
- `->`, `|`, `;`, `ε`; comentários `// ...`.

## 3. Análise sintática

```ebnf
gramatica   ::= regra { regra } EOF
regra       ::= NT "->" alternativa { "|" alternativa } ";"
alternativa ::= simbolo { simbolo } | "ε"
simbolo     ::= NT | TERM
```
O símbolo inicial é o lado esquerdo da primeira regra; `$` marca o fim da entrada no FOLLOW.

## 4. Análise semântica e regras da linguagem

- Não-terminal usado sem nenhuma regra é erro semântico.
- Algoritmos de **ponto fixo**: repita até nenhum conjunto mudar.
- Conflito LL(1): duas produções na mesma célula `M[A, a]`.

## 5. Saída

Anuláveis, FIRST e FOLLOW de cada não-terminal (na ordem em que aparecem; os elementos de cada conjunto em
**ordem lexicográfica**, como `{ $, ), + }` — veja os testes), a tabela LL(1) e a lista de conflitos com o veredito "é LL(1)" ou "não é LL(1)".

## 6. Dicas

- Teste com as gramáticas da Aula 18: a de expressões fatorada (LL(1)), a com recursão à esquerda e a do *dangling else*.

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
// gramática de expressões SEM recursão à esquerda (clássica, LL(1))
E  -> T E' ;
E' -> + T E' | ε ;
T  -> F T' ;
T' -> * F T' | ε ;
F  -> ( E ) | id ;
```

**Saída esperada:**

```text
Gramática: 5 não-terminal(is), 5 terminal(is), símbolo inicial E
  E    -> T E'
  E'   -> + T E' | ε
  T    -> F T'
  T'   -> * F T' | ε
  F    -> ( E ) | id
Anuláveis: E', T'
FIRST:
  E    = { (, id }
  E'   = { + }
  T    = { (, id }
  T'   = { * }
  F    = { (, id }
FOLLOW:
  E    = { $, ) }
  E'   = { $, ) }
  T    = { $, ), + }
  T'   = { $, ), + }
  F    = { $, ), *, + }
Tabela LL(1):
  M[E, (] = E -> T E'
  M[E, id] = E -> T E'
  M[E', $] = E' -> ε
  M[E', )] = E' -> ε
  M[E', +] = E' -> + T E'
  M[T, (] = T -> F T'
  M[T, id] = T -> F T'
  M[T', $] = T' -> ε
  M[T', )] = T' -> ε
  M[T', *] = T' -> * F T'
  M[T', +] = T' -> ε
  M[F, (] = F -> ( E )
  M[F, id] = F -> id
=> A gramática é LL(1).
```

#### `ok_2.txt`

**Entrada:**

```text
// recursão à esquerda: não é LL(1)
E -> E + T | T ;
T -> T * F | F ;
F -> ( E ) | id ;
```

**Saída esperada:**

```text
Gramática: 3 não-terminal(is), 5 terminal(is), símbolo inicial E
  E    -> E + T | T
  T    -> T * F | F
  F    -> ( E ) | id
Anuláveis: (nenhum)
FIRST:
  E    = { (, id }
  T    = { (, id }
  F    = { (, id }
FOLLOW:
  E    = { $, ), + }
  T    = { $, ), *, + }
  F    = { $, ), *, + }
Tabela LL(1):
  M[E, (] = E -> E + T  /  E -> T   <-- CONFLITO
  M[E, id] = E -> E + T  /  E -> T   <-- CONFLITO
  M[T, (] = T -> T * F  /  T -> F   <-- CONFLITO
  M[T, id] = T -> T * F  /  T -> F   <-- CONFLITO
  M[F, (] = F -> ( E )
  M[F, id] = F -> id
=> A gramática NÃO é LL(1): 4 célula(s) com conflito (recursão à esquerda? fatoração? ambiguidade?).
```

#### `ok_3.txt`

**Entrada:**

```text
// "dangling else" (Dragon Book): ambígua, conflito em M[S2, e]
S  -> i Ex t S S2 | a ;
S2 -> e S | ε ;
Ex -> b ;
```

**Saída esperada:**

```text
Gramática: 3 não-terminal(is), 5 terminal(is), símbolo inicial S
  S    -> i Ex t S S2 | a
  S2   -> e S | ε
  Ex   -> b
Anuláveis: S2
FIRST:
  S    = { a, i }
  S2   = { e }
  Ex   = { b }
FOLLOW:
  S    = { $, e }
  S2   = { $, e }
  Ex   = { t }
Tabela LL(1):
  M[S, a] = S -> a
  M[S, i] = S -> i Ex t S S2
  M[S2, $] = S2 -> ε
  M[S2, e] = S2 -> e S  /  S2 -> ε   <-- CONFLITO
  M[Ex, b] = Ex -> b
=> A gramática NÃO é LL(1): 1 célula(s) com conflito (recursão à esquerda? fatoração? ambiguidade?).
```


### 7.2 Programas com erro

#### `erro_1.txt`

```text
S -> a B ;
A -> b ;
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro semântico [1:8]: não-terminal 'B' é usado mas não tem regras
```

#### `erro_2.txt`

```text
S -> a S b | ε
A -> b ;
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro sintático [2:1]: token inesperado 'A'
```

#### `erro_3.txt`

```text
S -> a @ b ;
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro léxico [1:8]: caractere inválido '@'
```


Escreva também **pelo menos 2 testes `ok_` e 2 testes `erro_` seus**, cobrindo casos que estes não cobrem.

## 8. Desafios extras (opcionais)

- Eliminar recursão à esquerda e fatorar automaticamente, e recalcular a tabela.
- Usar a tabela para analisar uma frase, mostrando a pilha passo a passo.

## 9. Critérios de avaliação

| Critério | Peso |
|---|---|
| Análise léxica (tokens, posições, erros léxicos) | 20% |
| Análise sintática (gramática sem conflitos / avisos, precedência, erros sintáticos) | 25% |
| Semântica e back-end (regras da seção 4, saída da seção 5) | 35% |
| Mensagens de erro (tipo, `[linha:coluna]`, clareza) | 10% |
| Testes próprios e README (como compilar, executar e testar) | 10% |
