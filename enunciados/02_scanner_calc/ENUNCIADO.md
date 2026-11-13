# Enunciado 02 — Scanner da calculadora

**Teoria da Computação e Compiladores — 6M (Digital) · Prof. Giovane Barcelos**

| | |
|---|---|
| **Nível** | ⭐ (introdutório) |
| **Aulas relacionadas** | 16 e 17 |
| **Ferramentas** | ANTLR 4 **ou** JFlex + JCup (implementar com as duas vale como desafio extra) |
| **Testes fornecidos** | 1 válido(s) (`ok_*`) e 1 inválido(s) (`erro_*`), na pasta [`testes/`](testes/) |

> Leia antes as **regras gerais** em [`../README.md`](../README.md): formato das mensagens de erro, códigos de
> saída, como usar o `testar.sh`, o que entregar e como será avaliado.

---

## 1. Objetivo

Escrever o scanner de uma calculadora: números, identificadores, os quatro operadores e parênteses.
A saída mostra, para cada linha do arquivo, a sequência de tokens reconhecida. O exercício também
mostra o **limite do scanner**: parênteses desbalanceados são lexicamente válidos (quem os rejeita é o
parser, no enunciado 04).

## 2. Análise léxica

| Token | Lexema |
|---|---|
| `ID` | letra ou `_`, seguida de letras, dígitos ou `_` |
| `NUMBER` | um ou mais dígitos |
| `PLUS` `MINUS` `TIMES` `DIVIDE` | `+` `-` `*` `/` |
| `LPAREN` `RPAREN` | `(` `)` |
| (ignorados) | espaços, tabulações e quebras de linha |

Qualquer outro caractere é erro léxico. No JFlex, use **macros** (`DIGIT`, `NUMBER`, `ID`).

## 3. Análise sintática

```ebnf
programa ::= { token } EOF        (* aceita QUALQUER sequência de tokens *)
```

## 4. Análise semântica e regras da linguagem

Não há análise semântica: `(1 + 2` é aceito.

## 5. Saída

Para cada linha do arquivo que tem tokens: `número_da_linha -> TOKEN TOKEN ...` (veja os casos de teste).

## 6. Dicas

- Agrupe os tokens pela linha em que começam (`yyline` + 1 no JFlex; `getLine()` no ANTLR).

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
1 + 2
36 *    7
5 -     38
12 / 4
( 1 + 2 ) * ( 3 - 4) / 3
1 + 2 ) * ( 3 - 4) / 3
( 1 + 2 ) * ( 3 - 4)  3
( 1 + 2 ) * ( 3 - 4 / 3
( num + 3 )
```

**Saída esperada:**

```text
1 -> NUMBER(1) PLUS NUMBER(2)
2 -> NUMBER(36) TIMES NUMBER(7)
3 -> NUMBER(5) MINUS NUMBER(38)
4 -> NUMBER(12) DIVIDE NUMBER(4)
5 -> LPAREN NUMBER(1) PLUS NUMBER(2) RPAREN TIMES LPAREN NUMBER(3) MINUS NUMBER(4) RPAREN DIVIDE NUMBER(3)
6 -> NUMBER(1) PLUS NUMBER(2) RPAREN TIMES LPAREN NUMBER(3) MINUS NUMBER(4) RPAREN DIVIDE NUMBER(3)
7 -> LPAREN NUMBER(1) PLUS NUMBER(2) RPAREN TIMES LPAREN NUMBER(3) MINUS NUMBER(4) RPAREN NUMBER(3)
8 -> LPAREN NUMBER(1) PLUS NUMBER(2) RPAREN TIMES LPAREN NUMBER(3) MINUS NUMBER(4) DIVIDE NUMBER(3)
9 -> LPAREN ID(num) PLUS NUMBER(3) RPAREN
```


### 7.2 Programas com erro

#### `erro_1.txt`

```text
( num + 3 )
1 + 3 * [ 4 + 5 ]
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro léxico [2:9]: caractere inválido '['
```


Escreva também **pelo menos 2 testes `ok_` e 2 testes `erro_` seus**, cobrindo casos que estes não cobrem.

## 8. Desafios extras (opcionais)

- Mostre também o lexema dos tokens `ID` e `NUMBER`: `NUMBER(42)`.

## 9. Critérios de avaliação

| Critério | Peso |
|---|---|
| Análise léxica (tokens, posições, erros léxicos) | 20% |
| Análise sintática (gramática sem conflitos / avisos, precedência, erros sintáticos) | 25% |
| Semântica e back-end (regras da seção 4, saída da seção 5) | 35% |
| Mensagens de erro (tipo, `[linha:coluna]`, clareza) | 10% |
| Testes próprios e README (como compilar, executar e testar) | 10% |
