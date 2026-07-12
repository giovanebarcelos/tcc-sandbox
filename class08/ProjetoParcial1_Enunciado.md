# Projeto Parcial 1 — Pacote de Autômatos e Expressões Regulares  

**Disciplina:** Teoria da Computação e Compiladores  
**Unidade:** UA2 — Linguagens Regulares  
**Prof. Giovane Barcelos** — [💻 Códigos Fonte](https://github.com/giovanebarcelos/tcc-sandbox)  

**Valor:** 10 pontos (parte da A3) | **Prazo:** 2 semanas  

---  

## Objetivo  

Consolidar os conhecimentos da UA2 produzindo um pacote com autômatos finitos e expressões regulares.  

---  

## Especificação  

### Parte 1 — 5 AFDs no JFLAP (5 pontos)  

Construa **5 AFDs** sobre {a, b} ou {0, 1}, um de cada padrão:  

1. **Prefixo:** palavras que começam com ___  
2. **Sufixo:** palavras que terminam com ___  
3. **Subpalavra:** palavras que contêm ___  
4. **Tamanho:** palavras com comprimento ___  
5. **Exclusão:** palavras que NÃO contêm ___  

Cada AFD deve ser entregue como `.jff` com pelo menos **5 palavras testadas** (Multiple Run).  

### Parte 2 — Expressões Regulares (2 pontos)  

Escreva a **ER equivalente** para 3 dos 5 AFDs da Parte 1. Converta cada ER para AFN no JFLAP e verifique a equivalência (mesmas palavras aceitas/rejeitadas).  

### Parte 3 — Validador Regex (2 pontos)  

Implemente um validador usando expressões regulares em **Python e Java** (códigos idênticos em comportamento) para **um** dos padrões reais abaixo:  

- CPF: `xxx.xxx.xxx-xx`  
- E-mail: `usuario@dominio.ext`  
- URL: `http(s)://dominio/caminho`  
- Telefone brasileiro: `(xx) xxxxx-xxxx`  

Entregue: código Python, código Java e arquivo de teste com 10 entradas.  

### Parte 4 — Relatório (1 ponto)  

Escreva **1 página** comparando:  
- AFD × AFN × ER: vantagens e desvantagens de cada formalismo  
- Qual você usaria para cada situação e por quê  

---  

## Critérios de Avaliação  

| Critério | Pontos |  
|---|---|  
| 5 AFDs corretos e testados | 5 |  
| 3 ERs equivalentes verificadas | 2 |  
| Validador Python + Java funcionando | 2 |  
| Relatório claro e comparativo | 1 |  

---  
