# Enunciado 18 — Expressão regular → AFN de Thompson

**Teoria da Computação e Compiladores — 6M (Digital) · Prof. Giovane Barcelos**

| | |
|---|---|
| **Nível** | ⭐⭐⭐⭐ (avançado) |
| **Aulas relacionadas** | 06–08 (AFN-ε, ER) |
| **Ferramentas** | ANTLR 4 **ou** JFlex + JCup (implementar com as duas vale como desafio extra) |
| **Testes fornecidos** | 1 válido(s) (`ok_*`) e 3 inválido(s) (`erro_*`), na pasta [`testes/`](testes/) |

> Leia antes as **regras gerais** em [`../README.md`](../README.md): formato das mensagens de erro, códigos de
> saída, como usar o `testar.sh`, o que entregar e como será avaliado.

---

## 1. Objetivo

Ler expressões regulares, construir o **AFN-ε pela construção de Thompson** e **simular** palavras sobre
ele usando o **fecho-ε**. A sintaxe tem **concatenação implícita** (justaposição): `ab` é `a` seguido de `b`.

## 2. Análise léxica

Símbolos `[a-z0-9]`, `ε`, `| * + ? ( ) : , ;`. Espaços são ignorados.

## 3. Análise sintática

```ebnf
programa ::= { linha } EOF
linha    ::= regex ":" palavra { "," palavra } ";"
palavra  ::= CHAR { CHAR } | "ε"
regex    ::= regex ("*" | "+" | "?") | regex regex | regex "|" regex | "(" regex ")" | CHAR | "ε"
```
Precedência: repetição (`* + ?`) > concatenação > alternativa (`|`). No CUP, prefira uma gramática
**estratificada** (`alt → conc → rep → atom`) em vez de declarações de precedência.

## 4. Análise semântica e regras da linguagem

- Construção de Thompson: cada subexpressão vira um AFN com um estado inicial e um final, combinados por transições ε.
  Para que os números de estados e transições batam com os testes, **não funda estados**:
  - um símbolo (ou `ε`) gera 2 estados e 1 transição;
  - a concatenação liga o final do primeiro ao início do segundo com 1 transição ε;
  - `|` cria 2 estados novos e 4 transições ε;
  - `*` cria 2 estados novos e 4 transições ε (entra, sai, pula e repete); `+` não tem o "pula" e `?` não tem o
    "repete" (3 transições ε cada).

  Ex.: `(a|b)*abb` → 14 estados e 16 transições (a versão do livro do
  Dragão, que funde estados na concatenação, daria 11 estados).
- Simulação: conjunto de estados atuais = fecho-ε; consome um símbolo; repete; aceita se o estado final estiver no conjunto.

## 5. Saída

Para cada linha: a ER, o número de estados e de transições do AFN-ε de Thompson, o alfabeto e o resultado de
cada palavra (ACEITA/REJEITA). Veja os casos de teste: com a construção de Thompson "pura", o número de estados e
transições é determinado pela ER.

## 6. Dicas

- `(0|1(01*0)*1)*` reconhece os números binários múltiplos de 3: ótimo teste.

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
(a|b)*abb : abb, aabb, babb, ab, abba, ε ;
a+b?c*    : a, aaab, abccc, b, ac, aabcb ;
(0|1(01*0)*1)* : 0, 11, 110, 1001, 111, 10 ;
ε|ab : ε, ab, a ;
```

**Saída esperada:**

```text
ER 1: (a|b)*abb   →  AFN-ε de Thompson: 14 estados, 16 transições, alfabeto [a, b]
  abb        ACEITA
  aabb       ACEITA
  babb       ACEITA
  ab         REJEITA
  abba       REJEITA
  ε          REJEITA
ER 2: a+b?c*   →  AFN-ε de Thompson: 12 estados, 15 transições, alfabeto [a, b, c]
  a          ACEITA
  aaab       ACEITA
  abccc      ACEITA
  b          REJEITA
  ac         ACEITA
  aabcb      REJEITA
ER 3: (0|1(01*0)*1)*   →  AFN-ε de Thompson: 20 estados, 26 transições, alfabeto [0, 1]
  0          ACEITA
  11         ACEITA
  110        ACEITA
  1001       ACEITA
  111        REJEITA
  10         REJEITA
ER 4: (ε|ab)   →  AFN-ε de Thompson: 8 estados, 8 transições, alfabeto [a, b]
  ε          ACEITA
  ab         ACEITA
  a          REJEITA
```


### 7.2 Programas com erro

#### `erro_1.txt`

```text
(a|b*abb : abb ;
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro sintático [1:10]: token inesperado ':'
```

#### `erro_2.txt`

```text
a#b : ab ;
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro léxico [1:2]: caractere inválido '#'
```

#### `erro_3.txt`

```text
a|*b : ab ;
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro sintático [1:3]: token inesperado '*'
```


Escreva também **pelo menos 2 testes `ok_` e 2 testes `erro_` seus**, cobrindo casos que estes não cobrem.

## 8. Desafios extras (opcionais)

- Converter o AFN em AFD (subconjuntos) e exportar para o JFLAP.
- Classes de caracteres `[a-c]` e o curinga `.`.

## 9. Critérios de avaliação

| Critério | Peso |
|---|---|
| Análise léxica (tokens, posições, erros léxicos) | 20% |
| Análise sintática (gramática sem conflitos / avisos, precedência, erros sintáticos) | 25% |
| Semântica e back-end (regras da seção 4, saída da seção 5) | 35% |
| Mensagens de erro (tipo, `[linha:coluna]`, clareza) | 10% |
| Testes próprios e README (como compilar, executar e testar) | 10% |
