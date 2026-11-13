# Enunciado 14 — Mini-Pascal: análise semântica

**Teoria da Computação e Compiladores — 6M (Digital) · Prof. Giovane Barcelos**

| | |
|---|---|
| **Nível** | ⭐⭐⭐ (intermediário) |
| **Aulas relacionadas** | 22 (semântica e tabela de símbolos) |
| **Ferramentas** | ANTLR 4 **ou** JFlex + JCup (implementar com as duas vale como desafio extra) |
| **Testes fornecidos** | 2 válido(s) (`ok_*`) e 5 inválido(s) (`erro_*`), na pasta [`testes/`](testes/) |

> Leia antes as **regras gerais** em [`../README.md`](../README.md): formato das mensagens de erro, códigos de
> saída, como usar o `testar.sh`, o que entregar e como será avaliado.

---

## 1. Objetivo

Fazer a **análise semântica** de um mini-Pascal com variáveis tipadas (`int` e `float`). O resultado é a
**tabela de símbolos** com tipo, linha da declaração e número de usos de cada variável, além dos erros e
avisos semânticos.

## 2. Análise léxica

Palavras-chave `program begin end int float if then else while do read write`; `ID`; `NUMBER` (inteiro);
`REAL` (`3.5`); `= + - * / == != < > <= >= ( ) { } ; ,`; comentários `// ...`.

## 3. Análise sintática

```ebnf
programa ::= "program" ID ";" { decl } "begin" { stmt } "end" EOF
decl     ::= ("int" | "float") ID { "," ID } ";"
stmt     ::= ID "=" expr ";" | "read" "(" ID ")" ";" | "write" "(" expr ")" ";"
           | "if" cond "then" stmt [ "else" stmt ] | "while" cond "do" stmt | "{" { stmt } "}"
cond     ::= expr ("==" | "!=" | "<" | ">" | "<=" | ">=") expr
expr     ::= "-" expr | expr ("*" | "/") expr | expr ("+" | "-") expr | NUMBER | REAL | ID | "(" expr ")"
```
**Desafio do JCup**: resolva o *dangling else* **sem declarações de precedência**, com a técnica dos comandos
**casados/não casados** (*matched/unmatched*).

## 4. Análise semântica e regras da linguagem

- Variável declarada duas vezes: erro (`variável 'x' já declarada na linha 2`).
- Variável não declarada: erro.
- Atribuir expressão `float` a variável `int` é erro (perda de precisão); `int` → `float` é permitido.
- O tipo de uma expressão é `float` se algum operando for `float`.
- Variável declarada e **nunca usada** gera um **aviso** (não é erro).

## 5. Saída

Mensagem de sucesso, a tabela de símbolos (nome, tipo, linha, usos) e os avisos. Veja os casos de teste.

## 6. Dicas

- Conte como "uso" toda ocorrência da variável em comandos (leitura ou escrita).

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
program calculadora;
int x, y, resultado;
float media;
begin
    read(x);
    read(y);
    resultado = x + y;
    write(resultado);
    media = resultado / 2;
    write(media);
    if x > y then
        write(x);
    else
        write(y);
    while x < 10 do {
        x = x + 1;
        write(x);
    }
end
```

**Saída esperada:**

```text
Programa 'calculadora': análise semântica concluída sem erros.
Tabela de símbolos:
  nome         tipo   linha  usos
  x            int    2      8
  y            int    2      4
  resultado    int    2      3
  media        float  3      2
```

#### `ok_2.txt`

**Entrada:**

```text
program fatorial;
int n, fat, i;
float taxa, naoUsada;
begin
    read(n);
    fat = 1;
    i = 1;
    taxa = 0.5 * n;
    while i <= n do {
        fat = fat * i;
        i = i + 1;
    }
    write(fat);
    if n == 0 then
        write(0);
    if n != 0 then {
        if n > 0 then
            write(1);
        else
            write(-1);
    }
end
```

**Saída esperada:**

```text
Programa 'fatorial': análise semântica concluída sem erros.
Tabela de símbolos:
  nome         tipo   linha  usos
  n            int    2      6
  fat          int    2      4
  i            int    2      5
  taxa         float  3      1
  naoUsada     float  3      0
Aviso: variável 'naoUsada' declarada e nunca usada
```


### 7.2 Programas com erro

#### `erro_1.txt`

```text
program teste_erros;
int x, y;
float @valor;  // ERRO LÉXICO: caractere '@' inválido
begin
    read(x);
end
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro léxico [3:7]: caractere inválido '@'
```

#### `erro_2.txt`

```text
program p;
int x;
begin
    y = x + 5;
end
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro semântico [4:5]: variável 'y' não declarada
```

#### `erro_3.txt`

```text
program p;
int x;
float f;
begin
    f = 2.5;
    x = f * 2;
end
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro semântico [6:5]: atribuição de float para a variável int 'x' (perda de precisão)
```

#### `erro_4.txt`

```text
program p;
int x;
float x;
begin
end
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro semântico [3:7]: variável 'x' já declarada na linha 2
```

#### `erro_5.txt`

```text
program p;
int x, y;
begin
    read(x);
    y = x + 5
    write(y);
end
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro sintático [6:5]: token inesperado 'write'
```


Escreva também **pelo menos 2 testes `ok_` e 2 testes `erro_` seus**, cobrindo casos que estes não cobrem.

## 8. Desafios extras (opcionais)

- Interpretar o programa (com `read` lendo da entrada padrão).
- Tipo `bool` e operadores `and`, `or`, `not`.

## 9. Critérios de avaliação

| Critério | Peso |
|---|---|
| Análise léxica (tokens, posições, erros léxicos) | 20% |
| Análise sintática (gramática sem conflitos / avisos, precedência, erros sintáticos) | 25% |
| Semântica e back-end (regras da seção 4, saída da seção 5) | 35% |
| Mensagens de erro (tipo, `[linha:coluna]`, clareza) | 10% |
| Testes próprios e README (como compilar, executar e testar) | 10% |
