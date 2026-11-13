# Enunciado 16 — Processador de templates

**Teoria da Computação e Compiladores — 6M (Digital) · Prof. Giovane Barcelos**

| | |
|---|---|
| **Nível** | ⭐⭐⭐⭐ (avançado) |
| **Aulas relacionadas** | 17 e 26 (modos léxicos) |
| **Ferramentas** | ANTLR 4 **ou** JFlex + JCup (implementar com as duas vale como desafio extra) |
| **Testes fornecidos** | 2 válido(s) (`ok_*`) e 5 inválido(s) (`erro_*`), na pasta [`testes/`](testes/) |

> Leia antes as **regras gerais** em [`../README.md`](../README.md): formato das mensagens de erro, códigos de
> saída, como usar o `testar.sh`, o que entregar e como será avaliado.

---

## 1. Objetivo

Um processador de **templates** no estilo Jinja/Django: texto livre misturado com marcações
`{{ variável | filtro }}` e comandos `{% if %}`, `{% for %}` e `{% set %}`. O resultado é o texto
**renderizado**. Texto e código convivem graças aos **modos léxicos**.

## 2. Análise léxica

- Fora do código: `{{` e `{%` abrem o modo código; todo o resto é texto (inclusive `{` sozinho).
- No modo código: `}}` e `%}` fecham; palavras `if else endif for in endfor set not`; `IDENT`; `STRING` entre aspas duplas;
  `| = [ ] ,`; espaços ignorados.
- **ANTLR**: lexer separado com `mode CODE`. **JFlex**: `%xstate CODE`.

## 3. Análise sintática

```ebnf
template ::= { secao } EOF
secao    ::= TEXTO
           | "{{" IDENT { "|" IDENT } "}}"
           | "{%" "if" [ "not" ] IDENT "%}" { secao } [ "{%" "else" "%}" { secao } ] "{%" "endif" "%}"
           | "{%" "for" IDENT "in" IDENT "%}" { secao } "{%" "endfor" "%}"
           | "{%" "set" IDENT "=" valor "%}"
valor    ::= STRING | "[" [ STRING { "," STRING } ] "]"
```

## 4. Análise semântica e regras da linguagem

- Variáveis são criadas por `set` (texto ou lista) ou pelo `for` (dentro do laço).
- Filtros: `upper`, `lower`, `capitalize` (primeira letra maiúscula) e `length` (tamanho do texto ou da lista).
- `if x`: verdadeiro se `x` existe e não é vazio (texto `""` ou lista `[]`).
- Erros: variável não definida, filtro desconhecido, `for` sobre algo que não é lista.

## 5. Saída

O texto renderizado e, no fim, uma linha com o número de variáveis no contexto. Veja os casos de teste.

## 6. Dicas

- No JCup, depois de uma lista de seções, um `{%` pode abrir uma seção aninhada **ou** o `else`/`endif`:
  o LALR(1) empilha o `{%` e decide pelo token seguinte. Escreva a gramática para que isso não gere conflito.

## 7. Casos de teste

O `testar.sh` compara a saída padrão inteira, linha a linha. Os testes `erro_*` passam se o compilador terminar com código de saída
diferente de 0 e escrever uma mensagem na saída de erro; o texto da mensagem de referência é um **modelo**
(o tipo do erro e a posição `[linha:coluna]` devem bater; a redação pode variar).

```bash
../testar.sh . <comando que executa o seu compilador>
# ex.: ../testar.sh . java -cp "build/classes:$ANTLR_JAR" Main
```

### 7.1 Programas válidos

#### `ok_1.tpl`

**Entrada:**

```text
{% set nome = "Muttley" %}{% set vip = "sim" %}{% set cursos = ["Compiladores", "Teoria da Computação", "IA"] %}Olá, {{ nome | upper }}!
{% if vip %}Você é cliente VIP.{% else %}Você é um cliente comum.{% endif %}
Cursos matriculados ({{ cursos | length }}):
{% for c in cursos %}  - {{ c }}
{% endfor %}{% if not desconto %}Nenhum desconto aplicado.{% endif %}
```

**Saída esperada:**

```text
Olá, MUTTLEY!
Você é cliente VIP.
Cursos matriculados (3):
  - Compiladores
  - Teoria da Computação
  - IA
Nenhum desconto aplicado.
--- 3 variável(is) no contexto
```

#### `ok_2.tpl`

**Entrada:**

```text
{% set pedido = "1234" %}{% set itens = [] %}{% set cliente = "penélope" %}Pedido #{{ pedido }} de {{ cliente | capitalize }}
{% if itens %}Itens: {{ itens }}{% else %}{% if not itens %}(pedido vazio){% endif %}{% endif %}
Total de caracteres do nome: {{ cliente | length }}
```

**Saída esperada:**

```text
Pedido #1234 de Penélope
(pedido vazio)
Total de caracteres do nome: 8
--- 3 variável(is) no contexto
```


### 7.2 Programas com erro

#### `erro_1.tpl`

```text
{% set x = "a" %}{% if x %}sem fim
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro sintático [2:1]: fim de arquivo inesperado
```

#### `erro_2.tpl`

```text
Olá, {{ nome }}!
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro semântico [1:9]: variável 'nome' não definida
```

#### `erro_3.tpl`

```text
{% set n = "a" %}{{ n | reverse }}
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro semântico [1:25]: filtro desconhecido 'reverse'
```

#### `erro_4.tpl`

```text
{{ nome @ }}
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro léxico [1:9]: caractere inválido '@'
```

#### `erro_5.tpl`

```text
{% set n = "abc" %}{% for c in n %}{{ c }}{% endfor %}
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro semântico [1:32]: 'n' não é uma lista
```


Escreva também **pelo menos 2 testes `ok_` e 2 testes `erro_` seus**, cobrindo casos que estes não cobrem.

## 8. Desafios extras (opcionais)

- `{% elif %}`, comparação `{% if a == "x" %}` e `loop.index` dentro do `for`.
- `{% include "outro.tpl" %}`.

## 9. Critérios de avaliação

| Critério | Peso |
|---|---|
| Análise léxica (tokens, posições, erros léxicos) | 20% |
| Análise sintática (gramática sem conflitos / avisos, precedência, erros sintáticos) | 25% |
| Semântica e back-end (regras da seção 4, saída da seção 5) | 35% |
| Mensagens de erro (tipo, `[linha:coluna]`, clareza) | 10% |
| Testes próprios e README (como compilar, executar e testar) | 10% |
