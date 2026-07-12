# TCC Sandbox — Teoria da Computação e Compiladores

**Repositório de código-fonte do curso** Teoria da Computação e Compiladores (6M — Digital)  
**Prof. Giovane Barcelos**  

Este repositório será publicado em `github.com/giovanebarcelos/tcc-sandbox`. Os links nos slides das aulas apontam para `raw.githubusercontent.com/giovanebarcelos/tcc-sandbox/main/...`.

---

## Estrutura

```
repository/
  class01/ ... class28/    # Código por aula (Python + Java + diagramas + .jff/.flex/.cup/.g4)
  ferramentas/             # Jars e lançadores (JFLAP, JFlex, CUP, ANTLR)
  livros/                  # Bibliografia digital (20 arquivos)
```

---

## Contagem de artefatos por aula

| Aula | Título | Python | Java | .flex | .cup | .g4 | .jff | .mmd | Outros |
|---|---|---|---|---|---|---|---|---|---|
| 01 | Boas-vindas e Panorama | 1 | 1 | — | — | — | 1 | 1 | — |
| 02 | Alfabeto, Palavra, Linguagem | 1 | 1 | — | — | — | 1 | 1 | — |
| 03 | Hierarquia de Chomsky | 1 | 1 | — | — | — | — | 1 | — |
| 04 | AFD — Definição | 1 | 1 | — | — | — | 1 | — | — |
| 05 | AFD — Construção | 1 | 1 | — | — | — | — | — | — |
| 06 | AFN e AFNe | 1 | 1 | — | — | — | — | — | — |
| 07 | Conversões e Minimização | 1 | 1 | — | — | — | — | — | — |
| 08 | Expressões Regulares + PP1 | 1 | 1 | — | — | — | — | — | PP1 |
| 09 | GR, Transformações e GLC | 1 | 1 | — | — | — | — | — | — |
| 10 | Árvores, Ambiguidade, FNC | 1 | 1 | — | — | — | — | 1 | — |
| 11 | Autômato de Pilha | 1 | 1 | — | — | — | — | — | — |
| 12 | Máquina de Turing | 1 | 1 | — | — | — | — | — | — |
| 13 | Computabilidade | 1 | 1 | — | — | — | — | 1 | — |
| 14 | PP2 + Revisão A1 | — | — | — | — | — | — | — | PP2 |
| 15 | Compiladores — Arquitetura | 1 | 1 | — | — | — | — | 1 | — |
| 16 | Scanner Manual | 1 | 1 | — | — | — | — | 1 | — |
| 17 | JFlex e ANTLR (Léxico) | 1 | 1 | 1 | — | 1 | — | — | — |
| 18 | First/Follow e LL(1) | 2 | 1 | — | — | — | — | — | — |
| 19 | LL × LR e Shift-Reduce | 1 | 1 | — | — | — | — | 1 | — |
| 20 | LR — Itens, SLR | 1 | 1 | — | — | — | — | — | — |
| 21 | JFlex+JCup e ANTLR + PP3 | 1 | — | 1 | 1 | 1 | — | — | PP3 |
| 22 | Semântica, Tabela de Símbolos | 1 | 1 | — | — | 1 | — | — | — |
| 23 | AST, Visitor e Factory | 1 | 1 | — | — | — | — | 1 | — |
| 24 | Controle e Erros | 1 | 1 | — | — | 1 | — | — | — |
| 25 | Geração de Código e Otimização | 2 | 2 | — | — | — | — | 1 | — |
| 26 | ANTLR em Profundidade | 1 | 1 | — | — | 1 | — | — | — |
| 27 | Projeto Integrador A3 | 1 | 1 | — | — | — | — | — | A3 |
| 28 | Tendências e Encerramento | — | — | — | — | — | — | — | Godbolt |

**Totais:** 28 pares Python+Java (paridade completa), 3 `.flex`, 1 `.cup`, 5 `.g4`, 3 `.jff`, 8 `.mmd`, 3 enunciados de projeto (PP1, PP2, PP3/A3).

---

## Regras de paridade

1. **Python + Java:** todo exemplo de código existe nas duas linguagens com comportamento idêntico (mesmas entradas/saídas)
2. **JFlex/JCup ↔ ANTLR:** em todo ponto do material onde JFlex/JCup aparece, o mesmo exemplo é apresentado em ANTLR (`.g4`)
3. **PLY como espelho Python** do par JFlex+JCup

---

## Ferramentas (em `ferramentas/`)

| Ferramenta | Versão | Arquivo |
|---|---|---|
| JFLAP | 7.1 | `JFLAP7.1.jar` |
| JFlex | 1.9.1 | `jflex-full-1.9.1.jar` |
| CUP (JCup) | 11b | `java-cup-11b.jar` + `java-cup-11b-runtime.jar` |
| ANTLR | 4.13.1 | `antlr-4.13.1-complete.jar` |

Lançadores: `jflap.sh/.bat`, `antlr4.sh/.bat`, `compilar_jflex_jcup.sh/.bat`

---

## Livros (em `livros/`)

20 arquivos (~184MB): Hopcroft (PT/EN), Linz, Menezes, Aho (Dragon Book), apostilas LFA, JFLAP book, ANTLR Reference, entre outros. Todos abaixo do limite de 100MB do GitHub.
