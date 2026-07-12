# Projeto Integrador Final (A3) — Aula 27

**Disciplina:** Teoria da Computação e Compiladores  
**Unidade:** UA8 — ANTLR, Projeto Integrador e Tendências  
**Prof. Giovane Barcelos** — [💻 Códigos Fonte](https://github.com/giovanebarcelos/tcc-sandbox)  
**Valor:** 40 pontos (A3) | **Prazo:** 2 semanas  

---

## Objetivo

Construir um **mini-compilador ou interpretador** de uma linguagem simples, integrando todas as fases: léxica → sintática → semântica → geração de código (ou interpretação direta).

---

## Especificação da Linguagem

A linguagem deve suportar no mínimo:

```
program  → stmt+
stmt     → ID '=' expr ';'
         | 'if' '(' expr ')' block ('else' block)?
         | 'while' '(' expr ')' block
         | 'print' '(' expr ')' ';'
block    → '{' stmt* '}'
expr     → expr ('+'|'-'|'*'|'/') term | term
term     → NUM | ID | '(' expr ')'
```

### Funcionalidades obrigatórias

1. **Scanner** (léxico): NUM, ID, KEYWORD, OP, PUNCT
2. **Parser** (sintático): gramática acima com precedência correta
3. **Tabela de símbolos** (semântico): variáveis com tipo e valor
4. **Avaliação ou geração de código**: interpretar diretamente OU gerar código C
5. **Tratamento de erros**: mensagens com linha/coluna

---

## Stack de Implementação (escolha UMA — a mesma do PP3)

| Stack | Arquivos esperados |
|---|---|
| **JFlex + JCup** | `.flex`, `.cup`, `Main.java` |
| **ANTLR 4** | `.g4`, `Main.java` ou `main.py` |
| **PLY** | `calc.py` |

---

## Menu de Domínios (cada equipe escolhe UM — evita cópia)

1. **Calculadora com variáveis** (básico)
2. **Conversor de temperatura** (C ↔ F ↔ K)
3. **Avaliador de expressões booleanas** (AND, OR, NOT)
4. **Mini-linguagem de robô** (forward, turn, repeat)
5. **Analisador de JSON simplificado**
6. **Gerador de HTML** a partir de markup simples
7. **Calculadora de matriz** (soma, multiplicação)
8. **Interpretador de Brainfuck** (desafio)

---

## Checkpoints

| Checkpoint | Descrição | Prazo |
|---|---|---|
| **CP1** | Scanner + Parser funcionando (tokens + AST) | 3 dias |
| **CP2** | Semântica + Tabela de Símbolos | 5 dias |
| **CP3** | Interpretação ou Geração de Código C | 7 dias |
| **Entrega** | Apresentação + código + documentação | 14 dias |

---

## Rubrica de Avaliação (40 pontos)

| Critério | Pontos | Descrição |
|---|---|---|
| **Scanner** | 5 | Todos os tokens reconhecidos corretamente |
| **Parser** | 8 | Gramática correta, precedência e associatividade |
| **Semântica** | 7 | Tabela de símbolos, verificação de variáveis |
| **Geração/Interpretação** | 10 | Código C gerado OU interpretação correta |
| **Tratamento de erros** | 4 | Mensagens amigáveis com linha/coluna |
| **Documentação** | 3 | README com instruções de build e uso |
| **Apresentação** | 3 | Demo clara (5 min por equipe) |
| **Total** | **40** | |

---

## Entrega

- Código-fonte no GitHub (repositório da equipe)
- README com: instruções de build, exemplos de uso, limitações
- Apresentação de 5 minutos na Aula 28 (demo ao vivo)
- 5 programas-exemplo testados
