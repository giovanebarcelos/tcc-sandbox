# Enunciado 11 — Formas geométricas → SVG

**Teoria da Computação e Compiladores — 6M (Digital) · Prof. Giovane Barcelos**

| | |
|---|---|
| **Nível** | ⭐⭐ (básico) |
| **Aulas relacionadas** | 22 (análise semântica) |
| **Ferramentas** | ANTLR 4 **ou** JFlex + JCup (implementar com as duas vale como desafio extra) |
| **Testes fornecidos** | 2 válido(s) (`ok_*`) e 5 inválido(s) (`erro_*`), na pasta [`testes/`](testes/) |

> Leia antes as **regras gerais** em [`../README.md`](../README.md): formato das mensagens de erro, códigos de
> saída, como usar o `testar.sh`, o que entregar e como será avaliado.

---

## 1. Objetivo

Uma pequena linguagem para **descrever figuras**. O compilador valida as propriedades de cada forma,
calcula **área e perímetro** e gera um desenho **SVG**.

## 2. Análise léxica

- Palavras-chave: `shape`, os tipos `rectangle circle triangle square` e as propriedades `width height radius base side color position`.
- `ID`, `NUMBER` (inteiro ou real), `COLOR` (`#` seguido de exatamente 6 dígitos hexadecimais), `: , { }`.

## 3. Análise sintática

```ebnf
programa ::= { forma } EOF
forma    ::= "shape" ID ":" tipo "{" prop { "," prop } "}"
tipo     ::= "rectangle" | "circle" | "triangle" | "square"
prop     ::= ("width" | "height" | "radius" | "base" | "side") ":" NUMBER
           | "color" ":" COLOR
           | "position" ":" NUMBER "," NUMBER
```

## 4. Análise semântica e regras da linguagem

| Tipo | Propriedades obrigatórias | Área | Perímetro |
|---|---|---|---|
| `rectangle` | `width`, `height` | w·h | 2(w+h) |
| `square` | `side` | s² | 4s |
| `circle` | `radius` | πr² | 2πr |
| `triangle` | `base`, `height` | b·h/2 | perímetro do triângulo isósceles de base b e altura h |

- `color` e `position` são opcionais em todas as formas.
- Propriedade que não se aplica ao tipo é erro (`'radius' não se aplica a rectangle`); obrigatória ausente também (`circle 'c' precisa de 'radius'`).
- Nome de forma repetido é erro; propriedade repetida também.

## 5. Saída

Uma linha por forma (nome, tipo, área e perímetro com 2 casas, cor e posição), a área total e um arquivo
`.svg` com o desenho. A linha que informa onde o SVG foi gravado é ignorada na comparação.

## 6. Dicas

- Os valores-padrão de cor e posição que aparecem na saída esperada fazem parte da especificação: confira nos testes.
- Abra o `.svg` gerado no navegador para conferir o desenho.

## 7. Casos de teste

O `testar.sh` compara a saída padrão inteira, linha a linha — ignorando as linhas que casam com `^Desenho SVG gravado`. Os testes `erro_*` passam se o compilador terminar com código de saída
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
shape rect1: rectangle {
    width: 100,
    height: 50,
    color: #ff0000,
    position: 10, 20
}
shape circle1: circle {
    radius: 30,
    color: #00ff00,
    position: 50, 50
}
```

**Saída esperada:**

```text
rect1      rectangle  área =    5000.00   perímetro =   300.00   cor = #ff0000   posição = (10, 20)
circle1    circle     área =    2827.43   perímetro =   188.50   cor = #00ff00   posição = (50, 50)
2 forma(s), área total = 7827.43
Desenho SVG gravado em build/out/ok_1.txt.svg
```

#### `ok_2.txt`

**Entrada:**

```text
shape telhado: triangle { base: 120, height: 60, color: #aa3300, position: 140, 40 }
shape parede: square { side: 120, color: #ffdd88, position: 140, 100 }
shape porta: rectangle { width: 30, height: 60, position: 185, 160 }
shape sol: circle { radius: 25.5, color: #ffcc00, position: 60, 60 }
```

**Saída esperada:**

```text
telhado    triangle   área =    3600.00   perímetro =   289.71   cor = #aa3300   posição = (140, 40)
parede     square     área =   14400.00   perímetro =   480.00   cor = #ffdd88   posição = (140, 100)
porta      rectangle  área =    1800.00   perímetro =   180.00   cor = #888888   posição = (185, 160)
sol        circle     área =    2042.82   perímetro =   160.22   cor = #ffcc00   posição = (60, 60)
4 forma(s), área total = 21842.82
Desenho SVG gravado em build/out/ok_2.txt.svg
```


### 7.2 Programas com erro

#### `erro_1.txt`

```text
shape r: rectangle { width: 10 height: 5 }
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro sintático [1:32]: token inesperado 'height'
```

#### `erro_2.txt`

```text
shape c: circle { color: #00ff00, position: 1, 2 }
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro semântico [1:7]: circle 'c' precisa de 'radius'
```

#### `erro_3.txt`

```text
shape r: rectangle { width: 10, height: 5, radius: 3 }
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro semântico [1:44]: 'radius' não se aplica a rectangle
```

#### `erro_4.txt`

```text
shape a: square { side: 1 }
shape a: circle { radius: 2 }
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro semântico [2:7]: forma 'a' já definida
```

#### `erro_5.txt`

```text
shape a: square { side: 1, color: red }
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro sintático [1:35]: token inesperado 'red'
```


Escreva também **pelo menos 2 testes `ok_` e 2 testes `erro_` seus**, cobrindo casos que estes não cobrem.

## 8. Desafios extras (opcionais)

- Nova forma `ellipse` (`rx`, `ry`) e a propriedade `rotation`.
- Detectar formas sobrepostas.

## 9. Critérios de avaliação

| Critério | Peso |
|---|---|
| Análise léxica (tokens, posições, erros léxicos) | 20% |
| Análise sintática (gramática sem conflitos / avisos, precedência, erros sintáticos) | 25% |
| Semântica e back-end (regras da seção 4, saída da seção 5) | 35% |
| Mensagens de erro (tipo, `[linha:coluna]`, clareza) | 10% |
| Testes próprios e README (como compilar, executar e testar) | 10% |
