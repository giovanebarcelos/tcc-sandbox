# Enunciado 24 — Código de três endereços e máquina virtual

**Teoria da Computação e Compiladores — 6M (Digital) · Prof. Giovane Barcelos**

| | |
|---|---|
| **Nível** | ⭐⭐⭐⭐ (avançado) |
| **Aulas relacionadas** | 25 (geração de código) |
| **Ferramentas** | ANTLR 4 **ou** JFlex + JCup (implementar com as duas vale como desafio extra) |
| **Testes fornecidos** | 2 válido(s) (`ok_*`) e 3 inválido(s) (`erro_*`), na pasta [`testes/`](testes/) |

> Leia antes as **regras gerais** em [`../README.md`](../README.md): formato das mensagens de erro, códigos de
> saída, como usar o `testar.sh`, o que entregar e como será avaliado.

---

## 1. Objetivo

Gerar **código de três endereços** e **quádruplas** (op, arg1, arg2, resultado) para uma linguagem com
`if`, `while`, `print` e expressões, e **executar as quádruplas** numa máquina virtual escrita por você — a
prova de que o código gerado está correto. Os operadores lógicos usam **código de desvio** (curto-circuito).

## 2. Análise léxica

`if else while print`, `ID`, `NUM`, `= + - * / % < > <= >= == != && || ! ( ) { } ;`, comentários `// ...`.

## 3. Análise sintática

```ebnf
programa ::= { stmt } EOF
stmt     ::= ID "=" expr ";" | "if" "(" expr ")" stmt [ "else" stmt ] | "while" "(" expr ")" stmt
           | "print" "(" expr ")" ";" | "{" { stmt } "}"
expr     ::= ("-" | "!") expr | expr op expr | NUM | ID | "(" expr ")"
```
Precedência (da maior para a menor): unários; `* / %`; `+ -`; `< > <= >=`; `== !=`; `&&`; `||`.

## 4. Análise semântica e regras da linguagem

- Variável usada antes de receber valor: erro semântico.
- Valores lógicos são inteiros (0 = falso). Em `if`/`while`, `&&`, `||` e `!` **não** calculam 0/1: viram saltos.
- Divisão por zero é erro **de execução** da VM.

## 5. Saída

O código de 3 endereços, a tabela de quádruplas e a execução na VM. **O código gerado é livre.** Nos testes são
comparadas só as linhas `print: ...` e os valores finais das variáveis (`Variáveis ao final: ...`).

## 6. Dicas

- Funções do esquema clássico: `gerarExpr(e) → endereço` e `gerarCond(e, Lverdadeiro, Lfalso)`.
- A VM é um laço com um contador de programa e um `Map` de rótulo → índice.

## 7. Casos de teste

O `testar.sh` compara só a **saída da execução** do programa gerado (o código gerado é livre) — só as linhas que casam com `^(print:|Variáveis ao final)`. Os testes `erro_*` passam se o compilador terminar com código de saída
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
a = 5;
b = a * 2 + -3;
if (a < b && !(b == 0)) print(b); else print(a);
```

**Saída da execução do programa gerado** (é o que o `testar.sh` compara):

```text
print: 7
Variáveis ao final: a=5 b=7   (13 instruções executadas)
```

<details><summary>Saída completa da solução de referência (o código gerado pode ser diferente)</summary>

```text
=== Código de três endereços ===
    a = 5
    t1 = a * 2
    t2 = -3
    t3 = t1 + t2
    b = t3
    if a < b goto L4
    goto L2
L4:
    if b == 0 goto L2
    goto L1
L1:
    print b
    goto L3
L2:
    print a
L3:
=== Quádruplas ===
  #   op      arg1   arg2   res
  0   =       5             a
  1   *       a      2      t1
  2   neg     3             t2
  3   +       t1     t2     t3
  4   =       t3            b
  5   if<     a      b      L4
  6   goto                  L2
  7   if==    b      0      L2
  8   goto                  L1
  9   print   b             
  10  goto                  L3
  11  print   a             
=== Execução na máquina virtual ===
print: 7
Variáveis ao final: a=5 b=7   (13 instruções executadas)
```

</details>

#### `ok_2.txt`

**Entrada:**

```text
// curto-circuito: se i == 7 já for verdadeiro, i % 2 == 0 nem é avaliado
i = 0;
soma = 0;
while (i < 10 && soma <= 20) {
    if (i == 7 || i % 2 == 0) soma = soma + i * 2;
    else soma = soma - 1;
    i = i + 1;
}
print(soma);
par = (soma % 2 == 0);
print(par);
```

**Saída da execução do programa gerado** (é o que o `testar.sh` compara):

```text
print: 21
print: 0
Variáveis ao final: i=7 soma=21 par=0   (147 instruções executadas)
```

<details><summary>Saída completa da solução de referência (o código gerado pode ser diferente)</summary>

```text
=== Código de três endereços ===
    i = 0
    soma = 0
L1:
    if i < 10 goto L4
    goto L3
L4:
    if soma <= 20 goto L2
    goto L3
L2:
    if i == 7 goto L5
    goto L8
L8:
    t1 = i % 2
    if t1 == 0 goto L5
    goto L6
L5:
    t2 = i * 2
    t3 = soma + t2
    soma = t3
    goto L7
L6:
    t4 = soma - 1
    soma = t4
L7:
    t5 = i + 1
    i = t5
    goto L1
L3:
    print soma
    t7 = soma % 2
    if t7 == 0 goto L9
    goto L10
L9:
    t6 = 1
    goto L11
L10:
    t6 = 0
L11:
    par = t6
    print par
=== Quádruplas ===
  #   op      arg1   arg2   res
  0   =       0             i
  1   =       0             soma
  2   if<     i      10     L4
  3   goto                  L3
  4   if<=    soma   20     L2
  5   goto                  L3
  6   if==    i      7      L5
  7   goto                  L8
  8   %       i      2      t1
  9   if==    t1     0      L5
  10  goto                  L6
  11  *       i      2      t2
  12  +       soma   t2     t3
  13  =       t3            soma
  14  goto                  L7
  15  -       soma   1      t4
  16  =       t4            soma
  17  +       i      1      t5
  18  =       t5            i
  19  goto                  L1
  20  print   soma          
  21  %       soma   2      t7
  22  if==    t7     0      L9
  23  goto                  L10
  24  =       1             t6
  25  goto                  L11
  26  =       0             t6
  27  =       t6            par
  28  print   par           
=== Execução na máquina virtual ===
print: 21
print: 0
Variáveis ao final: i=7 soma=21 par=0   (147 instruções executadas)
```

</details>


### 7.2 Programas com erro

#### `erro_1.txt`

```text
x = 1;
if (x > 0 && ) print(x);
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro sintático [2:14]: token inesperado ')'
```

#### `erro_2.txt`

```text
x = y + 1;
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro semântico [1:5]: variável 'y' usada antes de receber valor
```

#### `erro_3.txt`

```text
x = 10;
z = 0;
print(x / z);
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro de execução: divisão por zero em t1 = x / z
```


Escreva também **pelo menos 2 testes `ok_` e 2 testes `erro_` seus**, cobrindo casos que estes não cobrem.

## 8. Desafios extras (opcionais)

- Mostrar os **blocos básicos** e o grafo de fluxo de controle (CFG).
- Contar quantas instruções foram executadas e comparar com o código sem curto-circuito.

## 9. Critérios de avaliação

| Critério | Peso |
|---|---|
| Análise léxica (tokens, posições, erros léxicos) | 20% |
| Análise sintática (gramática sem conflitos / avisos, precedência, erros sintáticos) | 25% |
| Semântica e back-end (regras da seção 4, saída da seção 5) | 35% |
| Mensagens de erro (tipo, `[linha:coluna]`, clareza) | 10% |
| Testes próprios e README (como compilar, executar e testar) | 10% |
