  
  
---  
  
### 1) Comprimento múltiplo de 4  
  
Alfabeto: `{a,b}`  
Gramática (estados q0..q3; q0 inicial e aceitante):  
  
```  
q0 -> a q1 | b q1 | ⊣  
q1 -> a q2 | b q2  
q2 -> a q3 | b q3  
q3 -> a q0 | b q0  
```  
  
---  
  
### 2) Termina em **aba**  
  
Alfabeto: `{a,b}`  
Gramática (q3 aceitante — sufixo "aba"):  
  
```  
q0 -> a q1 | b q0  
q1 -> a q1 | b q2  
q2 -> a q3 | b q0  
q3 -> a q1 | b q2 | ⊣  
```  
  
---  
  
### 3) Número par de **a** e ímpar de **b**  
  
Alfabeto: `{a,b}`  
Notação estados: q_pp (par a, par b), q_pi (par a, ímpar b), q_ip (ímpar a, par b), q_ii (ímpar a, ímpar b). Start = `q_pp`, aceitante = `q_pi`.  
  
```  
q_pp -> a q_ip | b q_pi  
q_pi -> a q_ii | b q_pp | ⊣  
q_ip -> a q_pp | b q_ii  
q_ii -> a q_pi | b q_ip  
```  
  
---  
  
### 4) Não contém **bb**  
  
Alfabeto: `{a,b}`  
Estados: q0 (sem b anterior), q1 (último foi b), qx (poço/rejeição). q0 e q1 são aceitantes.  
  
```  
q0 -> a q0 | b q1 | ⊣  
q1 -> a q0 | b qx | ⊣  
qx -> a qx | b qx  
```  
  
---  
  
### 5) Cada `0` é seguido imediatamente por `11`  
  
Alfabeto: `{0,1}`  
Estados: q0 (normal, aceitante), q1 (após 0, falta 1), q2 (após 01, falta 1), qx (poço).  
  
```  
q0 -> 1 q0 | 0 q1 | ⊣  
q1 -> 1 q2  
q2 -> 1 q0  
qx -> 0 qx | 1 qx  
```  
  
---  
  
### 6) O 3º símbolo (posição 3) é `a` (|w| ≥ 3)  
  
Alfabeto: `{a,b}`  
Estados: q0 (antes 1º), q1 (antes 2º), q2 (antes 3º), q_ok (aceita), q_rej (poço). q_ok é aceitante.  
  
```  
q0 -> a q1 | b q1  
q1 -> a q2 | b q2  
q2 -> a q_ok | b q_rej  
q_ok -> a q_ok | b q_ok | ⊣  
q_rej -> a q_rej | b q_rej  
```  
  
---  
  
### 7) Começa e termina com o mesmo símbolo (strings não vazias)  
  
Alfabeto: `{a,b}`  
Estados: q0 (antes do 1º símbolo), qA (primeiro=a, último=a; aceitante), qA_b (primeiro=a, último=b), qB (primeiro=b, último=b; aceitante), qB_a (primeiro=b, último=a).  
  
```  
q0 -> a qA | b qB  
qA -> a qA | b qA_b | ⊣  
qA_b -> a qA | b qA_b  
qB -> b qB | a qB_a | ⊣  
qB_a -> b qB | a qB_a  
```  
  
---  
  
### 8) Exatamente duas ocorrências de `ab` (podem sobrepor)  
  
Alfabeto: `{a,b}`  
Estados (contagem + memória do último símbolo): s0 (0 ab, último ≠ a), s1 (0 ab, último = a), s2 (1 ab, último ≠ a), s3 (1 ab, último = a), s4 (2 ab — aceitante), sX (>2 poço).  
  
```  
s0 -> a s1 | b s0  
s1 -> a s1 | b s2  
s2 -> a s3 | b s2  
s3 -> a s1 | b s4  
s4 -> a s4 | b s4 | ⊣  
sX -> a sX | b sX  
```  
  
(Leitura: a seguido de b incrementa contador conforme as transições acima; s4 é aceitação quando já viu exatamente 2 ocorrências.)  
  
---  
  
### 9) (AFN) Termina em `abc` **ou** `bca` — (gramática geradora de sufixos)  
  
Alfabeto: `{a,b,c}`  
Gramática com produção de consumo de prefixo e duas trilhas para os sufixos:  
  
```  
S -> a S | b S | c S | a A1 | b B1  
A1 -> b A2  
A2 -> c F  
B1 -> c B2  
B2 -> a F  
F -> ⊣ | a F | b F | c F  
```  
  
(`F` é estado aceitante que permite quaisquer símbolos depois do sufixo — aqui usamos `F -> ⊣` e também loops para permitir emissão do marcador; a forma acima gera palavras cujo final contém `abc` ou `bca`.)  
  
---  
  
### 10) (AFN) Ao menos três ocorrências de `ab` (possivelmente sobrepostas)  
  
Alfabeto: `{a,b}`  
Estratégia: reconhecer a primeira `ab`, depois a segunda, depois a terceira; entre elas permite qualquer prefixo. Produções:  
  
```  
S -> a S | b S | a A1  
A1 -> b S1  
S1 -> a S1 | b S1 | a A2  
A2 -> b S2  
S2 -> a S2 | b S2 | a A3  
A3 -> b F  
F -> ⊣ | a F | b F  
```  
  
(A leitura leva S → ... → A1 ao achar um `a` que inicia a primeira `ab`, etc.; `F` é aceitante após a 3ª ocorrência.)  
  
---  
  
### 11) Entre dois `1` sempre um número par de `0`  
  
Alfabeto: `{0,1}`  
Estados: S (antes de ver um `1` ou em região sem paridade restrita — aceitante), P_even (acaba de ver `1` ou estamos com paridade par entre últimas `1`s — aceitante), P_odd (paridade ímpar — rejeita ao ver `1`), DEAD (violação).  
  
```  
S -> 0 S | 1 P_even | ⊣  
P_even -> 0 P_odd | 1 P_even | ⊣  
P_odd -> 0 P_even | 1 DEAD  
DEAD -> 0 DEAD | 1 DEAD  
```  
  
(Quando um `1` aparece em `P_odd` ocorre violação; aceitam-se S e P_even.)  
  
---  
  
### 12) (AFNe → AFN) Contém `aba` como subpalavra (ou seja, “aba” aparece em algum lugar)  
  
Alfabeto: `{a,b}`  
Gramática (procura por `aba` em qualquer posição; `F` aceitante que permite o resto):  
  
```  
S -> a S | b S | a A1  
A1 -> b A2  
A2 -> a F  
F -> ⊣ | a F | b F  
```  
  
(Produção `S -> a S | b S` consome prefixo; `a A1 -> b A2 -> a F` garante ocorrência de `aba` e `F` aceita.)  
  
---  
  
  
