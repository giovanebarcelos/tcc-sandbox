# Enunciado 12 — Interpretador de comandos de robô

**Teoria da Computação e Compiladores — 6M (Digital) · Prof. Giovane Barcelos**

| | |
|---|---|
| **Nível** | ⭐⭐ (básico) |
| **Aulas relacionadas** | 21 |
| **Ferramentas** | ANTLR 4 **ou** JFlex + JCup (implementar com as duas vale como desafio extra) |
| **Testes fornecidos** | 2 válido(s) (`ok_*`) e 3 inválido(s) (`erro_*`), na pasta [`testes/`](testes/) |

> Leia antes as **regras gerais** em [`../README.md`](../README.md): formato das mensagens de erro, códigos de
> saída, como usar o `testar.sh`, o que entregar e como será avaliado.

---

## 1. Objetivo

Um **interpretador**: os comandos movem um robô num plano. O robô começa em (0, 0) virado para o
**Norte**. No fim, o programa mostra a posição final e um **mapa ASCII** do caminho percorrido.

## 2. Análise léxica

`MOVE LEFT RIGHT REPORT LOOP TIMES`, `NUMBER` (inteiro), `{ }` e comentários `// ...`. Outros caracteres são erro léxico.

## 3. Análise sintática

```ebnf
programa ::= comando { comando } EOF
comando  ::= "MOVE" [ NUMBER ] | "LEFT" | "RIGHT" | "REPORT"
           | "LOOP" NUMBER "TIMES" "{" comando { comando } "}"
```
Laços podem ser aninhados.

## 4. Análise semântica e regras da linguagem

- `MOVE` anda 1 passo; `MOVE n` anda `n` passos na direção atual; `LEFT`/`RIGHT` giram 90°.
- `REPORT` imprime a posição e a direção atuais.
- `LOOP 0 TIMES` é **erro semântico**.

## 5. Saída

As linhas dos `REPORT`, a posição final com a direção (N, L, S, O) e o número de passos, e o mapa
(`S` = início, `R` = robô, `*` = caminho, `.` = vazio), com o Norte para cima. Veja os casos de teste.

## 6. Dicas

- Guarde o conjunto de células visitadas e, no fim, desenhe o retângulo mínimo que as contém.

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
MOVE
LEFT
LOOP 3 TIMES {
    MOVE
    RIGHT
    MOVE
}
RIGHT
MOVE
```

**Saída esperada:**

```text
Posição final: (0, 2) virado para O — 8 passo(s)
Mapa (S = início, R = robô, * = caminho):
  * * *
  * R *
  * * .
  . S .
```

#### `ok_2.txt`

**Entrada:**

```text
// desenha um quadrado 3x3 e depois uma escada
LOOP 4 TIMES { MOVE 3 RIGHT }
REPORT
LOOP 2 TIMES {
    LOOP 2 TIMES { MOVE }
    RIGHT MOVE LEFT
}
REPORT
```

**Saída esperada:**

```text
REPORT: (0, 0) N
REPORT: (2, 4) N
Posição final: (2, 4) virado para N — 18 passo(s)
Mapa (S = início, R = robô, * = caminho):
  . * R .
  * * * *
  * * . *
  * . . *
  S * * *
```


### 7.2 Programas com erro

#### `erro_1.txt`

```text
MOVE
LEFT
LOOP 3 TIMES
    MOVE
    RIGHT
    MOVE
}
RIGHT
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro sintático [4:5]: token inesperado 'MOVE'
```

#### `erro_2.txt`

```text
LOOP 0 TIMES { MOVE }
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro semântico [1:1]: LOOP com 0 repetições
```

#### `erro_3.txt`

```text
MOVE
JUMP
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro léxico [2:1]: caractere inválido 'J'
```


Escreva também **pelo menos 2 testes `ok_` e 2 testes `erro_` seus**, cobrindo casos que estes não cobrem.

## 8. Desafios extras (opcionais)

- Obstáculos (`WALL x, y`) com erro de execução ao bater.
- Procedimentos: `DEF quadrado { ... }` e `CALL quadrado`.

## 9. Critérios de avaliação

| Critério | Peso |
|---|---|
| Análise léxica (tokens, posições, erros léxicos) | 20% |
| Análise sintática (gramática sem conflitos / avisos, precedência, erros sintáticos) | 25% |
| Semântica e back-end (regras da seção 4, saída da seção 5) | 35% |
| Mensagens de erro (tipo, `[linha:coluna]`, clareza) | 10% |
| Testes próprios e README (como compilar, executar e testar) | 10% |
