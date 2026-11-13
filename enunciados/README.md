# Enunciados — 31 compiladores para desenvolver

**Teoria da Computação e Compiladores — 6M (Digital) · Prof. Giovane Barcelos**

Cada pasta tem o **enunciado** (`ENUNCIADO.md`) de um compilador e os **casos de teste** (`testes/`).
Os compiladores vão do primeiro scanner (01) até um compilador completo que gera assembly x86-64 (31).

| # | Enunciado | Nível | Aulas |
|---|---|---|---|
| 01 | [Scanner básico](01_scanner_basico/ENUNCIADO.md) | ⭐ | 16 (Scanner manual) e 17 (JFlex/ANTLR) |
| 02 | [Scanner da calculadora](02_scanner_calc/ENUNCIADO.md) | ⭐ | 16 e 17 |
| 03 | [Scanner completo](03_scanner_completo/ENUNCIADO.md) | ⭐⭐ | 16 e 17 |
| 04 | [Calculadora](04_calculadora/ENUNCIADO.md) | ⭐⭐ | 10 (GLC e ambiguidade), 17, 19–21 (LR, JCup) |
| 05 | [Calculadora com variáveis](05_calculadora_variaveis/ENUNCIADO.md) | ⭐⭐ | 21 (JFlex + JCup / ANTLR) e 22 (tabela de símbolos) |
| 06 | [Analisador de JSON](06_json/ENUNCIADO.md) | ⭐⭐ | 17 e 21 |
| 07 | [Linguagem de consulta → SQL](07_consulta_sql/ENUNCIADO.md) | ⭐⭐⭐ | 21 e 22 |
| 08 | [IF/ELSE → código de 3 endereços e C](08_if_else_c/ENUNCIADO.md) | ⭐⭐⭐ | 10 (ambiguidade), 19–20 (shift/reduce) e 25 (geração de código) |
| 09 | [Repetição DO → código de 3 endereços e C](09_repeticao_do_c/ENUNCIADO.md) | ⭐⭐⭐ | 25 |
| 10 | [HTML básico](10_html/ENUNCIADO.md) | ⭐⭐⭐ | 17 e 26 (modos léxicos) |
| 11 | [Formas geométricas → SVG](11_formas_geometricas/ENUNCIADO.md) | ⭐⭐ | 22 (análise semântica) |
| 12 | [Interpretador de comandos de robô](12_robo/ENUNCIADO.md) | ⭐⭐ | 21 |
| 13 | [Marcação estilo BBCode → HTML](13_marcacao_bbcode/ENUNCIADO.md) | ⭐⭐⭐ | 16 e 17 |
| 14 | [Mini-Pascal: análise semântica](14_mini_pascal/ENUNCIADO.md) | ⭐⭐⭐ | 22 (semântica e tabela de símbolos) |
| 15 | [Autômato finito determinístico (AFD) → JFLAP](15_automato_finito/ENUNCIADO.md) | ⭐⭐⭐ | 04–08 (AFD, AFN, minimização) e 22 |
| 16 | [Processador de templates](16_templates/ENUNCIADO.md) | ⭐⭐⭐⭐ | 17 e 26 (modos léxicos) |
| 17 | [Tabela-verdade](17_tabela_verdade/ENUNCIADO.md) | ⭐⭐ | 10 (gramáticas, precedência) |
| 18 | [Expressão regular → AFN de Thompson](18_expressao_regular/ENUNCIADO.md) | ⭐⭐⭐⭐ | 06–08 (AFN-ε, ER) |
| 19 | [FIRST, FOLLOW e tabela LL(1)](19_first_follow/ENUNCIADO.md) | ⭐⭐⭐⭐ | 18 (FIRST, FOLLOW, LL(1)) |
| 20 | [Máquina de Turing](20_maquina_turing/ENUNCIADO.md) | ⭐⭐⭐ | 12 (Máquina de Turing) e 13 |
| 21 | [CSV → tabela, estatísticas e JSON](21_csv/ENUNCIADO.md) | ⭐⭐ | 17 e 18 (produções ε) |
| 22 | [Arquivos INI com interpolação](22_ini_config/ENUNCIADO.md) | ⭐⭐⭐⭐ | 17, 22 e 26 (modos léxicos) |
| 23 | [Infixa → pós-fixa e pré-fixa](23_posfixa/ENUNCIADO.md) | ⭐⭐ | 18–20 (LL × LR) e 25 |
| 24 | [Código de três endereços e máquina virtual](24_codigo_tres_enderecos/ENUNCIADO.md) | ⭐⭐⭐⭐ | 25 (geração de código) |
| 25 | [Otimização da AST](25_otimizacao/ENUNCIADO.md) | ⭐⭐⭐⭐ | 25 (otimização) |
| 26 | [Interpretador da mini-linguagem (PP3)](26_interpretador_pp3/ENUNCIADO.md) | ⭐⭐⭐ | 21 (Projeto Parcial 3) |
| 27 | [Funções, escopo e pilha de chamadas](27_funcoes_escopo/ENUNCIADO.md) | ⭐⭐⭐⭐ | 22 (escopo) e 27 |
| 28 | [Verificação estática de tipos](28_verificacao_tipos/ENUNCIADO.md) | ⭐⭐⭐ | 22 |
| 29 | [Recuperação de erros](29_recuperacao_erros/ENUNCIADO.md) | ⭐⭐⭐ | 24 (controle de erros) |
| 30 | [Mini-compilador Lingo → C](30_mini_compilador_c/ENUNCIADO.md) | ⭐⭐⭐⭐⭐ | 25 (geração de código) e 27 (Projeto Integrador A3) |
| 31 | [Mini-compilador Lingo → assembly x86-64](31_mini_compilador_asm/ENUNCIADO.md) | ⭐⭐⭐⭐⭐ | 25 (geração de código: Slides 12–14) |

Níveis: ⭐ introdutório · ⭐⭐ básico · ⭐⭐⭐ intermediário · ⭐⭐⭐⭐ avançado · ⭐⭐⭐⭐⭐ projeto completo.

---

## Regras gerais (valem para todos os enunciados)

### Ferramentas e linguagem
- Gerador de analisadores: **ANTLR 4** ou **JFlex + JCup** (instalação: Aula 17). Fazer nas duas é desafio extra.
- Linguagem de implementação: **Java 21** (recomendado). Python com o alvo Python do ANTLR ou com PLY também é aceito.
- Os enunciados que geram código C ou assembly (08, 09, 30, 31) precisam do **gcc** (Aula 17, *Pré-requisito 4*).
- Monte uma **AST** e trabalhe sobre ela; não faça toda a lógica dentro das ações semânticas do parser.

### Como o compilador é executado
- O compilador recebe o **caminho do arquivo-fonte** como argumento: `java Main entrada.txt`.
- O resultado vai para a **saída padrão** (stdout), exatamente no formato da *Saída esperada* do enunciado.
- Código de saída **0** quando o programa é aceito e **1** quando há erro.
- Erros vão para a **saída de erro** (stderr), uma mensagem por erro, no formato:

  ```text
  Erro léxico [linha:coluna]: caractere inválido '@'
  Erro sintático [linha:coluna]: token inesperado ';'
  Erro semântico [linha:coluna]: variável 'x' não declarada
  Erro de execução [linha:coluna]: divisão por zero em (10 / (5 - 5))
  ```

  Linha e coluna começam em **1**. O compilador para no **primeiro erro** (exceto no enunciado 29).
- Enunciados com geração de código (08, 09, 30, 31): o compilador grava o arquivo gerado (`.c` ou `.s`) e
  um script seu (ex.: `executar.sh`) compila com o `gcc`, executa o programa e imprime a saída dele.

### Como testar: `testar.sh`

```bash
cd enunciados/04_calculadora
../testar.sh . java -cp "build/classes:$ANTLR_JAR" Main      # ANTLR
../testar.sh . java -cp "build/classes:$CUP_RT" Main          # JFlex + JCup
../testar.sh . ./executar.sh                                  # 08, 09, 30, 31: compila, gera, roda o gcc e executa
```

Para cada `testes/ok_*`, o script executa `<comando> testes/ok_N.ext` e compara a saída com
`testes/ok_N.ext.esperado` (ignorando `\r` e espaços no fim das linhas). Para cada `testes/erro_*`, confere se o
compilador terminou com código diferente de 0 e escreveu na saída de erro, e mostra a mensagem de referência
(`testes/erro_N.ext.erro`) ao lado da sua. No Windows, use o `testar.ps1` (`..\testar.ps1 . java -cp ... Main`),
o Git Bash ou o WSL.

### O que entregar
1. A(s) gramática(s) (`.g4` ou `.flex` + `.cup`) e o código-fonte do compilador.
2. Um `README.md` com: como gerar, compilar, executar e testar; a ferramenta usada; o que foi feito dos desafios extras.
3. Os testes fornecidos **mais pelo menos 2 `ok_` e 2 `erro_` seus**, com as saídas esperadas.
4. Não entregue arquivos gerados (`build/`, `*.class`, código gerado pelo ANTLR/JFlex/JCup).

### Avaliação
Cada enunciado traz a tabela de critérios (léxico 20%, sintático 25%, semântica e back-end 35%, mensagens de erro
10%, testes próprios e README 10%). Passar em todos os testes fornecidos é **necessário, mas não suficiente**: o
compilador também será executado com testes que não estão aqui.

### Uso de IA
Siga a política da Aula 01B: **primeiro à mão, depois o agente**. Você deve ser capaz de explicar cada regra da
gramática e cada decisão do back-end; o código gerado por IA que você não sabe explicar não conta.
