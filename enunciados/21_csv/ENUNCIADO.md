# Enunciado 21 — CSV → tabela, estatísticas e JSON

**Teoria da Computação e Compiladores — 6M (Digital) · Prof. Giovane Barcelos**

| | |
|---|---|
| **Nível** | ⭐⭐ (básico) |
| **Aulas relacionadas** | 17 e 18 (produções ε) |
| **Ferramentas** | ANTLR 4 **ou** JFlex + JCup (implementar com as duas vale como desafio extra) |
| **Testes fornecidos** | 2 válido(s) (`ok_*`) e 4 inválido(s) (`erro_*`), na pasta [`testes/`](testes/) |

> Leia antes as **regras gerais** em [`../README.md`](../README.md): formato das mensagens de erro, códigos de
> saída, como usar o `testar.sh`, o que entregar e como será avaliado.

---

## 1. Objetivo

Analisar arquivos **CSV** com campos entre aspas (que podem conter vírgulas e aspas duplicadas `""`) e
**campos vazios**. O programa mostra o conteúdo como tabela, calcula estatísticas das colunas numéricas e
converte para **JSON**. A gramática tem uma produção **ε** (o campo vazio).

## 2. Análise léxica

- `TEXTO`: sequência sem `,`, `"` nem quebra de linha.
- `STRING`: entre aspas duplas; dentro, `""` representa uma aspa e vírgulas/quebras são permitidas.
- `,` e `NL` (quebra de linha, `\n` ou `\r\n`).

## 3. Análise sintática

```ebnf
csv   ::= linha { linha } EOF
linha ::= campo { "," campo } NL
campo ::= TEXTO | STRING | ε
```
Não há conflito no LALR(1) porque FOLLOW(campo) = { `,`, NL }. O arquivo deve terminar com quebra de linha.

## 4. Análise semântica e regras da linguagem

- A primeira linha é o cabeçalho; todas as linhas devem ter o **mesmo número de campos** que ele.
- Uma coluna é numérica se todos os valores não vazios forem números.

## 5. Saída

A tabela alinhada, o total de registros e colunas, as estatísticas das colunas numéricas (n, soma, média,
mínimo e máximo com 2 casas) e o JSON (lista de objetos; números sem aspas e campos vazios como `null`). Veja os casos de teste.

## 6. Dicas

- O valor de um `STRING` é o conteúdo sem as aspas externas e com `""` trocado por `"`.

## 7. Casos de teste

O `testar.sh` compara a saída padrão inteira, linha a linha. Os testes `erro_*` passam se o compilador terminar com código de saída
diferente de 0 e escrever uma mensagem na saída de erro; o texto da mensagem de referência é um **modelo**
(o tipo do erro e a posição `[linha:coluna]` devem bater; a redação pode variar).

```bash
../testar.sh . <comando que executa o seu compilador>
# ex.: ../testar.sh . java -cp "build/classes:$ANTLR_JAR" Main
```

### 7.1 Programas válidos

#### `ok_1.csv`

**Entrada:**

```text
nome,idade,cidade,nota
Muttley,36,Porto Alegre,8.5
"Vigarista, Dick",42,"São Paulo",7
Penélope,29,,9.25
"Ele disse ""olá""",,Canoas,6
```

**Saída esperada:**

```text
| nome            | idade | cidade       | nota |
|-----------------|-------|--------------|------|
| Muttley         | 36    | Porto Alegre | 8.5  |
| Vigarista, Dick | 42    | São Paulo    | 7    |
| Penélope        | 29    |              | 9.25 |
| Ele disse "olá" |       | Canoas       | 6    |
4 registro(s), 4 coluna(s)
  coluna 'idade': n=3 soma=107.00 média=35.67 mín=29.00 máx=42.00
  coluna 'nota': n=4 soma=30.75 média=7.69 mín=6.00 máx=9.25
JSON:
[
  {"nome": "Muttley", "idade": 36, "cidade": "Porto Alegre", "nota": 8.5},
  {"nome": "Vigarista, Dick", "idade": 42, "cidade": "São Paulo", "nota": 7},
  {"nome": "Penélope", "idade": 29, "cidade": null, "nota": 9.25},
  {"nome": "Ele disse \"olá\"", "idade": null, "cidade": "Canoas", "nota": 6}
]
```

#### `ok_2.csv`

**Entrada:**

```text
codigo,disciplina,creditos
TCC,"Teoria da Computação e Compiladores",8

IA,Inteligência Artificial,4
```

**Saída esperada:**

```text
| codigo | disciplina                          | creditos |
|--------|-------------------------------------|----------|
| TCC    | Teoria da Computação e Compiladores | 8        |
| IA     | Inteligência Artificial             | 4        |
2 registro(s), 3 coluna(s)
  coluna 'creditos': n=2 soma=12.00 média=6.00 mín=4.00 máx=8.00
JSON:
[
  {"codigo": "TCC", "disciplina": "Teoria da Computação e Compiladores", "creditos": 8},
  {"codigo": "IA", "disciplina": "Inteligência Artificial", "creditos": 4}
]
```


### 7.2 Programas com erro

#### `erro_1.csv`

```text
a,b,c
1,2,3
4,5
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro semântico [3:1]: a linha tem 2 campo(s), mas o cabeçalho tem 3
```

#### `erro_2.csv`

```text
a,b
"sem fim,2
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro léxico [2:1]: caractere inválido '"'
```

#### `erro_3.csv`

```text
a,b
1,2
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro sintático [2:4]: fim de arquivo inesperado
```

#### `erro_4.csv`

```text
a,b
1,"x"y
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro sintático [2:6]: token inesperado 'y'
```


Escreva também **pelo menos 2 testes `ok_` e 2 testes `erro_` seus**, cobrindo casos que estes não cobrem.

## 8. Desafios extras (opcionais)

- Detectar o separador automaticamente (`,` ou `;`).
- Inferir tipos (inteiro, real, data) por coluna.

## 9. Critérios de avaliação

| Critério | Peso |
|---|---|
| Análise léxica (tokens, posições, erros léxicos) | 20% |
| Análise sintática (gramática sem conflitos / avisos, precedência, erros sintáticos) | 25% |
| Semântica e back-end (regras da seção 4, saída da seção 5) | 35% |
| Mensagens de erro (tipo, `[linha:coluna]`, clareza) | 10% |
| Testes próprios e README (como compilar, executar e testar) | 10% |
