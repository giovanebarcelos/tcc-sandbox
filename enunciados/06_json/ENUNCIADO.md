# Enunciado 06 — Analisador de JSON

**Teoria da Computação e Compiladores — 6M (Digital) · Prof. Giovane Barcelos**

| | |
|---|---|
| **Nível** | ⭐⭐ (básico) |
| **Aulas relacionadas** | 17 e 21 |
| **Ferramentas** | ANTLR 4 **ou** JFlex + JCup (implementar com as duas vale como desafio extra) |
| **Testes fornecidos** | 2 válido(s) (`ok_*`) e 3 inválido(s) (`erro_*`), na pasta [`testes/`](testes/) |

> Leia antes as **regras gerais** em [`../README.md`](../README.md): formato das mensagens de erro, códigos de
> saída, como usar o `testar.sh`, o que entregar e como será avaliado.

---

## 1. Objetivo

Validar e **formatar** documentos JSON e calcular estatísticas sobre eles. É um exercício de
gramática com **listas separadas por vírgula** (objetos e arrays), um padrão que aparece em toda
linguagem de programação.

## 2. Análise léxica

Conforme a especificação JSON (json.org):
- `STRING`: entre aspas duplas, com escapes `\" \\ \/ \b \f \n \r \t \uXXXX`; caracteres de controle não são permitidos;
- `NUMBER`: `-`? (`0` ou dígito não nulo seguido de dígitos), fração `.dígitos` opcional e expoente `e`/`E` opcional;
- `true`, `false`, `null`, `{ } [ ] : ,`; espaços são ignorados.

## 3. Análise sintática

```ebnf
json   ::= valor EOF
valor  ::= objeto | array | STRING | NUMBER | "true" | "false" | "null"
objeto ::= "{" [ par { "," par } ] "}"
par    ::= STRING ":" valor
array  ::= "[" [ valor { "," valor } ] "]"
```
Vírgula sobrando (`[1, 2,]`) ou faltando é erro sintático.

## 4. Análise semântica e regras da linguagem

- **Chave duplicada** no mesmo objeto é erro semântico (`chave duplicada "a"`).

## 5. Saída

- O documento formatado com indentação de 2 espaços.
- Estatísticas: quantos valores de cada tipo (objeto, array, string, número, booleano, null) e a profundidade
  máxima (veja os casos de teste).

## 6. Dicas

- ANTLR: `(x (',' x)*)?`. JCup: recursão à esquerda `lista ::= lista VIRG x | x` mais uma produção vazia.

## 7. Casos de teste

O `testar.sh` compara a saída padrão inteira, linha a linha. Os testes `erro_*` passam se o compilador terminar com código de saída
diferente de 0 e escrever uma mensagem na saída de erro; o texto da mensagem de referência é um **modelo**
(o tipo do erro e a posição `[linha:coluna]` devem bater; a redação pode variar).

```bash
../testar.sh . <comando que executa o seu compilador>
# ex.: ../testar.sh . java -cp "build/classes:$ANTLR_JAR" Main
```

### 7.1 Programas válidos

#### `ok_1.json`

**Entrada:**

```text
{
   "name": "Muttley",
   "age": 36,
   "grades": [100, 30.6, 90.8],
   "passed": true
}
```

**Saída esperada:**

```text
{
  "name": "Muttley",
  "age": 36,
  "grades": [
    100,
    30.6,
    90.8
  ],
  "passed": true
}
Estatísticas: {objeto=1, array=1, STRING=1, NUMBER=4, BOOL=1, NULL=0}, profundidade máxima = 2
```

#### `ok_2.json`

**Entrada:**

```text
{"aluno": {"nome": "Dick Vigarista", "ra": null, "notas": [], "endereco": {}},
 "turmas": [{"id": 1, "tags": ["tcc", "6M"]}, {"id": 2, "media": -7.5e-1}], "escape": "linha\n\"aspas\""}
```

**Saída esperada:**

```text
{
  "aluno": {
    "nome": "Dick Vigarista",
    "ra": null,
    "notas": [],
    "endereco": {}
  },
  "turmas": [
    {
      "id": 1,
      "tags": [
        "tcc",
        "6M"
      ]
    },
    {
      "id": 2,
      "media": -7.5e-1
    }
  ],
  "escape": "linha\n\"aspas\""
}
Estatísticas: {objeto=5, array=3, STRING=4, NUMBER=3, BOOL=0, NULL=1}, profundidade máxima = 4
```


### 7.2 Programas com erro

#### `erro_1.json`

```text
{
   "name": "Muttley"
   "age": 36,
   "grades": [100, 30.6, 90.8],
   "passed": true
}
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro sintático [3:4]: token inesperado '"age"'
```

#### `erro_2.json`

```text
{"a": 1, "b": [1, 2,], "c": 3}
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro sintático [1:21]: token inesperado ']'
```

#### `erro_3.json`

```text
{"a": 1, "b": 2, "a": 3}
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro semântico [1:18]: chave duplicada "a"
```


Escreva também **pelo menos 2 testes `ok_` e 2 testes `erro_` seus**, cobrindo casos que estes não cobrem.

## 8. Desafios extras (opcionais)

- Validar o documento contra um "esquema" simples (ex.: todo objeto de `alunos` precisa de `nome`).
- Converter o JSON para XML ou YAML.

## 9. Critérios de avaliação

| Critério | Peso |
|---|---|
| Análise léxica (tokens, posições, erros léxicos) | 20% |
| Análise sintática (gramática sem conflitos / avisos, precedência, erros sintáticos) | 25% |
| Semântica e back-end (regras da seção 4, saída da seção 5) | 35% |
| Mensagens de erro (tipo, `[linha:coluna]`, clareza) | 10% |
| Testes próprios e README (como compilar, executar e testar) | 10% |
