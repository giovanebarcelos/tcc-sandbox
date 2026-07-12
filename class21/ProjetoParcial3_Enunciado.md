# Projeto Parcial 3 (PP3) — Aula 21

**Disciplina:** Teoria da Computação e Compiladores  
**Unidade:** UA6 — Análise Sintática  
**Prof. Giovane Barcelos** — [💻 Códigos Fonte](https://github.com/giovanebarcelos/tcc-sandbox)  
**Valor:** 10 pontos (parte da A3) | **Prazo:** 1 semana  

---

## Objetivo

Construir um **parser** para uma linguagem própria simples, na **stack de sua escolha** (JFlex+JCup, ANTLR 4 ou PLY), integrando scanner e parser.

---

## Especificação

Implemente um parser que reconheça a seguinte linguagem (mini-calculadora com variáveis):

```
program  → stmt+
stmt     → ID '=' expr ';'
         | 'print' '(' expr ')' ';'
expr     → expr ('+'|'-') term
         | term
term     → term ('*'|'/') factor
         | factor
factor   → NUM | ID | '(' expr ')'
```

### Requisitos

1. **Scanner**: reconhecer `NUM`, `ID`, `KEYWORD` (print), `OP` (+, -, *, /, =), `PUNCT` (;, (, ))
2. **Parser**: implementar a gramática acima com **tratamento de precedência** correto (`*`/`/` antes de `+`/`-`)
3. **Avaliação**: calcular o resultado das expressões (com variáveis armazenadas em uma tabela de símbolos)
4. **Erros**: reportar erros sintáticos com linha/coluna

### Exemplo de entrada

```
x = 3 + 4 * 2;
print(x);
y = (x - 1) / 2;
print(y);
```

### Saída esperada

```
x = 11
11
y = 5
5
```

---

## Stack de implementação (escolha UMA)

| Stack | Arquivos | Build |
|---|---|---|
| **JFlex + JCup** | `calc.flex`, `calc.cup`, `Main.java` | `compilar_jflex_jcup.sh` |
| **ANTLR 4** | `Calc.g4`, `Main.java` ou `main.py` | `antlr4.sh` |
| **PLY** | `calc.py` | `python3 calc.py` |

> A stack escolhida aqui será usada também no **Projeto Integrador A3** (Aula 27).

---

## Critérios de avaliação

| Item | Pontos |
|---|---|
| Scanner correto (todos os tokens) | 2,0 |
| Parser correto (precedência e associatividade) | 3,0 |
| Avaliação com variáveis (tabela de símbolos) | 2,0 |
| Tratamento de erros (linha/coluna) | 2,0 |
| Documentação (README do projeto) | 1,0 |
| **Total** | **10,0** |

---

## Entrega

Compacte os fontes + README em `PP3_NomeSobrenome.zip` e envie pelo portal.
