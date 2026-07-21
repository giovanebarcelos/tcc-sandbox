# 🚀 Guia Rápido de Uso  
  
## Passo a Passo Completo  
  
### 1️⃣ Compilar o Projeto  
  
```bash  
chmod +x build.sh  
./build.sh  
```  
  
Você deve ver:  
```  
✅ Build concluído com sucesso!  
```  
  
---  
  
### 2️⃣ Executar os Testes  
  
#### Opção A: Testes Embutidos (mais rápido)  
```bash  
java -cp build:java-cup-11b-runtime.jar Main  
```  
  
#### Opção B: Testes a partir de Arquivos  
```bash  
chmod +x run_tests.sh  
./run_tests.sh  
```  
  
---  
  
## 📋 Estrutura dos Arquivos  
  
```  
.  
├── Lexer.flex              # Especificação do analisador léxico  
├── Parser.cup              # Especificação do analisador sintático  
├── Automaton.java          # Modelo de dados  
├── Main.java               # Testes integrados  
├── TestRunner.java         # Executor de testes por arquivo  
├── test1.dfa               # Teste 1 (sucesso)  
├── test2.dfa               # Teste 2 (sucesso)  
├── test3.dfa               # Teste 3 (erro)  
├── build.sh                # Script de compilação  
├── run_tests.sh            # Script para rodar testes  
└── README.md               # Documentação completa  
```  
  
---  
  
## 🎯 Saída Esperada dos Testes  
  
### ✅ Teste 1 e 2 (Sucesso)  
```  
Autômato 'NomeDoAutomato' compilado com sucesso!  
  
=== Autômato: NomeDoAutomato ===  
Estados: [q0, q1, ...]  
Alfabeto: [a, b, ...]  
Estado Inicial: q0  
Estados Finais: [q1, ...]  
Transições:  
  δ(q0, a) = q1  
  ...  
```  
  
### ❌ Teste 3 (Erro)  
```  
❌ ERRO DE COMPILAÇÃO:  
Erro sintático na linha 2, coluna 19: token inesperado 'alphabet'  
Tipo: Erro Sintático  
```  
  
---  
  
## 🔧 Solução de Problemas  
  
### Erro: "comando não encontrado: jflex"  
```bash  
# Ubuntu/Debian  
sudo apt-get install jflex cup  
  
# Ou baixe manualmente  
wget https://jflex.de/release/jflex-1.8.2.tar.gz  
```  
  
### Erro: "ClassNotFoundException"  
Certifique-se de incluir o runtime do Cup no classpath:  
```bash  
java -cp build:java-cup-11b-runtime.jar Main  
```  
  
### Erro: "arquivo não encontrado"  
Verifique se está no diretório correto e se executou `./build.sh` primeiro.  
  
---  
  
## 📝 Criar Seu Próprio Teste  
  
1. Crie um arquivo `.dfa`:  
```bash  
nano meu_teste.dfa  
```  
  
2. Escreva um autômato:  
```  
automaton MeuAutomato {  
    states: q0, q1;  
    alphabet: a, b;  
    initial: q0;  
    final: q1;  
    transitions: {  
        q0, a -> q1;  
    }  
} end  
```  
  
3. Compile programaticamente:  
```java  
String content = Files.readString(Path.of("meu_teste.dfa"));  
StringReader reader = new StringReader(content);  
Lexer lexer = new Lexer(reader);  
parser parser = new parser(lexer);  
parser.parse();  
```  
  
---  
  
## 💡 Dicas  
  
- ✅ Use identificadores descritivos para estados (q0, q1, start, accept...)  
- ✅ Símbolos do alfabeto devem ser alfanuméricos únicos  
- ✅ Não esqueça o ponto e vírgula após cada declaração  
- ✅ As transições devem seguir o formato: `estado, símbolo -> estado_destino;`  
- ✅ Sempre termine com `} end`  
  
---  
  
## 📞 Precisa de Ajuda?  
  
Consulte o **README.md** para documentação completa ou revise os exemplos em **test1.dfa**, **test2.dfa** e **test3.dfa**.  
