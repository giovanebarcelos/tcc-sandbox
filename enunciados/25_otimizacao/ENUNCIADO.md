# Enunciado 25 — Otimização da AST

**Teoria da Computação e Compiladores — 6M (Digital) · Prof. Giovane Barcelos**

| | |
|---|---|
| **Nível** | ⭐⭐⭐⭐ (avançado) |
| **Aulas relacionadas** | 25 (otimização) |
| **Ferramentas** | ANTLR 4 **ou** JFlex + JCup (implementar com as duas vale como desafio extra) |
| **Testes fornecidos** | 2 válido(s) (`ok_*`) e 3 inválido(s) (`erro_*`), na pasta [`testes/`](testes/) |

> Leia antes as **regras gerais** em [`../README.md`](../README.md): formato das mensagens de erro, códigos de
> saída, como usar o `testar.sh`, o que entregar e como será avaliado.

---

## 1. Objetivo

Escrever um **otimizador** para a linguagem do enunciado 24 que transforma a AST e prova que o programa
otimizado tem o **mesmo comportamento** do original, executando os dois.

## 2. Análise léxica

O mesmo do enunciado 24.

## 3. Análise sintática

A mesma do enunciado 24.

## 4. Análise semântica e regras da linguagem

Otimizações a implementar:
- **dobramento de constantes** (`2 * 3 + 4` → `10`) e **propagação de constantes** (conservadora: invalide o que for
  atribuído dentro de laços e desvios);
- **simplificações algébricas**: `x+0`, `0+x`, `x-0`, `x*1`, `1*x`, `x*0`, `x-x`, `!!e`;
- **redução de força**: `x*2` → `x+x`;
- **eliminação de código morto**: `if` com condição constante, `while` com condição falsa.

Regras: variável usada antes de receber valor é erro; **divisão por zero detectada ao dobrar constantes é erro de
compilação** (não pode ser dobrada).

## 5. Saída

O programa original e o otimizado (com o número de nós), as otimizações aplicadas e a execução dos dois
(saída e número de operações). **As otimizações encontradas podem variar**; nos testes é comparada a
**saída** dos dois programas e a confirmação de que são idênticas.

## 6. Dicas

- Faça uma função `otimizar(no) → no` recursiva (de baixo para cima) e repita até nada mudar.

## 7. Casos de teste

O `testar.sh` compara a saída padrão inteira, linha a linha — só as linhas que casam com `saída \[|idênticas`. Os testes `erro_*` passam se o compilador terminar com código de saída
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
x = 2 * 3 + 4;          // dobramento: 10
y = x * 1 + 0;          // propagação + álgebra: 10
z = (y - 5) * (x / 2);  // tudo constante: 25
print(z);
```

**Saída esperada:**

```text
=== Original (26 nós) ===
    x = ((2 * 3) + 4);
    y = ((x * 1) + 0);
    z = ((y - 5) * (x / 2));
    print(z);
=== Otimizado (12 nós) ===
    x = 10;
    y = 10;
    z = 25;
    print(25);
=== Otimizações aplicadas ===
  7× dobramento de constantes
  4× propagação de constante
=== Execução ===
  original : saída [25] (7 operações)
  otimizado: saída [25] (0 operações)
  saídas idênticas? sim
```

#### `ok_2.txt`

**Entrada:**

```text
n = 5;
i = 0;
s = 0;
while (i < n) {         // i e s mudam no laço: não são propagados
    s = s + i * 1;
    i = i + 1;
}
t = s * 0 + n * 2;      // s*0 -> 0, n -> 5: t = 10
if (n > 10) print(999); else print(t);    // condição constante: só o else fica
while (0) print(1);                        // código morto
print(s - s + 2 * s);   // s - s -> 0;  2*s -> s+s
print(!!(i == n) && 1);
```

**Saída esperada:**

```text
=== Original (64 nós) ===
    n = 5;
    i = 0;
    s = 0;
    while (i < n)
        {
            s = (s + (i * 1));
            i = (i + 1);
        }
    t = ((s * 0) + (n * 2));
    if (n > 10)
        print(999);
    else
        print(t);
    while (0)
        print(1);
    print(((s - s) + (2 * s)));
    print((!!(i == n) && 1));
=== Otimizado (40 nós) ===
    n = 5;
    i = 0;
    s = 0;
    while (i < 5)
        {
            s = (s + i);
            i = (i + 1);
        }
    t = 10;
    print(10);
    print((s + s));
    print(((i == 5) && 1));
=== Otimizações aplicadas ===
  5× propagação de constante
  5× simplificação algébrica
  3× dobramento de constantes
  1× código morto eliminado (if com condição constante)
  1× código morto eliminado (while com condição falsa)
  1× redução de força (x*2 -> x+x)
=== Execução ===
  original : saída [10, 20, 1] (32 operações)
  otimizado: saída [10, 20, 1] (19 operações)
  saídas idênticas? sim
```


### 7.2 Programas com erro

#### `erro_1.txt`

```text
x = 1;
if (x > 0 print(x);
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro sintático [2:11]: token inesperado 'print'
```

#### `erro_2.txt`

```text
a = b * 2;
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro semântico [1:5]: variável 'b' usada antes de receber valor
```

#### `erro_3.txt`

```text
x = 4;
y = x / (x - 4);
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro semântico [2:7]: divisão por zero detectada em tempo de compilação
```


Escreva também **pelo menos 2 testes `ok_` e 2 testes `erro_` seus**, cobrindo casos que estes não cobrem.

## 8. Desafios extras (opcionais)

- Eliminação de subexpressões comuns (CSE) e de atribuições mortas (variável atribuída e nunca lida).

## 9. Critérios de avaliação

| Critério | Peso |
|---|---|
| Análise léxica (tokens, posições, erros léxicos) | 20% |
| Análise sintática (gramática sem conflitos / avisos, precedência, erros sintáticos) | 25% |
| Semântica e back-end (regras da seção 4, saída da seção 5) | 35% |
| Mensagens de erro (tipo, `[linha:coluna]`, clareza) | 10% |
| Testes próprios e README (como compilar, executar e testar) | 10% |
