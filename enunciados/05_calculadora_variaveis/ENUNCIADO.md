# Enunciado 05 — Calculadora com variáveis

**Teoria da Computação e Compiladores — 6M (Digital) · Prof. Giovane Barcelos**

| | |
|---|---|
| **Nível** | ⭐⭐ (básico) |
| **Aulas relacionadas** | 21 (JFlex + JCup / ANTLR) e 22 (tabela de símbolos) |
| **Ferramentas** | ANTLR 4 **ou** JFlex + JCup (implementar com as duas vale como desafio extra) |
| **Testes fornecidos** | 1 válido(s) (`ok_*`) e 2 inválido(s) (`erro_*`), na pasta [`testes/`](testes/) |

> Leia antes as **regras gerais** em [`../README.md`](../README.md): formato das mensagens de erro, códigos de
> saída, como usar o `testar.sh`, o que entregar e como será avaliado.

---

## 1. Objetivo

Estender a calculadora com **atribuições**, o comando `print` e a potência `^`. Para guardar os valores
das variáveis é preciso uma **tabela de símbolos**. A potência introduz a **associatividade à direita**.

## 2. Análise léxica

- `ID`: letra ou `_`, seguida de letras, dígitos ou `_`; palavra-chave `print`.
- `NUM`: inteiro ou real (`3`, `2.5`).
- Operadores `+ - * / % ^ =`, parênteses e `;`. Comentários `# ...` até o fim da linha.

## 3. Análise sintática

```ebnf
programa ::= { comando } EOF
comando  ::= ID "=" expr ";"  |  "print" expr ";"
expr     ::= expr "^" expr | "-" expr | expr ("*"|"/"|"%") expr | expr ("+"|"-") expr
           | NUM | ID | "(" expr ")"
```

| Precedência | Operador | Associatividade |
|---|---|---|
| 1 (maior) | `^` | **direita**: `2^3^2 = 2^(3^2) = 512` |
| 2 | `-` unário | `-2^2 = -(2^2) = -4` |
| 3 | `*` `/` `%` | esquerda |
| 4 | `+` `-` | esquerda |

## 4. Análise semântica e regras da linguagem

- Atribuição cria ou atualiza a variável na tabela de símbolos.
- Usar uma variável **antes de ela receber valor** é **erro semântico**.
- Resultados inteiros são impressos sem casas decimais (`512`, não `512.0`).

## 5. Saída

- Cada atribuição imprime `variável <- valor`; cada `print` imprime `print: valor`.
- No final, a **tabela de símbolos** com o valor final de cada variável (veja o formato nos casos de teste).

## 6. Dicas

- No ANTLR, `<assoc=right>` na alternativa da potência. No JCup, `precedence right POT;`.

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
# variáveis e precedência
x = 3;
y = 2 ^ 3 ^ 2;        # associativo à direita: 2^(3^2) = 512
print y;
print -2 ^ 2;         # -(2^2) = -4
z = (x + 1) * 2.5 - 10 % 4;
print z / 2;
x = x + 1;            # redefinição
print x * x;
```

**Saída esperada:**

```text
x <- 3
y <- 512
print: 512
print: -4
z <- 8
print: 4
x <- 4
print: 16
Tabela de símbolos:
  x = 4
  y = 512
  z = 8
```


### 7.2 Programas com erro

#### `erro_1.txt`

```text
a = 1;
print a + b;
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro semântico [2:11]: variável 'b' usada antes de ser definida
```

#### `erro_2.txt`

```text
a = 1
print a;
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro sintático [2:1]: token inesperado 'print'
```


Escreva também **pelo menos 2 testes `ok_` e 2 testes `erro_` seus**, cobrindo casos que estes não cobrem.

## 8. Desafios extras (opcionais)

- Funções embutidas: `sqrt(x)`, `abs(x)`, `max(a, b)`.
- Aviso para variáveis atribuídas e nunca usadas.

## 9. Critérios de avaliação

| Critério | Peso |
|---|---|
| Análise léxica (tokens, posições, erros léxicos) | 20% |
| Análise sintática (gramática sem conflitos / avisos, precedência, erros sintáticos) | 25% |
| Semântica e back-end (regras da seção 4, saída da seção 5) | 35% |
| Mensagens de erro (tipo, `[linha:coluna]`, clareza) | 10% |
| Testes próprios e README (como compilar, executar e testar) | 10% |
