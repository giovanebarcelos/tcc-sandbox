# Processador de Templates Simples  
  
Este projeto implementa um compilador em **JFlex** + **JCup** para uma linguagem mínima de templates com:  
  
- Substituição de variáveis via `{{ variavel }}`  
- Estruturas condicionais `{% if variavel %} ... {% else %} ... {% endif %}` (o bloco `else` é opcional)  
- Texto livre entre construções de template  
  
## Léxico  
  
| Token        | Lexema                              | Observações |  
|--------------|-------------------------------------|-------------|  
| `VAR_OPEN`   | `{{`                                | Início de substituição |  
| `VAR_CLOSE`  | `}}`                                | Fim de substituição |  
| `CTL_OPEN`   | `{%`                                | Início de bloco de controle |  
| `CTL_CLOSE`  | `%}`                                | Fim de bloco de controle |  
| `IF`         | `if` (dentro de bloco de controle)  | Palavras-chave |  
| `ELSE`       | `else`                              | Opcional |  
| `ENDIF`      | `endif`                             | Finaliza bloco |  
| `IDENT`      | `[a-zA-Z_][a-zA-Z0-9_]*`            | Nome de variável |  
| `TEXT`       | Sequência de caracteres fora de tags| Inclui espaços e quebras de linha |  
  
Espaços e quebras de linha dentro de blocos são ignorados quando permitido.  
  
## Gramática (simplificada)  
  
```  
Template       ::= Sections  
Sections       ::= Sections Section | Section  
Section        ::= TEXT  
                 | VAR_OPEN IDENT VAR_CLOSE  
                 | CTL_OPEN IF IDENT CTL_CLOSE Sections ElseOpt CTL_OPEN ENDIF CTL_CLOSE  
ElseOpt        ::= CTL_OPEN ELSE CTL_CLOSE Sections | /* vazio */  
```  
  
A gramática é recursiva, permitindo blocos condicionais aninhados.  
  
## Saída  
  
O parser constrói uma AST simples (nós `TextNode`, `VariableNode`, `IfNode`) e imprime uma representação formatada. Em caso de erro léxico ou sintático, o compilador aborta informando **linha**, **coluna** e **token** que causou a falha.  
  
## Estrutura esperada  
  
- `TemplateLexer.flex` — especificação JFlex  
- `TemplateParser.cup` — gramática JCup  
- `nodes/` — classes Java para nós da AST  
- `TemplateCompiler.java` — ponto de entrada para compilar strings/arquivos  
- `build.sh` e `run_tests.sh`  
- `templates/valid1.tpl`, `templates/valid2.tpl`, `templates/error.tpl`  
  
## Execução (após build)  
  
```  
./build.sh  
./run_tests.sh  
```  
  
`run_tests.sh` compila os exemplos e demonstra o relatório de erro no arquivo inválido.  
