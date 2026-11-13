# Enunciado 03 — Scanner completo

**Teoria da Computação e Compiladores — 6M (Digital) · Prof. Giovane Barcelos**

| | |
|---|---|
| **Nível** | ⭐⭐ (básico) |
| **Aulas relacionadas** | 16 e 17 |
| **Ferramentas** | ANTLR 4 **ou** JFlex + JCup (implementar com as duas vale como desafio extra) |
| **Testes fornecidos** | 1 válido(s) (`ok_*`) e 3 inválido(s) (`erro_*`), na pasta [`testes/`](testes/) |

> Leia antes as **regras gerais** em [`../README.md`](../README.md): formato das mensagens de erro, códigos de
> saída, como usar o `testar.sh`, o que entregar e como será avaliado.

---

## 1. Objetivo

Construir o scanner de uma linguagem "de verdade": palavras-chave, números inteiros e reais (com
expoente), strings com sequências de escape, comentários de linha e de bloco e operadores de dois
caracteres. Aqui aparecem as decisões clássicas do léxico: **palavra-chave × identificador**,
**match mais longo** (`==` e não `=` `=`) e **estados léxicos** para strings e comentários.

## 2. Análise léxica

| Token | Lexema |
|---|---|
| palavras-chave | `int float string if else while print return` (têm prioridade sobre `ID`) |
| `ID` | letra ou `_`, seguida de letras, dígitos ou `_` |
| `NUM_INT` | dígitos (`42`) |
| `NUM_REAL` | dígitos `.` dígitos, com expoente opcional: `3.14`, `2.5e-3`, `1.0E10` |
| `TEXTO` | entre aspas duplas, na mesma linha; escapes `\n` `\t` `\"` `\\` |
| operadores de 2 caracteres | `==` `!=` `<=` `>=` `&&` `\|\|` |
| operadores de 1 caractere | `=` `<` `>` `!` `+` `-` `*` `/` `%` |
| pontuação | `;` `,` `(` `)` `{` `}` |
| (ignorados) | espaços, `// comentário até o fim da linha`, `/* comentário de bloco */` |

**Erros léxicos** (com mensagem própria): string não fechada, comentário não fechado e caractere inválido.

## 3. Análise sintática

```ebnf
programa ::= { token } EOF        (* o foco é o léxico *)
```

## 4. Análise semântica e regras da linguagem

O valor do token `TEXTO` deve ser a string **já decodificada** (sem as aspas e com os escapes
convertidos), mas a saída mostra o lexema original.

## 5. Saída

Uma linha por token: `TIPO('lexema')@linha:coluna` (o mesmo formato da Aula 17); para strings, o lexema
original aparece entre aspas duplas: `TEXTO("a\"b")@3:1`. No fim, um resumo por categoria
(`Resumo: {palavra-chave=2, identificador=12, ...}`). Veja os casos de teste.

## 6. Dicas

- **JFlex**: use `%state STRING COMENTARIO` e acumule a string num `StringBuilder`.
- **ANTLR**: use `fragment`, o operador não-guloso `.*?` e regras extras (`TEXTO_ABERTO`,
  `COMENTARIO_ABERTO`) que só vencem quando o fechamento não existe.
- A posição de um erro de string/comentário não fechado é a posição de **início** da string/comentário.

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
// programa de exemplo
int x = 42;
float media = 1.5e2 / 3.0;   /* comentário
                               de várias linhas */
string msg = "Olá, \"mundo\"!\n";
if (x >= 10 && media != 0) { print(msg); } else { x = x % 2; }
while (!(x == 0) || x <= -1) x = x - 1;
return x;
```

**Saída esperada:**

```text
INT('int')@2:1
ID('x')@2:5
ASSIGN('=')@2:7
NUM_INT('42')@2:9
SEMI(';')@2:11
FLOAT('float')@3:1
ID('media')@3:7
ASSIGN('=')@3:13
NUM_REAL('1.5e2')@3:15
DIV('/')@3:21
NUM_REAL('3.0')@3:23
SEMI(';')@3:26
STR('string')@5:1
ID('msg')@5:8
ASSIGN('=')@5:12
TEXTO("Olá, \"mundo\"!\n")@5:14
SEMI(';')@5:33
IF('if')@6:1
LPAREN('(')@6:4
ID('x')@6:5
GE('>=')@6:7
NUM_INT('10')@6:10
AND('&&')@6:13
ID('media')@6:16
NE('!=')@6:22
NUM_INT('0')@6:25
RPAREN(')')@6:26
LBRACE('{')@6:28
PRINT('print')@6:30
LPAREN('(')@6:35
ID('msg')@6:36
RPAREN(')')@6:39
SEMI(';')@6:40
RBRACE('}')@6:42
ELSE('else')@6:44
LBRACE('{')@6:49
ID('x')@6:51
ASSIGN('=')@6:53
ID('x')@6:55
MOD('%')@6:57
NUM_INT('2')@6:59
SEMI(';')@6:60
RBRACE('}')@6:62
WHILE('while')@7:1
LPAREN('(')@7:7
NOT('!')@7:8
LPAREN('(')@7:9
ID('x')@7:10
EQ('==')@7:12
NUM_INT('0')@7:15
RPAREN(')')@7:16
OR('||')@7:18
ID('x')@7:21
LE('<=')@7:23
MINUS('-')@7:26
NUM_INT('1')@7:27
RPAREN(')')@7:28
ID('x')@7:30
ASSIGN('=')@7:32
ID('x')@7:34
MINUS('-')@7:36
NUM_INT('1')@7:38
SEMI(';')@7:39
RETURN('return')@8:1
ID('x')@8:8
SEMI(';')@8:9
Resumo: {palavra-chave=8, identificador=13, número=9, texto=1, operador/pontuação=35}
```


### 7.2 Programas com erro

#### `erro_1.txt`

```text
int x = 1;
string s = "sem fim;
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro léxico [2:12]: string não fechada
```

#### `erro_2.txt`

```text
int y = 2; /* comentário que nunca fecha
print(y);
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro léxico [1:12]: comentário não fechado
```

#### `erro_3.txt`

```text
int z = 3 # 4;
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro léxico [1:11]: caractere inválido '#'
```


Escreva também **pelo menos 2 testes `ok_` e 2 testes `erro_` seus**, cobrindo casos que estes não cobrem.

## 8. Desafios extras (opcionais)

- Aceite números hexadecimais (`0x1F`) e caracteres (`'a'`, `'\n'`).
- Comentários de bloco aninhados `/* ... /* ... */ ... */` (dica: contador no estado `COMENTARIO`).

## 9. Critérios de avaliação

| Critério | Peso |
|---|---|
| Análise léxica (tokens, posições, erros léxicos) | 20% |
| Análise sintática (gramática sem conflitos / avisos, precedência, erros sintáticos) | 25% |
| Semântica e back-end (regras da seção 4, saída da seção 5) | 35% |
| Mensagens de erro (tipo, `[linha:coluna]`, clareza) | 10% |
| Testes próprios e README (como compilar, executar e testar) | 10% |
