# Enunciado 29 — Recuperação de erros

**Teoria da Computação e Compiladores — 6M (Digital) · Prof. Giovane Barcelos**

| | |
|---|---|
| **Nível** | ⭐⭐⭐ (intermediário) |
| **Aulas relacionadas** | 24 (controle de erros) |
| **Ferramentas** | ANTLR 4 **ou** JFlex + JCup (implementar com as duas vale como desafio extra) |
| **Testes fornecidos** | 1 válido(s) (`ok_*`) e 3 inválido(s) (`erro_*`), na pasta [`testes/`](testes/) |

> Leia antes as **regras gerais** em [`../README.md`](../README.md): formato das mensagens de erro, códigos de
> saída, como usar o `testar.sh`, o que entregar e como será avaliado.

---

## 1. Objetivo

Um compilador que **não para no primeiro erro**: depois de um erro léxico ou sintático ele se **recupera**
e continua analisando, executa os comandos válidos e, no fim, lista **todos** os erros encontrados.

## 2. Análise léxica

`print`, `ID`, `NUM`, `= + - * / ( ) ;`, comentários `// ...`. Caractere inválido é registrado e **descartado**.

## 3. Análise sintática

```ebnf
programa ::= { comando } EOF
comando  ::= ID "=" expr ";" | "print" expr ";"
expr     ::= expr ("*" | "/") expr | expr ("+" | "-") expr | NUM | ID | "(" expr ")"
```

## 4. Análise semântica e regras da linguagem

- **JCup**: recuperação em **modo pânico** com a produção `comando ::= error ";"` (descarta tokens até o `;`).
- **ANTLR**: listeners que só **registram** o erro, e a estratégia padrão de recuperação (inserção/remoção de token).
- Comandos válidos são executados (variável sem valor vale erro semântico registrado, não fatal).
- Se houve algum erro, o código de saída é 1 e a mensagem final lista todos, na ordem.

## 5. Saída

A saída dos comandos válidos e a contagem de comandos válidos e com erro. Em caso de erro, na saída de erro:
`Erro (recuperação): N erro(s) encontrado(s)` seguido de um erro por linha.

## 6. Dicas

- A quantidade de erros reportados pode variar com a estratégia de recuperação (o teste `erro_3` mostra uma
  diferença real entre as ferramentas). O importante é **continuar** e reportar mais de um erro quando houver.
- As mensagens de referência foram geradas pela solução em ANTLR (`mismatched input ...`, `missing ')'`); com o
  JCup, o texto depois de `erro sintático:` será diferente.

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
x = 10;
y = x * 2 + 1;
print y;
print (x + y) / 3;
```

**Saída esperada:**

```text
print: 21
print: 10
4 comando(s) válido(s), 0 comando(s) com erro recuperado(s)
```


### 7.2 Programas com erro

#### `erro_1.txt`

```text
x = 10;
y = x * ;              // erro 1: expressão incompleta
print x + 1;
z = (x + 2;            // erro 2: falta ')'
w = 5;
print w * w;
print 7 7;             // erro 3: falta operador
print x - w;
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro (recuperação): 3 erro(s) encontrado(s) — a análise continuou após cada um:
  [2:9] erro sintático: mismatched input ';' expecting {'(', ID, NUM}
  [4:11] erro sintático: missing ')' at ';'
  [7:9] erro sintático: extraneous input '7' expecting ';'
```

#### `erro_2.txt`

```text
a = 1;
b = a # 2;             // '#' inválido: descartado pelo scanner, mas sobra "a 2"
print a + 1;
c = a @ ;              // '@' descartado; depois falta o operando
print a * 3;
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro (recuperação): 3 erro(s) encontrado(s) — a análise continuou após cada um:
  [2:7] erro léxico: caractere inválido '#' (descartado)
  [2:9] erro sintático: extraneous input '2' expecting ';'
  [4:7] erro léxico: caractere inválido '@' (descartado)
```

#### `erro_3.txt`

```text
a = 1;
b = a +
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro (recuperação): 1 erro(s) encontrado(s) — a análise continuou após cada um:
  [3:1] erro sintático: mismatched input '<EOF>' expecting {'(', ID, NUM}
```


Escreva também **pelo menos 2 testes `ok_` e 2 testes `erro_` seus**, cobrindo casos que estes não cobrem.

## 8. Desafios extras (opcionais)

- Recuperação também no nível da expressão (`expr ::= error`).
- Sugerir a correção (*"faltou ';' na linha 3?"*).

## 9. Critérios de avaliação

| Critério | Peso |
|---|---|
| Análise léxica (tokens, posições, erros léxicos) | 20% |
| Análise sintática (gramática sem conflitos / avisos, precedência, erros sintáticos) | 25% |
| Semântica e back-end (regras da seção 4, saída da seção 5) | 35% |
| Mensagens de erro (tipo, `[linha:coluna]`, clareza) | 10% |
| Testes próprios e README (como compilar, executar e testar) | 10% |
