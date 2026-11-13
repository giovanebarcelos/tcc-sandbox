# Enunciado 01 — Scanner básico

**Teoria da Computação e Compiladores — 6M (Digital) · Prof. Giovane Barcelos**

| | |
|---|---|
| **Nível** | ⭐ (introdutório) |
| **Aulas relacionadas** | 16 (Scanner manual) e 17 (JFlex/ANTLR) |
| **Ferramentas** | ANTLR 4 **ou** JFlex + JCup (implementar com as duas vale como desafio extra) |
| **Testes fornecidos** | 1 válido(s) (`ok_*`) e 1 inválido(s) (`erro_*`), na pasta [`testes/`](testes/) |

> Leia antes as **regras gerais** em [`../README.md`](../README.md): formato das mensagens de erro, códigos de
> saída, como usar o `testar.sh`, o que entregar e como será avaliado.

---

## 1. Objetivo

Construir o **primeiro analisador léxico** com um gerador de scanners. O programa lê um arquivo
e lista cada token encontrado com a sua posição (linha:coluna). É o "olá, mundo" dos compiladores:
o foco é escrever as expressões regulares dos tokens e entender o *maximal munch* (o scanner sempre
escolhe o lexema mais longo).

## 2. Análise léxica

| Token | Descrição |
|---|---|
| `ID` | letra ou `_`, seguida de letras, dígitos ou `_` (ex.: `nomeCompleto`, `y_2`) |
| `NUM` | um ou mais dígitos (ex.: `13`) |
| `PLUS` | o caractere `+` |
| (ignorados) | espaços, tabulações e quebras de linha |

Qualquer outro caractere é **erro léxico**.

## 3. Análise sintática

```ebnf
programa ::= { token } EOF
token    ::= ID | NUM | PLUS
```
A sintaxe é trivial de propósito: o parser só existe para que a ferramenta mostre uma árvore
(no ANTLR: `grun Basico programa -tokens -tree`).

## 4. Análise semântica e regras da linguagem

Não há análise semântica.

## 5. Saída

Uma linha por token: `TIPO(lexema)`, alinhado em uma coluna de 23 caracteres, seguido de `@linha:coluna`
(linha e coluna começam em 1). No fim, um resumo com o total e a contagem por tipo:
`Total: 11 token(s) {ID=4, NUM=2, PLUS=5}`. Veja o formato exato nos casos de teste.

## 6. Dicas

- `9Nome` deve virar **dois** tokens, `NUM(9)` e `ID(Nome)`: um identificador não pode começar com dígito.
- Use os recursos de posição da ferramenta: `yyline`/`yycolumn` (JFlex, com `%line %column`) ou
  `getLine()`/`getCharPositionInLine()` (ANTLR). Atenção: as duas começam em **0** em alguma das coordenadas.

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
13
nomeCompleto
n123
9Nome
x + y_2 + 42
```

**Saída esperada:**

```text
NUM(13)                @1:1
ID(nomeCompleto)       @2:1
ID(n123)               @3:1
NUM(9)                 @4:1
ID(Nome)               @4:2
ID(x)                  @5:1
PLUS(+)                @5:3
ID(y_2)                @5:5
PLUS(+)                @5:9
NUM(42)                @5:11
Total: 10 token(s) {ID=5, NUM=3, PLUS=2}
```


### 7.2 Programas com erro

#### `erro_1.txt`

```text
total + 10
valor = 3
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro léxico [2:7]: caractere inválido '='
```


Escreva também **pelo menos 2 testes `ok_` e 2 testes `erro_` seus**, cobrindo casos que estes não cobrem.

## 8. Desafios extras (opcionais)

- Aceite também `-`, `*` e `/` e comentários `// ...`.
- Mostre, para cada identificador, as linhas em que ele aparece (uma "tabela de referências cruzadas").

## 9. Critérios de avaliação

| Critério | Peso |
|---|---|
| Análise léxica (tokens, posições, erros léxicos) | 20% |
| Análise sintática (gramática sem conflitos / avisos, precedência, erros sintáticos) | 25% |
| Semântica e back-end (regras da seção 4, saída da seção 5) | 35% |
| Mensagens de erro (tipo, `[linha:coluna]`, clareza) | 10% |
| Testes próprios e README (como compilar, executar e testar) | 10% |
