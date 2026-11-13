# Enunciado 17 — Tabela-verdade

**Teoria da Computação e Compiladores — 6M (Digital) · Prof. Giovane Barcelos**

| | |
|---|---|
| **Nível** | ⭐⭐ (básico) |
| **Aulas relacionadas** | 10 (gramáticas, precedência) |
| **Ferramentas** | ANTLR 4 **ou** JFlex + JCup (implementar com as duas vale como desafio extra) |
| **Testes fornecidos** | 1 válido(s) (`ok_*`) e 3 inválido(s) (`erro_*`), na pasta [`testes/`](testes/) |

> Leia antes as **regras gerais** em [`../README.md`](../README.md): formato das mensagens de erro, códigos de
> saída, como usar o `testar.sh`, o que entregar e como será avaliado.

---

## 1. Objetivo

Ler fórmulas da **lógica proposicional**, gerar a **tabela-verdade** de cada uma e classificá-la como
**tautologia**, **contradição** ou **contingência**. São 5 níveis de precedência.

## 2. Análise léxica

Variáveis (`[a-z][a-z0-9]*`), constantes `V` e `F`, operadores `! & | -> <->`, parênteses e `;`.

## 3. Análise sintática

```ebnf
programa ::= { formula ";" } EOF
formula  ::= "!" formula | formula "&" formula | formula "|" formula
           | formula "->" formula | formula "<->" formula | VAR | CONST | "(" formula ")"
```

| Precedência | Operador | Associatividade |
|---|---|---|
| 1 (maior) | `!` | — |
| 2 | `&` | esquerda |
| 3 | `\|` | esquerda |
| 4 | `->` | **direita**: `p -> q -> r` = `p -> (q -> r)` |
| 5 (menor) | `<->` | esquerda |

## 4. Análise semântica e regras da linguagem

- As colunas são as variáveis em **ordem alfabética**; as linhas seguem a contagem binária (todas `V` primeiro).
- Fórmulas com mais de **6 variáveis** são rejeitadas (erro semântico).

## 5. Saída

Para cada fórmula: a fórmula parentizada, a tabela e a classificação. Veja o formato exato nos casos de teste.

## 6. Dicas

- Avalie a AST para cada atribuição de valores (um `Map<String, Boolean>`).

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
p -> q;
(p -> q) <-> (!q -> !p);
p & !p;
p | q & r;
a -> b -> c;
V | F;
```

**Saída esperada:**

```text
Fórmula 1: (p -> q)
  p q | (p -> q)
  V V | V
  V F | F
  F V | V
  F F | V
  => CONTINGÊNCIA (3 de 4 linha(s) verdadeira(s))
Fórmula 2: ((p -> q) <-> (!q -> !p))
  p q | ((p -> q) <-> (!q -> !p))
  V V | V
  V F | V
  F V | V
  F F | V
  => TAUTOLOGIA (4 de 4 linha(s) verdadeira(s))
Fórmula 3: (p & !p)
  p | (p & !p)
  V | F
  F | F
  => CONTRADIÇÃO (0 de 2 linha(s) verdadeira(s))
Fórmula 4: (p | (q & r))
  p q r | (p | (q & r))
  V V V | V
  V V F | V
  V F V | V
  V F F | V
  F V V | V
  F V F | F
  F F V | F
  F F F | F
  => CONTINGÊNCIA (5 de 8 linha(s) verdadeira(s))
Fórmula 5: (a -> (b -> c))
  a b c | (a -> (b -> c))
  V V V | V
  V V F | F
  V F V | V
  V F F | V
  F V V | V
  F V F | V
  F F V | V
  F F F | V
  => CONTINGÊNCIA (7 de 8 linha(s) verdadeira(s))
Fórmula 6: (V | F)
  | (V | F)
  | V
  => TAUTOLOGIA (1 de 1 linha(s) verdadeira(s))
```


### 7.2 Programas com erro

#### `erro_1.txt`

```text
p & ;
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro sintático [1:5]: token inesperado ';'
```

#### `erro_2.txt`

```text
p + q;
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro léxico [1:3]: caractere inválido '+'
```

#### `erro_3.txt`

```text
a & b & c & d & e & f & g;
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro semântico [1:1]: fórmula com 7 variáveis (máximo 6: 2^6 linhas)
```


Escreva também **pelo menos 2 testes `ok_` e 2 testes `erro_` seus**, cobrindo casos que estes não cobrem.

## 8. Desafios extras (opcionais)

- Mostrar a forma normal disjuntiva (FND) a partir das linhas verdadeiras.
- Verificar se duas fórmulas são equivalentes: `p -> q == !p | q;`.

## 9. Critérios de avaliação

| Critério | Peso |
|---|---|
| Análise léxica (tokens, posições, erros léxicos) | 20% |
| Análise sintática (gramática sem conflitos / avisos, precedência, erros sintáticos) | 25% |
| Semântica e back-end (regras da seção 4, saída da seção 5) | 35% |
| Mensagens de erro (tipo, `[linha:coluna]`, clareza) | 10% |
| Testes próprios e README (como compilar, executar e testar) | 10% |
