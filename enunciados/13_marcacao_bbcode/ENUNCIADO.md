# Enunciado 13 — Marcação estilo BBCode → HTML

**Teoria da Computação e Compiladores — 6M (Digital) · Prof. Giovane Barcelos**

| | |
|---|---|
| **Nível** | ⭐⭐⭐ (intermediário) |
| **Aulas relacionadas** | 16 e 17 |
| **Ferramentas** | ANTLR 4 **ou** JFlex + JCup (implementar com as duas vale como desafio extra) |
| **Testes fornecidos** | 2 válido(s) (`ok_*`) e 5 inválido(s) (`erro_*`), na pasta [`testes/`](testes/) |

> Leia antes as **regras gerais** em [`../README.md`](../README.md): formato das mensagens de erro, códigos de
> saída, como usar o `testar.sh`, o que entregar e como será avaliado.

---

## 1. Objetivo

Traduzir uma linguagem de marcação estilo **BBCode** (fóruns) para **HTML**. Aqui o **scanner faz
mais trabalho**: uma tag de abertura com argumento, como `[color=red]`, é reconhecida como **um único token**.

## 2. Análise léxica

- `ABRE`: `[` nome `]` ou `[` nome `=` argumento `]` (nome = letras; argumento = qualquer coisa até `]`).
- `FECHA`: `[/` nome `]`.
- `NL`: quebra de linha. `TEXTO`: qualquer sequência sem `[` nem quebra de linha.
- Um `[` que não forma tag é erro léxico.

## 3. Análise sintática

```ebnf
documento ::= { elemento } EOF
elemento  ::= TEXTO | NL | ABRE { elemento } FECHA
```

## 4. Análise semântica e regras da linguagem

| BBCode | HTML |
|---|---|
| `[b] [i] [u] [s]` | `<strong> <em> <u> <del>` |
| `[code]` `[quote]` | `<code>` `<blockquote>` |
| `[color=X]` | `<span style="color:X">` (X: nome de cor ou `#rrggbb`) |
| `[size=N]` | `<span style="font-size:Npx">` (N inteiro entre 8 e 72) |
| `[url=U]` | `<a href="U">` |

- O texto é **escapado** (`&` → `&amp;`, `<` → `&lt;`, `>` → `&gt;`) e cada quebra de linha vira `<br/>`.
- Erros semânticos: tag desconhecida, fechamento trocado (`[b] (linha 1) fechada com [/i]`), argumento faltando
  em `color`/`size`/`url` ou sobrando nas outras, argumento inválido (`[size=200]`).

## 5. Saída

O HTML gerado e uma linha final com estatísticas (veja os casos de teste).

## 6. Dicas

- Faça o parser aceitar qualquer `ABRE ... FECHA` e confira os nomes na semântica: as mensagens ficam melhores.

## 7. Casos de teste

O `testar.sh` compara a saída padrão inteira, linha a linha. Os testes `erro_*` passam se o compilador terminar com código de saída
diferente de 0 e escrever uma mensagem na saída de erro; o texto da mensagem de referência é um **modelo**
(o tipo do erro e a posição `[linha:coluna]` devem bater; a redação pode variar).

```bash
../testar.sh . <comando que executa o seu compilador>
# ex.: ../testar.sh . java -cp "build/classes:$ANTLR_JAR" Main
```

### 7.1 Programas válidos

#### `ok_1.mark`

**Entrada:**

```text
Este é um texto com [b]negrito[/b], [i]itálico[/i] e [color=red]vermelho[/color].
Tamanhos: [size=20]grande[/size] e [u]sublinhado[/u].
```

**Saída esperada:**

```text
Este é um texto com <strong>negrito</strong>, <em>itálico</em> e <span style="color:red">vermelho</span>.<br/>
Tamanhos: <span style="font-size:20px">grande</span> e <u>sublinhado</u>.<br/>

(5 tag(s) traduzida(s))
```

#### `ok_2.mark`

**Entrada:**

```text
[quote]Aninhamento: [b]negrito com [i]itálico[/i] dentro[/b][/quote]
Veja a [url=https://www.antlr.org]documentação do ANTLR[/url] e use [code]a < b && c > d[/code].
```

**Saída esperada:**

```text
<blockquote>Aninhamento: <strong>negrito com <em>itálico</em> dentro</strong></blockquote><br/>
Veja a <a href="https://www.antlr.org">documentação do ANTLR</a> e use <code>a &lt; b &amp;&amp; c &gt; d</code>.<br/>

(5 tag(s) traduzida(s))
```


### 7.2 Programas com erro

#### `erro_1.mark`

```text
[b]texto[/i]
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro semântico [1:9]: [b] (linha 1) fechada com [/i]
```

#### `erro_2.mark`

```text
[b texto]
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro léxico [1:1]: caractere inválido '['
```

#### `erro_3.mark`

```text
[b]negrito sem fechar
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro sintático [2:1]: fim de arquivo inesperado
```

#### `erro_4.mark`

```text
[blink]pisca[/blink]
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro semântico [1:1]: tag desconhecida [blink]
```

#### `erro_5.mark`

```text
[size=200]enorme[/size]
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro semântico [1:1]: [size=200]: o tamanho deve ser um inteiro entre 8 e 72
```


Escreva também **pelo menos 2 testes `ok_` e 2 testes `erro_` seus**, cobrindo casos que estes não cobrem.

## 8. Desafios extras (opcionais)

- `[list]` com itens `[*]` → `<ul><li>`.
- `[img]url[/img]` → `<img src="url"/>`.

## 9. Critérios de avaliação

| Critério | Peso |
|---|---|
| Análise léxica (tokens, posições, erros léxicos) | 20% |
| Análise sintática (gramática sem conflitos / avisos, precedência, erros sintáticos) | 25% |
| Semântica e back-end (regras da seção 4, saída da seção 5) | 35% |
| Mensagens de erro (tipo, `[linha:coluna]`, clareza) | 10% |
| Testes próprios e README (como compilar, executar e testar) | 10% |
