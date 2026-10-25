# AGENTS.md
<Nome do projeto> — <ANTLR 4 e Java 17 | JFlex+JCup e Java 17 | PLY e Python 3.12>.

## Onde está cada coisa
- Autômatos e ER (PP1): jff/ · Gramáticas, AP e MT (PP2): gramaticas/
- Scanner e parser: src/ (arquivo .flex/.g4/.cup ou PLY) · Comando de build: ver README
- Programas de teste: testes/validos/ e testes/invalidos/ · Saídas esperadas: testes/esperado/

## Regras
- Nenhum autômato, gramática, tabela ou prova gerado pelo agente é aceito sem teste com cadeias (aceitas e rejeitadas, incluindo a vazia)
- Mensagens de erro com linha, coluna, encontrado e esperado em todas as fases
- O agente revisa e gera programas de teste; a construção formal e o código das fases são dos alunos
- Nada de dados pessoais; commits curtos; nunca faça push

## Agent skills
(bloco escrito pela skill setup-matt-pocock-skills)
