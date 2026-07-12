# Projeto Parcial 2 (PP2) — Aula 14

**Disciplina:** Teoria da Computação e Compiladores  
**Unidade:** UA4 — MT, LSC e Computabilidade  
**Prof. Giovane Barcelos** — [💻 Códigos Fonte](https://github.com/giovanebarcelos/tcc-sandbox)  
**Valor:** 10 pontos (parte da A3) | **Prazo:** 1 semana  

---

## Objetivo

Integrar os três modelos de computação estudados (AFD, Autômato de Pilha e Máquina de Turing) resolvendo problemas que destacam as diferenças de poder expressivo entre eles.

---

## Especificação

Entregue **três artefatos** (arquivos `.jff` do JFLAP) + **relatório** (1 página):

### 1. AFD — Sem subpalavra `aba` (3 pts)

Construa um AFD sobre Σ = {a, b} que aceite **todas as palavras que NÃO contêm** `aba` como subpalavra.

- **Exemplos aceitos:** `ε`, `a`, `b`, `bb`, `aab`, `bba`, `bbbb`, `aabbb`
- **Exemplos rejeitados:** `aba`, `abab`, `baba`, `aabaa`, `ababa`
- **Entrega:** `TCC1401-SemAba.jff`

### 2. Autômato de Pilha — Palíndromos de tamanho par (3 pts)

Construa um AP que reconheça **palíndromos de tamanho par** sobre Σ = {a, b}.

- **Exemplos aceitos:** `ε`, `aa`, `bb`, `abba`, `baab`, `aaaa`, `abbaabba`
- **Exemplos rejeitados:** `a`, `ab`, `aba`, `abbaa`, `abcba`
- **Dica:** empilhe a primeira metade; desempilhe na segunda metade (use o centro para inverter)
- **Entrega:** `TCC1402-PalindromoPar.jff`

### 3. Máquina de Turing — Inversão binária (3 pts)

Construa uma MT que **inverte** uma palavra binária (lê da esquerda para a direita e escreve o reverso).

- `101` → `101` (palíndromo, fica igual)
- `110` → `011`
- `1001` → `1001`
- `1100` → `0011`
- **Dica:** use a fita como rascunho; copie para o final invertendo
- **Entrega:** `TCC1403-InversaoBinaria.jff`

### 4. Relatório comparativo (1 pt)

Escreva 1 página comparando os três modelos:

- Por que o AFD **não consegue** reconhecer palíndromos de tamanho arbitrário?
- Por que o AP **não consegue** inverter uma palavra binária (precisa de MT)?
- Qual a relação com a **Hierarquia de Chomsky** (tipos 3, 2, 1)?

---

## Critérios de avaliação

| Item | Pontos |
|---|---|
| AFD correto (testa 8+ palavras no JFLAP Multiple Run) | 3,0 |
| AP correto (aceita/rejeita conforme especificação) | 3,0 |
| MT correta (inverte corretamente 5+ casos) | 3,0 |
| Relatório comparativo claro e técnico | 1,0 |
| **Total** | **10,0** |

---

## Entrega

Compacte os 3 arquivos `.jff` + relatório `.pdf` em `PP2_NomeSobrenome.zip` e envie pelo portal.
