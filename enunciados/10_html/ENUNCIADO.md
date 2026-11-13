# Enunciado 10 — HTML básico

**Teoria da Computação e Compiladores — 6M (Digital) · Prof. Giovane Barcelos**

| | |
|---|---|
| **Nível** | ⭐⭐⭐ (intermediário) |
| **Aulas relacionadas** | 17 e 26 (modos léxicos) |
| **Ferramentas** | ANTLR 4 **ou** JFlex + JCup (implementar com as duas vale como desafio extra) |
| **Testes fornecidos** | 2 válido(s) (`ok_*`) e 4 inválido(s) (`erro_*`), na pasta [`testes/`](testes/) |

> Leia antes as **regras gerais** em [`../README.md`](../README.md): formato das mensagens de erro, códigos de
> saída, como usar o `testar.sh`, o que entregar e como será avaliado.

---

## 1. Objetivo

Analisar um subconjunto de HTML: tags com atributos, tags auto-fechadas (`<br/>`), texto e comentários.
O desafio é **léxico**: dentro de uma tag os tokens são outros (nomes, `=`, valores entre aspas) e fora dela
tudo é texto. Isso se resolve com **modos léxicos** (ANTLR) ou **estados exclusivos** (JFlex).

## 2. Análise léxica

- Fora de tag: `<!-- comentário -->` (ignorado), `</` e `<` (abrem uma tag), e texto (tudo até o próximo `<`).
- Dentro de tag: `>` e `/>` (fecham), `=`, valor entre aspas duplas, nome (`[a-zA-Z][a-zA-Z0-9-]*`); espaços ignorados.
- **ANTLR**: lexer separado (`lexer grammar`) com `mode TAG` e parser separado (`tokenVocab`).
- **JFlex**: `%xstate TAG`.

## 3. Análise sintática

```ebnf
documento ::= { elemento } EOF
elemento  ::= tag | TEXTO
tag       ::= "<" NOME { atributo } ">" { elemento } "</" NOME ">"
            | "<" NOME { atributo } "/>"
atributo  ::= NOME "=" VALOR
```

## 4. Análise semântica e regras da linguagem

- Toda tag deve ser fechada com o **mesmo nome**: `a tag <br> (linha 2) foi fechada com </body>`.
- Atributo repetido na mesma tag é erro: `atributo 'class' repetido em <p>`.

## 5. Saída

A árvore do documento indentada com 2 espaços por nível (tags com seus atributos; textos entre aspas, sem os
espaços das pontas) e um resumo com o número de elementos, de textos e o título (`<title>`). Veja os casos de teste.

## 6. Dicas

- O texto entre tags costuma ter só espaços e quebras de linha: ignore textos vazios depois de `trim()`.

## 7. Casos de teste

O `testar.sh` compara a saída padrão inteira, linha a linha. Os testes `erro_*` passam se o compilador terminar com código de saída
diferente de 0 e escrever uma mensagem na saída de erro; o texto da mensagem de referência é um **modelo**
(o tipo do erro e a posição `[linha:coluna]` devem bater; a redação pode variar).

```bash
../testar.sh . <comando que executa o seu compilador>
# ex.: ../testar.sh . java -cp "build/classes:$ANTLR_JAR" Main
```

### 7.1 Programas válidos

#### `ok_1.html`

**Entrada:**

```text
<html>
 <head>
   <title>Parser Example</title>
 </head>
 <body class="anyClass" id="MyPage">
   <h1>Welcome Muttley</h1>
   <p>My example using <a href="https://jflex.de/">JFlex</a>.</p>
 </body>
</html>
```

**Saída esperada:**

```text
<html>
  <head>
    <title>
      "Parser Example"
  <body class="anyClass" id="MyPage">
    <h1>
      "Welcome Muttley"
    <p>
      "My example using"
      <a href="https://jflex.de/">
        "JFlex"
      "."
Resumo: 7 elemento(s), 5 texto(s); título: "Parser Example"
```

#### `ok_2.html`

**Entrada:**

```text
<!-- comentários são descartados pelo scanner -->
<div id="menu" data-x="1">
  <ul>
    <li><a href="aula17.html">JFlex e ANTLR</a></li>
    <li><a href="aula21.html">JCup</a><br/></li>
  </ul>
  <img src="logo.png" alt="logo"/>
</div>
```

**Saída esperada:**

```text
<div id="menu" data-x="1">
  <ul>
    <li>
      <a href="aula17.html">
        "JFlex e ANTLR"
    <li>
      <a href="aula21.html">
        "JCup"
      <br/>
  <img src="logo.png" alt="logo"/>
Resumo: 8 elemento(s), 2 texto(s); título: (sem título)
```


### 7.2 Programas com erro

#### `erro_1.html`

```text
<html>
 <head>
   <title>Parser Example</title
 </head>
</html>
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro léxico [4:2]: caractere inválido '<'
```

#### `erro_2.html`

```text
<html>
 <br class="anyClass" id="MyPage">
   <h1>Welcome Muttley</h1>
 </body>
</html>
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro semântico [4:4]: a tag <br> (linha 2) foi fechada com </body>
```

#### `erro_3.html`

```text
<p class="a" class="b">texto</p>
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro semântico [1:14]: atributo 'class' repetido em <p>
```

#### `erro_4.html`

```text
<p @x="1">texto</p>
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro léxico [1:4]: caractere inválido '@'
```


Escreva também **pelo menos 2 testes `ok_` e 2 testes `erro_` seus**, cobrindo casos que estes não cobrem.

## 8. Desafios extras (opcionais)

- Aceitar tags "vazias" do HTML sem a barra (`<br>`, `<img ...>`) por uma lista de nomes conhecidos.
- Gerar a versão minificada do documento.

## 9. Critérios de avaliação

| Critério | Peso |
|---|---|
| Análise léxica (tokens, posições, erros léxicos) | 20% |
| Análise sintática (gramática sem conflitos / avisos, precedência, erros sintáticos) | 25% |
| Semântica e back-end (regras da seção 4, saída da seção 5) | 35% |
| Mensagens de erro (tipo, `[linha:coluna]`, clareza) | 10% |
| Testes próprios e README (como compilar, executar e testar) | 10% |
