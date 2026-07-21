# Compilador para Linguagem de Definição de Autômatos Finitos  
  
## 📋 Descrição  
  
Este projeto implementa um compilador completo para uma DSL (Domain Specific Language) que permite definir Autômatos Finitos Determinísticos (DFA). O compilador foi desenvolvido utilizando **JFlex** para análise léxica e **JCup** para análise sintática.  
  
## 🎯 Estrutura da Linguagem  
  
### Sintaxe  
  
```  
automaton <nome> {  
    states: <lista_de_estados>;  
    alphabet: <lista_de_símbolos>;  
    initial: <estado_inicial>;  
    final: <estados_finais>;  
    transitions: {  
        <estado>, <símbolo> -> <estado_destino>;  
        ...  
    }  
} end  
```  
  
### Exemplo Válido  
  
```  
automaton EndWithA {  
    states: q0, q1;  
    alphabet: a, b;  
    initial: q0;  
    final: q1;  
    transitions: {  
        q0, a -> q1;  
        q0, b -> q0;  
        q1, a -> q1;  
        q1, b -> q0;  
    }  
} end  
```  
  
## 🏗️ Arquitetura  
  
### Componentes  
  
1. **Lexer.flex** - Analisador Léxico (JFlex)  
   - Reconhece tokens da linguagem  
   - Identifica palavras-chave, identificadores e símbolos  
   - Rastreia linha e coluna de cada token  
  
2. **Parser.cup** - Analisador Sintático (JCup)  
   - Define a gramática da linguagem  
   - Valida a estrutura sintática  
   - Gera erros detalhados com linha e coluna  
  
3. **Automaton.java** - Modelo de Dados  
   - Representa o autômato finito  
   - Armazena estados, alfabeto, transições  
  
4. **Main.java** - Programa Principal  
   - Executa os testes  
   - Demonstra casos de sucesso e erro  
  
## 🔧 Requisitos  
  
- **Java JDK** 11 ou superior  
- **JFlex** 1.8+  
- **JCup** 11b  
- **JCup Runtime** 11b  
  
## 📦 Instalação das Dependências  
  
### Ubuntu/Debian  
```bash  
sudo apt-get update  
sudo apt-get install jflex cup  
```  
  
### Manual  
```bash  
# Baixar JFlex  
wget https://jflex.de/release/jflex-1.8.2.tar.gz  
tar -xzf jflex-1.8.2.tar.gz  
  
# Baixar JCup  
wget http://www2.cs.tum.edu/projects/cup/releases/java-cup-bin-11b-20160615.tar.gz  
tar -xzf java-cup-bin-11b-20160615.tar.gz  
```  
  
## 🚀 Compilação e Execução  
  
### Passo 1: Compilar o projeto  
  
#### Linux/Mac  
```bash  
chmod +x build.sh  
./build.sh  
```  
  
#### Windows  
```cmd  
# 1. Gerar o Lexer  
jflex -d . Lexer.flex  
  
# 2. Gerar o Parser  
java -jar java-cup-11b.jar -parser parser -symbols sym Parser.cup  
  
# 3. Compilar  
mkdir build  
javac -cp .;java-cup-11b-runtime.jar *.java -d build/  
```  
  
### Passo 2: Executar os testes  
  
#### Opção 1: Testes integrados (Main.java)  
```bash  
# Linux/Mac  
java -cp build:java-cup-11b-runtime.jar Main  
  
# Windows  
java -cp build;java-cup-11b-runtime.jar Main  
```  
  
#### Opção 2: Testes a partir de arquivos (TestRunner.java)  
```bash  
# Linux/Mac  
chmod +x run_tests.sh  
./run_tests.sh  
  
# Ou manualmente:  
java -cp build:java-cup-11b-runtime.jar TestRunner  
  
# Windows  
java -cp build;java-cup-11b-runtime.jar TestRunner  
```  
  
## ✅ Testes Incluídos  
  
O projeto inclui **duas formas de executar os testes**:  
  
### Forma 1: Testes Integrados (Main.java)  
Os testes estão embutidos no código e executam automaticamente.  
  
### Forma 2: Testes por Arquivo (TestRunner.java + arquivos .dfa)  
Os testes são lidos de arquivos externos:  
- **test1.dfa** - Teste de sucesso  
- **test2.dfa** - Teste de sucesso  
- **test3.dfa** - Teste com erro  
  
---  
  
### Teste 1: ✔️ Sucesso - Autômato que aceita palavras terminadas em 'a'  
  
**Arquivo: test1.dfa**  
```  
automaton EndWithA {  
    states: q0, q1;  
    alphabet: a, b;  
    initial: q0;  
    final: q1;  
    transitions: {  
        q0, a -> q1;  
        q0, b -> q0;  
        q1, a -> q1;  
        q1, b -> q0;  
    }  
} end  
```  
  
### Teste 2: ✔️ Sucesso - Autômato que aceita número par de 'a's  
  
**Arquivo: test2.dfa**  
```  
automaton EvenAs {  
    states: par, impar;  
    alphabet: a, b;  
    initial: par;  
    final: par;  
    transitions: {  
        par, a -> impar;  
        par, b -> par;  
        impar, a -> par;  
        impar, b -> impar;  
    }  
} end  
```  
  
### Teste 3: ❌ Erro - Falta ponto e vírgula  
  
**Arquivo: test3.dfa**  
```  
automaton ErrorTest {  
    states: q0, q1    // ❌ ERRO: Falta ';'  
    alphabet: a, b;  
    initial: q0;  
    final: q1;  
    transitions: {  
        q0, a -> q1;  
    }  
} end  
```  
  
**Saída esperada:**  
```  
❌ ERRO DE COMPILAÇÃO:  
Erro sintático na linha 2, coluna 5: token inesperado 'alphabet'  
Tipo: Erro Sintático  
```  
  
## 📊 Saída do Compilador  
  
### Caso de Sucesso  
```  
=== Autômato: EndWithA ===  
Estados: [q0, q1]  
Alfabeto: [a, b]  
Estado Inicial: q0  
Estados Finais: [q1]  
Transições:  
  δ(q0, a) = q1  
  δ(q0, b) = q0  
  δ(q1, a) = q1  
  δ(q1, b) = q0  
  
Autômato 'EndWithA' compilado com sucesso!  
```  
  
### Caso de Erro  
```  
❌ ERRO DE COMPILAÇÃO:  
Erro sintático na linha X, coluna Y: token inesperado '<token>'  
Tipo: Erro Sintático/Léxico  
```  
  
## 🎓 Conceitos Implementados  
  
- ✅ Análise Léxica com JFlex  
- ✅ Análise Sintática com JCup  
- ✅ Tratamento de erros com linha e coluna  
- ✅ Estruturas de dados para autômatos  
- ✅ Testes de validação (2 sucessos, 1 erro)  
- ✅ Mensagens de erro detalhadas  
  
## 📝 Tokens Reconhecidos  
  
| Token | Descrição |  
|-------|-----------|  
| `automaton` | Palavra-chave para iniciar definição |  
| `states` | Declaração de estados |  
| `alphabet` | Declaração do alfabeto |  
| `initial` | Estado inicial |  
| `final` | Estados finais |  
| `transitions` | Declaração de transições |  
| `end` | Fim da definição |  
| `{`, `}` | Delimitadores de bloco |  
| `,` | Separador |  
| `:` | Operador de atribuição |  
| `->` | Seta de transição |  
| `;` | Fim de declaração |  
| IDENTIFIER | Identificador (estados) |  
| SYMBOL | Símbolo do alfabeto |  
  
## 🐛 Tratamento de Erros  
  
O compilador detecta e reporta:  
  
1. **Erros Léxicos**: Caracteres inválidos  
   - Reporta linha, coluna e caractere inválido  
  
2. **Erros Sintáticos**: Estrutura incorreta  
   - Reporta linha, coluna e token inesperado  
   - Indica onde a sintaxe foi violada  
  
## 👨‍💻 Autor Giovane Barcelos  
  
Projeto desenvolvido para demonstração de compiladores com JFlex e JCup.  
  
## 📄 Licença  
  
Este projeto é livre para uso educacional.  
