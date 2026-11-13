# Enunciado 15 — Autômato finito determinístico (AFD) → JFLAP

**Teoria da Computação e Compiladores — 6M (Digital) · Prof. Giovane Barcelos**

| | |
|---|---|
| **Nível** | ⭐⭐⭐ (intermediário) |
| **Aulas relacionadas** | 04–08 (AFD, AFN, minimização) e 22 |
| **Ferramentas** | ANTLR 4 **ou** JFlex + JCup (implementar com as duas vale como desafio extra) |
| **Testes fornecidos** | 2 válido(s) (`ok_*`) e 4 inválido(s) (`erro_*`), na pasta [`testes/`](testes/) |

> Leia antes as **regras gerais** em [`../README.md`](../README.md): formato das mensagens de erro, códigos de
> saída, como usar o `testar.sh`, o que entregar e como será avaliado.

---

## 1. Objetivo

Uma linguagem para **definir AFDs**. O compilador valida o autômato, imprime a tabela de transição δ,
**simula** as palavras de teste mostrando o caminho percorrido e **exporta** o autômato para o formato do
**JFLAP** (`.jff`), que pode ser aberto na ferramenta da disciplina.

## 2. Análise léxica

Palavras-chave `automaton states alphabet initial final transitions test end`; `ID` (letras, dígitos e `_`,
inclusive só dígitos, para símbolos como `0` e `1`); `ε` (palavra vazia); `-> , ; : { }`; comentários `# ...`.

## 3. Análise sintática

```ebnf
automato  ::= "automaton" ID "{"
                "states" ":" lista ";"   "alphabet" ":" lista ";"
                "initial" ":" ID ";"     "final" ":" lista ";"
                "transitions" ":" "{" { transicao } "}"
                [ "test" ":" palavra { "," palavra } ";" ]
              "}" "end" EOF
lista     ::= ID { "," ID }
transicao ::= ID "," ID "->" ID ";"          (* estado, símbolo -> estado *)
palavra   ::= ID | "ε"
```

## 4. Análise semântica e regras da linguagem

- Estados e símbolos usados devem estar declarados (`estado 'q2' não declarado`).
- **Determinismo**: no máximo uma transição por (estado, símbolo) (`não determinismo: δ(q0, a) = q1 e q0`).
- Palavras de teste só podem usar símbolos do alfabeto. Símbolos têm **um caractere**: a palavra `abc` é a sequência `a`, `b`, `c`.
- Avisos (não são erros): estados inalcançáveis e transições ausentes (o AFD é parcial).
- Na simulação, a palavra é rejeitada se faltar transição.

## 5. Saída

Resumo do AFD, tabela δ, avisos, o resultado de cada palavra com o caminho (`q0 → q1 → q1`) e o arquivo
`.jff` (a linha que informa onde ele foi gravado é ignorada na comparação).

## 6. Dicas

- Formato `.jff`: XML com `<structure><type>fa</type><automaton>` contendo `<state id= name=>` (com `<initial/>`
  e `<final/>`) e `<transition><from><to><read>`. Crie um AFD pequeno no JFLAP e salve para ver um exemplo.

## 7. Casos de teste

O `testar.sh` compara a saída padrão inteira, linha a linha — ignorando as linhas que casam com `^Arquivo JFLAP gravado`. Os testes `erro_*` passam se o compilador terminar com código de saída
diferente de 0 e escrever uma mensagem na saída de erro; o texto da mensagem de referência é um **modelo**
(o tipo do erro e a posição `[linha:coluna]` devem bater; a redação pode variar).

```bash
../testar.sh . <comando que executa o seu compilador>
# ex.: ../testar.sh . java -cp "build/classes:$ANTLR_JAR" Main
```

### 7.1 Programas válidos

#### `ok_1.dfa`

**Entrada:**

```text
automaton EndWithA {
    states: q0, q1;
    alphabet: a, b;
    initial: q0;
    final: q1;
    transitions: {
        q0, a -> q1;
        q0, b -> q0;
        q1, a -> q1;
        q1, b -> q0;
    }
    test: a, aab, abab, ba, ε;
} end
```

**Saída esperada:**

```text
AFD 'EndWithA': 2 estado(s), alfabeto [a, b], inicial q0, finais [q1]
  δ         a       b       
→ q0        q1      q0      
 *q1        q1      q0      
Simulação:
  a          ACEITA    q0 → q1
  aab        REJEITA   q0 → q1 → q1 → q0
  abab       REJEITA   q0 → q1 → q0 → q1 → q0
  ba         ACEITA    q0 → q0 → q1
  ε          REJEITA   q0
Arquivo JFLAP gravado em build/out/ok_1.dfa.jff  (abra com ferramentas/jflap.sh)
```

#### `ok_2.dfa`

**Entrada:**

```text
# número de 'a' par; o estado 'lixo' é inalcançável e a tabela é parcial
automaton EvenAs {
    states: par, impar, lixo;
    alphabet: a, b;
    initial: par;
    final: par;
    transitions: {
        par, a -> impar;
        par, b -> par;
        impar, a -> par;
        impar, b -> impar;
        lixo, a -> lixo;
    }
    test: ε, aa, aba, bbbab;
} end
```

**Saída esperada:**

```text
AFD 'EvenAs': 3 estado(s), alfabeto [a, b], inicial par, finais [par]
  δ         a       b       
→*par       impar   par     
  impar     par     impar   
  lixo      lixo    -       
Aviso: estado 'lixo' inalcançável
Aviso: AFD parcial (1 transição(ões) ausente(s) = rejeição)
Simulação:
  ε          ACEITA    par
  aa         ACEITA    par → impar → par
  aba        ACEITA    par → impar → impar → par
  bbbab      REJEITA   par → par → par → par → impar → impar
Arquivo JFLAP gravado em build/out/ok_2.dfa.jff  (abra com ferramentas/jflap.sh)
```


### 7.2 Programas com erro

#### `erro_1.dfa`

```text
automaton ErrorTest {
    states: q0, q1
    alphabet: a, b;
    initial: q0;
    final: q1;
    transitions: {
        q0, a -> q1;
    }
} end
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro sintático [3:5]: token inesperado 'alphabet'
```

#### `erro_2.dfa`

```text
automaton X {
  states: q0, q1;
  alphabet: a;
  initial: q0;
  final: q2;
  transitions: { q0, a -> q1; }
} end
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro semântico [5:10]: estado 'q2' não declarado
```

#### `erro_3.dfa`

```text
automaton X {
  states: q0, q1;
  alphabet: a;
  initial: q0;
  final: q1;
  transitions: { q0, a -> q1; q0, a -> q0; }
} end
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro semântico [6:35]: não determinismo: δ(q0, a) = q1 e q0
```

#### `erro_4.dfa`

```text
automaton X {
  states: q0;
  alphabet: a;
  initial: q0;
  final: q0;
  transitions: { q0, a -> q0; }
  test: aa, abc;
} end
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro semântico [7:13]: a palavra 'abc' usa 'b', fora do alfabeto
```


Escreva também **pelo menos 2 testes `ok_` e 2 testes `erro_` seus**, cobrindo casos que estes não cobrem.

## 8. Desafios extras (opcionais)

- **Minimizar** o AFD (algoritmo de partição) e mostrar os estados equivalentes.
- Aceitar AFN e convertê-lo para AFD (construção de subconjuntos).

## 9. Critérios de avaliação

| Critério | Peso |
|---|---|
| Análise léxica (tokens, posições, erros léxicos) | 20% |
| Análise sintática (gramática sem conflitos / avisos, precedência, erros sintáticos) | 25% |
| Semântica e back-end (regras da seção 4, saída da seção 5) | 35% |
| Mensagens de erro (tipo, `[linha:coluna]`, clareza) | 10% |
| Testes próprios e README (como compilar, executar e testar) | 10% |
