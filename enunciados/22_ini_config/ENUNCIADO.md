# Enunciado 22 — Arquivos INI com interpolação

**Teoria da Computação e Compiladores — 6M (Digital) · Prof. Giovane Barcelos**

| | |
|---|---|
| **Nível** | ⭐⭐⭐⭐ (avançado) |
| **Aulas relacionadas** | 17, 22 e 26 (modos léxicos) |
| **Ferramentas** | ANTLR 4 **ou** JFlex + JCup (implementar com as duas vale como desafio extra) |
| **Testes fornecidos** | 2 válido(s) (`ok_*`) e 5 inválido(s) (`erro_*`), na pasta [`testes/`](testes/) |

> Leia antes as **regras gerais** em [`../README.md`](../README.md): formato das mensagens de erro, códigos de
> saída, como usar o `testar.sh`, o que entregar e como será avaliado.

---

## 1. Objetivo

Analisar arquivos de configuração **INI** com seções, comentários, valores com aspas e **interpolação**
`${chave}` / `${secao.chave}`. O programa resolve as referências, detecta **referências circulares** e
infere o tipo de cada valor. São necessários **três modos léxicos**.

## 2. Análise léxica

- Modo padrão: comentários `;` ou `#` até o fim da linha, `[ ]`, `=` (entra no modo VALOR), `NOME`, NL.
- Modo VALOR (depois do `=`, até o fim da linha): `${` (entra no modo REF), texto entre aspas, texto livre.
- Modo REF: nome da referência e `}` (volta ao modo VALOR).

## 3. Análise sintática

```ebnf
ini   ::= { linha } EOF
linha ::= "[" NOME "]" NL  |  NOME "=" { parte } NL  |  NL
parte ::= TEXTO | ASPAS | "${" NOME "}"
```

## 4. Análise semântica e regras da linguagem

- Chaves antes da primeira seção ficam na seção `(global)`. O nome completo de uma chave é `secao.chave`.
- Referência `${x}` procura primeiro na seção atual e depois na global; `${s.x}` procura a chave `x` da seção `s`.
- Erros: chave repetida na seção, seção repetida, referência inexistente, **referência circular** (DFS com marcação).
- Tipos: `inteiro`, `real`, `booleano` (`true false yes no on off`), `vazio` ou `texto` (o valor **já interpolado**).

## 5. Saída

Cada seção com as chaves, os valores interpolados e o tipo inferido. Veja os casos de teste.

## 6. Dicas

- Espaços antes e depois do valor são removidos; o conteúdo entre aspas é mantido como está.

## 7. Casos de teste

O `testar.sh` compara a saída padrão inteira, linha a linha. Os testes `erro_*` passam se o compilador terminar com código de saída
diferente de 0 e escrever uma mensagem na saída de erro; o texto da mensagem de referência é um **modelo**
(o tipo do erro e a posição `[linha:coluna]` devem bater; a redação pode variar).

```bash
../testar.sh . <comando que executa o seu compilador>
# ex.: ../testar.sh . java -cp "build/classes:$ANTLR_JAR" Main
```

### 7.1 Programas válidos

#### `ok_1.ini`

**Entrada:**

```text
; configuração de exemplo
app = Compilador TCC
[servidor]
host  = localhost
porta = 8080
debug = true
url   = http://${host}:${porta}/api

[banco]
nome    = tcc
usuario = "  admin  "
url     = jdbc:postgresql://${servidor.host}/${nome}
timeout = 2.5
```

**Saída esperada:**

```text
(global)
  app                    = Compilador TCC                       (texto)
[servidor]
  servidor.host          = localhost                            (texto)
  servidor.porta         = 8080                                 (inteiro)
  servidor.debug         = true                                 (booleano)
  servidor.url           = http://localhost:8080/api            (texto)
[banco]
  banco.nome             = tcc                                  (texto)
  banco.usuario          =   admin                              (texto)
  banco.url              = jdbc:postgresql://localhost/tcc      (texto)
  banco.timeout          = 2.5                                  (real)
2 seção(ões), 9 chave(s)
```

#### `ok_2.ini`

**Entrada:**

```text
# referências em cadeia
[caminhos]
raiz  = /opt/tcc
bin   = ${raiz}/bin
jflex = ${bin}/jflex
vazio =
```

**Saída esperada:**

```text
[caminhos]
  caminhos.raiz          = /opt/tcc                             (texto)
  caminhos.bin           = /opt/tcc/bin                         (texto)
  caminhos.jflex         = /opt/tcc/bin/jflex                   (texto)
  caminhos.vazio         =                                      (vazio)
1 seção(ões), 4 chave(s)
```


### 7.2 Programas com erro

#### `erro_1.ini`

```text
[a]
x = 1
x = 2
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro semântico [3:1]: chave 'a.x' repetida
```

#### `erro_2.ini`

```text
[a]
x = ${y}
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro semântico [2:7]: referência a chave inexistente 'y'
```

#### `erro_3.ini`

```text
[a]
x = ${y}
y = ${x}
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro semântico [3:7]: referência circular envolvendo 'a.x'
```

#### `erro_4.ini`

```text
[a
x = 1
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro sintático [1:3]: token inesperado '\n'
```

#### `erro_5.ini`

```text
[a]
x = ${y z}
```

Deve ser **rejeitado**. Mensagem de referência:

```text
Erro léxico [2:8]: caractere inválido ' '
```


Escreva também **pelo menos 2 testes `ok_` e 2 testes `erro_` seus**, cobrindo casos que estes não cobrem.

## 8. Desafios extras (opcionais)

- Exportar para JSON (seções viram objetos aninhados).
- Variáveis de ambiente: `${env:HOME}`.

## 9. Critérios de avaliação

| Critério | Peso |
|---|---|
| Análise léxica (tokens, posições, erros léxicos) | 20% |
| Análise sintática (gramática sem conflitos / avisos, precedência, erros sintáticos) | 25% |
| Semântica e back-end (regras da seção 4, saída da seção 5) | 35% |
| Mensagens de erro (tipo, `[linha:coluna]`, clareza) | 10% |
| Testes próprios e README (como compilar, executar e testar) | 10% |
