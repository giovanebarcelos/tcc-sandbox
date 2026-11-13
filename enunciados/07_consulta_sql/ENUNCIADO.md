# Enunciado 07 — Linguagem de consulta → SQL

**Teoria da Computação e Compiladores — 6M (Digital) · Prof. Giovane Barcelos**

| | |
|---|---|
| **Nível** | ⭐⭐⭐ (intermediário) |
| **Aulas relacionadas** | 21 e 22 |
| **Ferramentas** | ANTLR 4 **ou** JFlex + JCup (implementar com as duas vale como desafio extra) |
| **Testes fornecidos** | 1 válido(s) (`ok_*`) e 4 inválido(s) (`erro_*`), na pasta [`testes/`](testes/) |

> Leia antes as **regras gerais** em [`../README.md`](../README.md): formato das mensagens de erro, códigos de
> saída, como usar o `testar.sh`, o que entregar e como será avaliado.

---

## 1. Objetivo

Um **tradutor**: uma linguagem de consulta com a ordem "natural" (`FROM ... SELECT ...`) é traduzida para
**SQL**. Antes das consultas, o programa declara o **esquema** das tabelas, que a análise semântica usa
para validar colunas e tabelas.

## 2. Análise léxica

- Palavras-chave (maiúsculas): `TABLE FROM SELECT WHERE AND OR ORDER BY ASC DESC`.
- `ID` (letra seguida de letras, dígitos ou `_`), `NUMBER` (inteiro), `STRING` entre **aspas simples** (`'Muttley'`).
- `( ) , ; *` e os operadores `= != < > <= >=`.

## 3. Análise sintática

```ebnf
programa ::= { tabela | consulta } EOF
tabela   ::= "TABLE" ID "(" ID { "," ID } ")" ";"
consulta ::= "FROM" ID "SELECT" campos [ "WHERE" cond ] [ "ORDER" "BY" ID [ "ASC" | "DESC" ] ] ";"
campos   ::= "*" | ID { "," ID }
cond     ::= cond "AND" cond | cond "OR" cond | valor op valor
valor    ::= ID | NUMBER | STRING
```
`AND` tem precedência **maior** que `OR`; ambos associam à esquerda.

## 4. Análise semântica e regras da linguagem

- Tabela usada numa consulta precisa ter sido declarada antes (`tabela 'alunos' não declarada`).
- Toda coluna citada (em `SELECT`, `WHERE` ou `ORDER BY`) deve existir na tabela (`coluna 'idade' não existe na tabela 'users'`).

## 5. Saída

- Para cada `TABLE`: um comentário SQL `-- tabela nome [colunas]`.
- Para cada consulta: `-- consulta N:` e o SQL gerado. A condição sai **parentizada** conforme a precedência,
  e `!=` vira `<>`. Veja os casos de teste.

## 6. Dicas

- A tradução é um percurso na AST; a parentização vem de graça se cada nó `AND`/`OR` gerar `( ... )`.

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
TABLE users (name, year, city);
TABLE cursos (codigo, titulo, vagas);
FROM users SELECT name, year WHERE year > 1973 AND name = 'Muttley';
FROM users SELECT * WHERE city = 'Porto Alegre' OR year <= 2000 AND name != 'Dick' ORDER BY year DESC;
FROM cursos SELECT titulo ORDER BY vagas;
```

**Saída esperada:**

```text
-- tabela users [name, year, city]
-- tabela cursos [codigo, titulo, vagas]
-- consulta 1:
SELECT name, year FROM users WHERE (year > 1973 AND name = 'Muttley');
-- consulta 2:
SELECT * FROM users WHERE (city = 'Porto Alegre' OR (year <= 2000 AND name <> 'Dick')) ORDER BY year DESC;
-- consulta 3:
SELECT titulo FROM cursos ORDER BY vagas ASC;
```


### 7.2 Programas com erro

#### `erro_1.txt`

```text
TABLE users (name, year);
FROM users SELECT name year WHERE year > 1973 AND name = 'Muttley';
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro sintático [2:24]: token inesperado 'year'
```

#### `erro_2.txt`

```text
TABLE users (name, year);
FROM users SELECT name, year WHERE year > 1973 AND name = Muttley';
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro léxico [2:66]: caractere inválido '''
```

#### `erro_3.txt`

```text
TABLE users (name, year);
FROM users SELECT name, idade WHERE year > 1973;
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro semântico [2:25]: coluna 'idade' não existe na tabela 'users'
```

#### `erro_4.txt`

```text
TABLE users (name, year);
FROM alunos SELECT *;
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro semântico [2:6]: tabela 'alunos' não declarada
```


Escreva também **pelo menos 2 testes `ok_` e 2 testes `erro_` seus**, cobrindo casos que estes não cobrem.

## 8. Desafios extras (opcionais)

- `LIMIT n`, `COUNT(*)`, e `JOIN` entre duas tabelas declaradas.

## 9. Critérios de avaliação

| Critério | Peso |
|---|---|
| Análise léxica (tokens, posições, erros léxicos) | 20% |
| Análise sintática (gramática sem conflitos / avisos, precedência, erros sintáticos) | 25% |
| Semântica e back-end (regras da seção 4, saída da seção 5) | 35% |
| Mensagens de erro (tipo, `[linha:coluna]`, clareza) | 10% |
| Testes próprios e README (como compilar, executar e testar) | 10% |
