# Enunciado 28 — Verificação estática de tipos

**Teoria da Computação e Compiladores — 6M (Digital) · Prof. Giovane Barcelos**

| | |
|---|---|
| **Nível** | ⭐⭐⭐ (intermediário) |
| **Aulas relacionadas** | 22 |
| **Ferramentas** | ANTLR 4 **ou** JFlex + JCup (implementar com as duas vale como desafio extra) |
| **Testes fornecidos** | 1 válido(s) (`ok_*`) e 6 inválido(s) (`erro_*`), na pasta [`testes/`](testes/) |

> Leia antes as **regras gerais** em [`../README.md`](../README.md): formato das mensagens de erro, códigos de
> saída, como usar o `testar.sh`, o que entregar e como será avaliado.

---

## 1. Objetivo

Um **verificador de tipos estático** para uma linguagem com `int`, `float`, `bool` e `string`. Além de
rejeitar programas mal tipados, o verificador **reescreve** o programa com todas as conversões implícitas
tornadas explícitas (`i2f(...)`, `str(...)`).

## 2. Análise léxica

`int float bool string if else while print true false`, `ID`, literais inteiros, reais (`2.5`) e de texto,
operadores `+ - * / % < > <= >= == != && || !`, `= ( ) { } ;`; comentários `// ...`.

## 3. Análise sintática

```ebnf
programa ::= { comando } EOF
comando  ::= tipo ID "=" expr ";" | ID "=" expr ";" | "if" "(" expr ")" bloco [ "else" bloco ]
           | "while" "(" expr ")" bloco | "print" "(" expr ")" ";" | bloco
tipo     ::= "int" | "float" | "bool" | "string"
expr     ::= ("-" | "!") expr | expr op expr | INT | FLOAT | STRING | "true" | "false" | ID | "(" expr ")"
```

## 4. Análise semântica e regras da linguagem

- `int → float` é **alargamento** implícito, mostrado como `i2f(...)`. `float → int` é **erro** (estreitamento).
- Aritmética: `int op int → int` (inclusive `/`, divisão inteira!); com algum `float` → `float`.
- `+` com uma `string` concatena; o outro lado é convertido com `str(...)`. Os outros operadores não se aplicam a `string`.
- `%` só aceita `int`. Comparações retornam `bool`; `&& || !` exigem `bool`.
- Condições de `if`/`while` devem ser `bool`. Escopo de bloco; variável não declarada é erro.

## 5. Saída

O programa reescrito (tipado, com conversões explícitas e indentação) e a tabela de variáveis com os tipos.
Veja os casos de teste — atenção para `float m = x / 4;`, que é uma divisão **inteira**.

## 6. Dicas

- Faça `tipo(expr)` devolver o tipo **e** a expressão reescrita.

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
int x = 10;
float media = x / 4;          // int / int = int (2!) e depois alargado para float
float y = x / 4.0;            // x é alargado: i2f(x) / 4.0
string s = "y = " + y;        // concatenação com conversão str(...)
bool ok = y >= 2.5 && !(x == 0);
if (ok) {
    print(s);
    int x = 3;                // outro x, no escopo do bloco
    print(x * 2.0);
} else {
    print("não ok");
}
while (x > 0) { x = x - 3; }
```

**Saída esperada:**

```text
=== Programa tipado (conversões explícitas) ===
    int x = 10;
    float media = i2f((x / 4));
    float y = (i2f(x) / 4.0);
    string s = ("y = " + str(y));
    bool ok = ((y >= 2.5) && !(x == 0));
    if (ok)
    {
        print(s);
        int x = 3;
        print((i2f(x) * 2.0));
    }
    else
    {
        print("não ok");
    }
    while (x > 0)
    {
        x = (x - 3);
    }
=== Tabela de símbolos ===
  x : int
  media : float
  y : float
  s : string
  ok : bool
  x (bloco, linha 8) : int
Verificação de tipos: OK — 31 expressão(ões) tipada(s), 4 conversão(ões) implícita(s) inserida(s)
```


### 7.2 Programas com erro

#### `erro_1.txt`

```text
int z = 2.5;
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro semântico [1:9]: tipos incompatíveis na inicialização de 'z': esperado int, encontrado float (estreitamento float -> int não é permitido)
```

#### `erro_2.txt`

```text
int x = 1;
if (x) { print(x); }
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro semântico [2:5]: condição do if deve ser bool, mas é int
```

#### `erro_3.txt`

```text
string t = "a" - "b";
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro semântico [1:16]: operador '-' não se aplica a string e string
```

#### `erro_4.txt`

```text
bool b = true;
b = 1;
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro semântico [2:5]: tipos incompatíveis na atribuição a 'b': esperado bool, encontrado int
```

#### `erro_5.txt`

```text
int x = 1;
{ int y = 2; }
print(y);
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro semântico [3:7]: variável 'y' não declarada
```

#### `erro_6.txt`

```text
float f = 1.5;
int r = 7 % f;
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro semântico [2:11]: operador '%' não se aplica a int e float
```


Escreva também **pelo menos 2 testes `ok_` e 2 testes `erro_` seus**, cobrindo casos que estes não cobrem.

## 8. Desafios extras (opcionais)

- Avisar a divisão inteira quando o resultado é atribuído a `float`.
- Tipo `char` e conversões explícitas com cast `(int) x`.

## 9. Critérios de avaliação

| Critério | Peso |
|---|---|
| Análise léxica (tokens, posições, erros léxicos) | 20% |
| Análise sintática (gramática sem conflitos / avisos, precedência, erros sintáticos) | 25% |
| Semântica e back-end (regras da seção 4, saída da seção 5) | 35% |
| Mensagens de erro (tipo, `[linha:coluna]`, clareza) | 10% |
| Testes próprios e README (como compilar, executar e testar) | 10% |
