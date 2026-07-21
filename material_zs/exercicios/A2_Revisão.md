# Material de Estudo - Teoria da Computação e Compiladores  
  
## 📚 Conteúdo Essencial para Dominar a Disciplina  
  
---  
  
## 1. GRAMÁTICAS FORMAIS  
  
### Componentes de uma Gramática Formal  
  
Uma gramática formal G é definida por uma quádrupla: **G = (N, Σ, P, S)**  
  
- **N**: Conjunto de símbolos não-terminais (variáveis)  
- **Σ**: Conjunto de símbolos terminais (alfabeto)  
- **P**: Conjunto de regras de produção  
- **S**: Símbolo inicial (axioma)  
  
### Gramáticas Livres de Contexto (GLC)  
  
**Características:**  
  
- Regras na forma: A → α, onde A é não-terminal e α é sequência de terminais e não-terminais  
- Permitem aninhamento e recursão  
- Usadas na análise sintática de compiladores  
  
**Estratégias para construir GLC:**  
  
1. **Identificar padrões de repetição**  
   
   - Use recursão à esquerda: A → Aα  
   - Use recursão à direita: A → αA  
   - Use recursão central: A → αAβ  
  
2. **Lidar com múltiplas dependências**  
   
   - Separe em não-terminais diferentes quando há contadores independentes  
   - Use um não-terminal para cada padrão de repetição  
  
3. **Exemplos de padrões comuns:**  
   
   ```  
   Mesmo número de a's e b's:  
   S → aSb | ab | ε  
   
   Palíndromos:  
   S → aSa | bSb | a | b | ε  
   
   Parênteses balanceados:  
   S → SS | (S) | ε  
   ```  
  
### Conversão entre GLC e Expressões Regulares  
  
**Quando possível:**  
  
- GLC que gera linguagem regular pode ser convertida  
- Elimine recursão e simplifique  
- Identifique padrões sequenciais (concatenação)  
- Identifique escolhas (união |)  
- Identifique repetições (*)  
  
**Exemplo:**  
  
```  
S → aS | Sb | c  
Equivale a: a*cb*  
```  
  
---  
  
## 2. CONCEITOS FUNDAMENTAIS  
  
### Alfabeto, Símbolo e Cadeia  
  
- **Símbolo**: Unidade básica indivisível (ex: 'a', '0', '+')  
  
- **Alfabeto (Σ)**: Conjunto **finito** de símbolos  
  
  - Exemplo: Σ = {0, 1} (alfabeto binário)  
  - Exemplo: Σ = {a, b, c}  
  
- **Cadeia (palavra, string)**: Sequência **finita** de símbolos do alfabeto  
  
  - Exemplo: "0101", "abc", "aaa"  
  - Cadeia vazia: ε (não contém símbolos)  
  - **Comprimento**: número de símbolos na cadeia  
  
### Linguagem Formal  
  
- **Definição**: Conjunto de cadeias sobre um alfabeto  
- Pode ser **finita** ou **infinita**  
- Exemplos:  
  - L₁ = {a, ab, abc} (finita)  
  - L₂ = {aⁿbⁿ | n ≥ 0} (infinita)  
  - L₃ = {todas as strings binárias} (infinita)  
  
---  
  
## 3. EXPRESSÕES REGULARES  
  
### Operadores Básicos  
  
- **Concatenação**: ab (a seguido de b)  
- **União (alternativa)**: a|b (a ou b)  
- **Fecho de Kleene**: a* (zero ou mais a's)  
- **Fecho positivo**: a+ (um ou mais a's)  
- **Opcional**: a? (zero ou um a)  
  
### Metacaracteres Importantes  
  
- **^**: início da string  
- **$**: fim da string  
- **[0-9]**: classe de caracteres (dígitos)  
- **[a-z]**: letras minúsculas  
- **\d**: dígito (equivale a [0-9])  
- **\s**: espaço em branco  
- **.**: qualquer caractere  
- **{n}**: exatamente n repetições  
- **{n,m}**: de n até m repetições  
  
### Exemplos Práticos  
  
```regex  
CPF (999.999.999-99):  
^[0-9]{3}[.][0-9]{3}[.][0-9]{3}-[0-9]{2}$  
  
Email simples:  
^[a-z]+@[a-z]+\.[a-z]+$  
  
CEP (99999-999):  
^[0-9]{5}-[0-9]{3}$  
  
Identificador (começa com letra):  
^[a-zA-Z][a-zA-Z0-9_]*$  
```  
  
---  
  
## 4. HIERARQUIA DE CHOMSKY  
  
### Os Quatro Tipos de Linguagens  
  
**Tipo 3 - Linguagens Regulares**  
  
- Gramáticas: Regulares  
- Reconhecedor: Autômato Finito (AF)  
- Poder: Mais restrito  
- Uso: Análise Léxica  
- Exemplos: a*b*, (a|b)*c  
  
**Tipo 2 - Linguagens Livres de Contexto**  
  
- Gramáticas: Livres de Contexto (GLC)  
- Reconhecedor: Autômato de Pilha (AP)  
- Poder: Intermediário  
- Uso: Análise Sintática  
- Exemplos: aⁿbⁿ, parênteses balanceados  
  
**Tipo 1 - Linguagens Sensíveis ao Contexto**  
  
- Gramáticas: Sensíveis ao Contexto  
- Reconhecedor: Autômato Limitado Linear  
- Poder: Mais poderoso que Tipo 2  
- Exemplos: aⁿbⁿcⁿ  
  
**Tipo 0 - Linguagens Irrestritas**  
  
- Gramáticas: Irrestritas  
- Reconhecedor: Máquina de Turing  
- Poder: Máximo (computável)  
- Exemplos: qualquer linguagem computável  
  
### Relação de Inclusão  
  
```  
Tipo 3 ⊂ Tipo 2 ⊂ Tipo 1 ⊂ Tipo 0  
Regular ⊂ Livre Contexto ⊂ Sensível Contexto ⊂ Irrestrita  
```  
  
### Aplicação em Compiladores  
  
| Fase              | Tipo de Gramática          | Reconhecedor      |  
| ----------------- | -------------------------- | ----------------- |  
| Análise Léxica    | Regular (Tipo 3)           | Autômato Finito   |  
| Análise Sintática | Livre de Contexto (Tipo 2) | Autômato de Pilha |  
  
---  
  
## 5. AUTÔMATOS FINITOS DETERMINÍSTICOS (AFD)  
  
### Definição Formal  
  
Um AFD é uma quíntupla: **M = (Q, Σ, δ, q₀, F)**  
  
- **Q**: Conjunto finito de estados  
- **Σ**: Alfabeto de entrada  
- **δ**: Função de transição (Q × Σ → Q)  
- **q₀**: Estado inicial (q₀ ∈ Q)  
- **F**: Conjunto de estados finais/aceitação (F ⊆ Q)  
  
### Características Importantes  
  
✅ **Determinístico**: Para cada estado e símbolo, existe **exatamente uma** transição  
✅ **Não usa transições ε** (épsilon/vazio)  
✅ **Sem memória**: Decisão baseada apenas no estado atual e símbolo lido  
✅ **Reconhece apenas linguagens regulares**  
  
### Projetando um AFD  
  
**Passo 1**: Identifique o que precisa "lembrar"  
  
- Últimos símbolos lidos?  
- Paridade (par/ímpar)?  
- Padrão específico?  
  
**Passo 2**: Determine estados necessários  
  
- Estado inicial sempre necessário  
- Estados para "histórico" relevante  
- Estados de aceitação  
- Possivelmente estado de erro/rejeição  
  
**Passo 3**: Defina transições  
  
- Para cada estado, defina transição para cada símbolo do alfabeto  
- Garanta determinismo (uma única transição)  
  
### Exemplos Clássicos  
  
**Linguagem: strings que terminam em "01"**  
  
```  
Estados necessários: 3  
- q₀: estado inicial (nenhum padrão ainda)  
- q₁: último símbolo foi '0'  
- q₂: últimos símbolos foram '01' (ACEITAÇÃO)  
```  
  
**Linguagem: strings com número par de 0's**  
  
```  
Estados necessários: 2  
- q₀: par de 0's (ACEITAÇÃO, inclui zero 0's)  
- q₁: ímpar de 0's  
```  
  
**Senha de 3 caracteres terminando em 'c'**  
  
```  
Estados necessários: 4  
- q₀: nenhum caractere lido  
- q₁: 1 caractere lido  
- q₂: 2 caracteres lidos  
- q₃: 3 caracteres, último é 'c' (ACEITAÇÃO)  
```  
  
### Minimização de Estados  
  
Use apenas os estados **estritamente necessários**:  
  
- Não crie estados redundantes  
- Estados devem representar distinções relevantes  
- Combine estados equivalentes  
  
---  
  
## 6. COMPILADORES: ESTRUTURA E FASES  
  
### O que é um Compilador?  
  
**Definição**: Programa que traduz código-fonte (linguagem de alto nível) em código-objeto executável (linguagem de máquina ou intermediária)  
  
### Fases da Compilação (EM ORDEM)  
  
#### **1. Análise Léxica (Lexer/Scanner)**  
  
**Função principal**: Identificar e classificar tokens no código-fonte  
  
**Atividades:**  
  
- Lê caracteres do código-fonte  
- Agrupa caracteres em **lexemas**  
- Gera sequência de **tokens**  
- Remove comentários e espaços em branco  
- Associa erros às linhas do código  
- Usa expressões regulares e autômatos finitos  
  
**Token vs Lexema:**  
  
- **Lexema**: Sequência de caracteres (ex: "while", "123", "count")  
- **Token**: Categoria do lexema (ex: KEYWORD, NUMBER, IDENTIFIER)  
  
**Não faz:**  
  
- ❌ Não cria árvore sintática  
- ❌ Não verifica semântica  
- ❌ Não verifica tipos  
  
#### **2. Análise Sintática (Parser)**  
  
**Função principal**: Estruturar o código usando gramáticas formais  
  
**Atividades:**  
  
- Recebe tokens do analisador léxico  
- Usa **gramáticas livres de contexto** (Tipo 2)  
- Constrói árvore sintática (parse tree)  
- Identifica erros de estrutura/organização  
- Verifica se código segue regras gramaticais  
  
**Exemplo:**  
  
```  
Entrada: IF LPAREN expr RPAREN stmt  
Saída: Árvore representando estrutura if  
```  
  
#### **3. Análise Semântica**  
  
**Função principal**: Verificar significado e consistência  
  
**Atividades:**  
  
- Verifica tipos das variáveis  
- Verifica compatibilidade de tipos  
- Verifica declarações de variáveis  
- Análise de escopo  
- Verifica conversões de tipo  
  
**Exemplo:**  
  
```c  
int x = "texto"; // ERRO: incompatibilidade de tipos  
```  
  
#### **4. Geração de Código Intermediário**  
  
**Função principal**: Criar representação intermediária  
  
**Características:**  
  
- ✅ **Independente da máquina/arquitetura**  
- Facilita otimizações  
- Permite portabilidade  
- Mais abstrato que código de máquina  
- Exemplos: código de três endereços, bytecode  
  
**Vantagens:**  
  
- Mesmo código intermediário serve para múltiplas arquiteturas  
- Otimizações podem ser aplicadas uma vez  
- Simplifica o compilador  
  
#### **5. Otimização de Código**  
  
**Função principal**: Melhorar eficiência do código  
  
**Atividades:**  
  
- Elimina redundâncias  
- Remove código morto (nunca executado)  
- Otimiza loops  
- Ajusta para arquitetura específica  
- Melhora uso de registradores  
  
**Tipos:**  
  
- Independente de máquina  
- Dependente de máquina  
  
#### **6. Geração de Código Final**  
  
**Função principal**: Produzir código executável  
  
**Atividades:**  
  
- Traduz código intermediário para linguagem de máquina  
- Aloca registradores  
- Gera instruções específicas da arquitetura  
  
---  
  
## 7. MÁQUINA DE TURING  
  
### Definição  
  
Modelo computacional mais poderoso da hierarquia de Chomsky, capaz de reconhecer qualquer linguagem computável.  
  
### Componentes  
  
- Fita infinita (memória)  
- Cabeça de leitura/escrita  
- Conjunto de estados  
- Função de transição  
- Estados inicial e finais  
  
### Máquina de Turing Decidível (Total)  
  
**Característica principal**: **Sempre para** (para qualquer entrada)  
  
**Também chamada:**  
  
- Máquina de Turing Total  
- Máquina de Turing que Decide  
  
**Linguagens reconhecidas**: **Linguagens Recursivas**  
  
**Propriedade**: Pode determinar com certeza se uma cadeia pertence ou não à linguagem  
  
### Diferença: Recursiva vs Recursivamente Enumerável  
  
| Linguagem Recursiva            | Recursivamente Enumerável     |  
| ------------------------------ | ----------------------------- |  
| Máquina sempre para            | Máquina pode não parar        |  
| Sempre decide (aceita/rejeita) | Aceita, mas pode não rejeitar |  
| Mais restrita                  | Mais geral                    |  
  
---  
  
## 8. SÍNTESE: LINGUAGENS E RECONHECEDORES  
  
### Tabela Completa  
  
| Linguagem                  | Gramática         | Reconhecedor      | Fechamento             |  
| -------------------------- | ----------------- | ----------------- | ---------------------- |  
| Regular (Tipo 3)           | Regular           | Autômato Finito   | União, concatenação, * |  
| Livre Contexto (Tipo 2)    | Livre Contexto    | Autômato de Pilha | União, concatenação, * |  
| Sensível Contexto (Tipo 1) | Sensível Contexto | Autômato Linear   | União, concatenação, * |  
| Recursiva                  | Irrestrita        | MT que para       | União, concatenação, * |  
| Rec. Enumerável (Tipo 0)   | Irrestrita        | Máquina de Turing | União, concatenação, * |  
  
---  
  
## 9. ESTRATÉGIAS DE RESOLUÇÃO  
  
### Para Gramáticas  
  
1. **Leia cuidadosamente as restrições** (n ≥ 0, m > 0, etc.)  
2. **Identifique relações entre símbolos** (proporções, igualdades)  
3. **Teste com exemplos pequenos**  
4. **Verifique casos extremos** (n=0, m=1)  
5. **Use não-terminais separados** para contadores independentes  
  
### Para Autômatos  
  
1. **Determine o que precisa "lembrar"**  
2. **Conte estados mínimos necessários**  
3. **Lembre-se: AFD não tem transições ε**  
4. **Desenhe mentalmente ou no papel**  
5. **Teste com exemplos de aceitação e rejeição**  
  
### Para Compiladores  
  
1. **Memorize a ordem das fases**  
2. **Associe cada fase à sua função principal**  
3. **Lembre-se dos tipos de gramática usados:**  
   - Léxica → Regular (Tipo 3)  
   - Sintática → Livre Contexto (Tipo 2)  
4. **Código intermediário = Independente de máquina**  
  
### Para Hierarquia de Chomsky  
  
1. **Visualize a inclusão**: 3 ⊂ 2 ⊂ 1 ⊂ 0  
2. **Associe tipo ao reconhecedor:**  
   - 3 → Autômato Finito  
   - 2 → Autômato de Pilha  
   - 0 → Máquina de Turing  
3. **Lembre-se das aplicações práticas**  
  
---  
  
## 10. CONCEITOS QUE NÃO PODEM SER CONFUNDIDOS  
  
### Alfabeto é FINITO  
  
❌ Alfabeto infinito não existe na teoria  
✅ Alfabeto sempre tem número finito de símbolos  
  
### Linguagens podem ser infinitas  
  
✅ Linguagem pode ter infinitas cadeias  
✅ Exemplo: {aⁿ | n ≥ 0} é infinita  
  
### AFD não usa transições ε  
  
❌ Transições vazias são de autômatos NÃO-determinísticos  
✅ AFD: toda transição consome um símbolo  
  
### Análise Léxica NÃO cria árvore  
  
❌ Árvore sintática é da análise sintática  
✅ Léxica gera apenas sequência linear de tokens  
  
### Otimização NÃO é opcional  
  
❌ Embora possa ser pulada, impacta muito o desempenho  
✅ É fase importante para eficiência  
  
---  
  
## ✅ CHECKLIST DE CONCEITOS ESSENCIAIS  
  
- [ ] Sei os 4 componentes de uma gramática formal (N, Σ, P, S)  
- [ ] Entendo a diferença entre símbolo, alfabeto e cadeia  
- [ ] Sei que alfabeto é finito e linguagem pode ser infinita  
- [ ] Conheço os operadores de expressões regulares  
- [ ] Sei construir expressões regulares para padrões comuns  
- [ ] Conheço os 4 tipos da Hierarquia de Chomsky  
- [ ] Sei qual reconhecedor para cada tipo de linguagem  
- [ ] Entendo que Tipo 3 → Léxica e Tipo 2 → Sintática  
- [ ] Sei projetar AFDs simples  
- [ ] Entendo que AFD não usa transições ε  
- [ ] Memorizei as 6 fases da compilação em ordem  
- [ ] Sei que análise léxica identifica tokens  
- [ ] Sei que análise sintática usa GLC e cria árvore  
- [ ] Entendo que código intermediário é independente de máquina  
- [ ] Sei que Máquina de Turing que para → Linguagens Recursivas  
  
---  
  
**Bons estudos! 🎓**  
