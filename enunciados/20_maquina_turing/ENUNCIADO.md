# Enunciado 20 — Máquina de Turing

**Teoria da Computação e Compiladores — 6M (Digital) · Prof. Giovane Barcelos**

| | |
|---|---|
| **Nível** | ⭐⭐⭐ (intermediário) |
| **Aulas relacionadas** | 12 (Máquina de Turing) e 13 |
| **Ferramentas** | ANTLR 4 **ou** JFlex + JCup (implementar com as duas vale como desafio extra) |
| **Testes fornecidos** | 2 válido(s) (`ok_*`) e 4 inválido(s) (`erro_*`), na pasta [`testes/`](testes/) |

> Leia antes as **regras gerais** em [`../README.md`](../README.md): formato das mensagens de erro, códigos de
> saída, como usar o `testar.sh`, o que entregar e como será avaliado.

---

## 1. Objetivo

Uma linguagem para definir **máquinas de Turing** determinísticas. O programa valida a máquina e
**simula** as entradas, mostrando o **rastro de configurações** da primeira entrada e o resultado
(ACEITA/REJEITA) com a fita final de cada uma.

## 2. Análise léxica

Palavras-chave `machine states initial accept blank transitions run`; `ID` (letras, dígitos, `_`, `#`, `*`);
`ε`; `-> , ; : { }`; comentários `// ...`.

## 3. Análise sintática

```ebnf
maquina   ::= "machine" ID "{"
                "states" ":" lista ";"  "initial" ":" ID ";"  "accept" ":" lista ";"  "blank" ":" ID ";"
                "transitions" "{" { transicao } "}"
                [ "run" ":" palavra { "," palavra } ";" ]
              "}" EOF
transicao ::= ID "," ID "->" ID "," ID "," ID ";"    (* estado, lido -> novo estado, escrito, direção *)
lista     ::= ID { "," ID }
palavra   ::= ID | "ε"
```

## 4. Análise semântica e regras da linguagem

- Estados das transições devem estar declarados; símbolos têm **um caractere**; direção `L`, `R` ou `S`.
- Determinismo: no máximo uma transição por (estado, símbolo lido).
- A máquina **aceita** ao entrar num estado de aceitação e **rejeita** quando não há transição. Limite de passos
  para evitar laço infinito (veja a saída esperada).

## 5. Saída

Resumo da máquina, o rastro da primeira entrada no formato `10[q1]10_` (o estado aparece antes do símbolo
sob a cabeça) e, para cada entrada, ACEITA/REJEITA com a fita final.

## 6. Dicas

- Represente a fita com um `Map<Integer, Character>` ou uma lista que cresce para os dois lados.

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
// soma 1 a um número binário
machine Incrementa {
  states: q0, q1, qf;
  initial: q0;
  accept: qf;
  blank: _;
  transitions {
    q0, 0 -> q0, 0, R;
    q0, 1 -> q0, 1, R;
    q0, _ -> q1, _, L;
    q1, 1 -> q1, 0, L;
    q1, 0 -> qf, 1, S;
    q1, _ -> qf, 1, S;
  }
  run: 1011, 111, 0;
}
```

**Saída esperada:**

```text
MT 'Incrementa': 3 estado(s), 6 transição(ões), inicial q0, aceitação [qf], branco '_'
Rastro de '1011':
    [q0]1011
    1[q0]011
    10[q0]11
    101[q0]1
    1011[q0]_
    101[q1]1_
    10[q1]10_
    1[q1]000_
    1[qf]100_
  1011     -> ACEITA   parou em qf   após   8 passo(s); fita final: 1100
  111      -> ACEITA   parou em qf   após   8 passo(s); fita final: 1000
  0        -> ACEITA   parou em qf   após   3 passo(s); fita final: 1
```

#### `ok_2.txt`

**Entrada:**

```text
// reconhece a^n b^n (n >= 0): marca um a com X e um b com Y por vez
machine AnBn {
  states: q0, q1, q2, q3, qf;
  initial: q0;
  accept: qf;
  blank: _;
  transitions {
    q0, a -> q1, X, R;
    q0, Y -> q3, Y, R;
    q0, _ -> qf, _, S;
    q1, a -> q1, a, R;
    q1, Y -> q1, Y, R;
    q1, b -> q2, Y, L;
    q2, a -> q2, a, L;
    q2, Y -> q2, Y, L;
    q2, X -> q0, X, R;
    q3, Y -> q3, Y, R;
    q3, _ -> qf, _, S;
  }
  run: ab, aabb, ε, aab, abab, ba;
}
```

**Saída esperada:**

```text
MT 'AnBn': 5 estado(s), 11 transição(ões), inicial q0, aceitação [qf], branco '_'
Rastro de 'ab':
    [q0]ab
    X[q1]b
    [q2]XY
    X[q0]Y
    XY[q3]_
    XY[qf]_
  ab       -> ACEITA   parou em qf   após   5 passo(s); fita final: XY
  aabb     -> ACEITA   parou em qf   após  13 passo(s); fita final: XXYY
  ε        -> ACEITA   parou em qf   após   1 passo(s); fita final: ε
  aab      -> REJEITA  parou em q1   após   7 passo(s); fita final: XXY
  abab     -> REJEITA  parou em q3   após   4 passo(s); fita final: XYab
  ba       -> REJEITA  parou em q0   após   0 passo(s); fita final: ba
```


### 7.2 Programas com erro

#### `erro_1.txt`

```text
machine M {
  states: q0;
  initial: q0;
  accept: q0;
  blank: _;
  transitions {
    q0, a -> q0, a, X;
  }
}
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro semântico [7:21]: direção 'X' inválida (use L, R ou S)
```

#### `erro_2.txt`

```text
machine M {
  states: q0;
  initial: q0;
  accept: q1;
  blank: _;
  transitions { }
}
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro semântico [4:11]: estado 'q1' não declarado
```

#### `erro_3.txt`

```text
machine M {
  states: q0;
  initial: q0;
  accept: q0;
  blank: _;
  transitions {
    q0, a -> q0, a, R
  }
}
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro sintático [8:3]: token inesperado '}'
```

#### `erro_4.txt`

```text
machine M {
  states: q0;
  initial: q0;
  accept: q0;
  blank: _;
  transitions {
    q0, a -> q0, a, R;
    q0, a -> q0, b, L;
  }
}
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro semântico [8:9]: não determinismo: duas transições para (q0, a)
```


Escreva também **pelo menos 2 testes `ok_` e 2 testes `erro_` seus**, cobrindo casos que estes não cobrem.

## 8. Desafios extras (opcionais)

- Máquina com várias fitas.
- Exportar a máquina para o JFLAP (`<type>turing</type>`).

## 9. Critérios de avaliação

| Critério | Peso |
|---|---|
| Análise léxica (tokens, posições, erros léxicos) | 20% |
| Análise sintática (gramática sem conflitos / avisos, precedência, erros sintáticos) | 25% |
| Semântica e back-end (regras da seção 4, saída da seção 5) | 35% |
| Mensagens de erro (tipo, `[linha:coluna]`, clareza) | 10% |
| Testes próprios e README (como compilar, executar e testar) | 10% |
