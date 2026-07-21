# Compilador de Linguagem de Definição de Formas Geométricas  
  
## 📋 Descrição  
  
Este é um compilador completo para uma DSL (Domain Specific Language) que permite definir formas geométricas com propriedades como cor, posição e dimensões. O compilador foi desenvolvido utilizando:  
  
- **JFlex**: para análise léxica  
- **JCup**: para análise sintática  
- **Java**: para as classes auxiliares e execução  
  
## 🎯 Características  
  
### Formas Suportadas  
- **Círculo**: requer propriedade `raio`  
- **Retângulo**: requer `largura` e `altura`  
- **Triângulo**: requer `base` e `altura`  
- **Quadrado**: requer `lado`  
- **Elipse**: requer `largura` e `altura`  
  
### Propriedades Disponíveis  
- `cor`: string ou identificador (ex: "vermelho", azul)  
- `posicao`: tupla (x, y) com coordenadas numéricas  
- `raio`: número decimal  
- `largura`: número decimal  
- `altura`: número decimal  
- `lado`: número decimal  
- `base`: número decimal  
  
## 🔍 Tratamento de Erros  
  
O compilador identifica e reporta os seguintes tipos de erros com **linha e coluna**:  
  
### 1. Erros Léxicos  
- Caracteres inválidos na entrada  
- Exemplo: caracteres especiais não reconhecidos  
  
### 2. Erros Sintáticos  
- Tokens inesperados  
- Estrutura inválida de formas  
- Propriedades mal formatadas  
- Falta de delimitadores (chaves, parênteses, ponto e vírgula)  
  
### 3. Erros Semânticos  
- Círculo sem propriedade `raio`  
- Retângulo sem `largura` ou `altura`  
- Triângulo sem `base` ou `altura`  
- Quadrado sem `lado`  
- Elipse sem `largura` ou `altura`  
  
## 📝 Sintaxe da Linguagem  
  
### Estrutura Básica  
  
```  
<tipo_forma> <identificador> {  
    <propriedade>: <valor>;  
    <propriedade>: <valor>;  
    ...  
}  
```  
  
### Exemplos de Código Válido  
  
```  
circulo meuCirculo {  
    cor: "vermelho";  
    posicao: (10, 20);  
    raio: 5.5;  
}  
  
retangulo minhaJanela {  
    cor: azul;  
    posicao: (0, 0);  
    largura: 100;  
    altura: 50;  
}  
  
triangulo meuTriangulo {  
    cor: "verde";  
    base: 10;  
    altura: 15;  
    posicao: (5, 5);  
}  
  
quadrado meuQuadrado {  
    lado: 25;  
    cor: amarelo;  
}  
  
elipse minhaElipse {  
    largura: 30;  
    altura: 20;  
    cor: "roxo";  
}  
```  
  
### Exemplos de Erros  
  
#### Erro 1: Círculo sem raio  
```  
circulo circuloErrado {  
    cor: "verde";  
    posicao: (5, 5);  
}  
// Erro: Círculo 'circuloErrado' deve ter propriedade 'raio'  
```  
  
#### Erro 2: Falta de ponto e vírgula  
```  
quadrado meuQuadrado {  
    lado: 10  
    cor: "amarelo";  
}  
// Erro sintático: esperado ';' na linha X, coluna Y  
```  
  
#### Erro 3: Posição mal formatada  
```  
triangulo trianguloErrado {  
    base: 10;  
    altura: 15;  
    posicao: (10 20);  
}  
// Erro: Posição deve ter formato (x, y)  
```  
  
#### Erro 4: Caractere inválido  
```  
circulo teste {  
    raio: 5@5;  
}  
// Erro léxico na linha X, coluna Y: caractere inválido '@'  
```  
  
## 🚀 Como Usar  
  
### Pré-requisitos  
  
1. **JFlex** (última versão)  
2. **JCup** (versão 11b ou superior)  
3. **JDK** 11 ou superior  
  
### Instalação  
  
1. Baixe os arquivos necessários:  
   - `jflex.jar`  
   - `java-cup-11b.jar`  
   - `java-cup-11b-runtime.jar`  
  
2. Coloque todos os arquivos do compilador no mesmo diretório  
  
### Compilação  
  
No Linux/Mac:  
```bash  
chmod +x build.sh  
./build.sh  
```  
  
No Windows (ou manualmente):  
```bash  
# Gerar o lexer  
jflex Lexer.flex  
  
# Gerar o parser  
java -jar java-cup-11b.jar -parser parser Parser.cup  
  
# Compilar  
javac -cp .;java-cup-11b-runtime.jar *.java  
```  
  
### Execução  
  
```bash  
java -cp .:java-cup-11b-runtime.jar Main  
```  
  
(No Windows use `;` ao invés de `:` no classpath)  
  
## 📂 Estrutura dos Arquivos  
  
```  
.  
├── Lexer.flex              # Especificação do analisador léxico  
├── Parser.cup              # Especificação do analisador sintático  
├── Main.java               # Programa principal com exemplos  
├── Forma.java              # Classes auxiliares (incluídas)  
├── Propriedade.java        # (geradas automaticamente)  
├── Posicao.java            # (geradas automaticamente)  
├── sym.java                # Símbolos do parser (gerado)  
├── build.sh                # Script de compilação  
└── README.md               # Esta documentação  
```  
  
## 🔧 Personalização  
  
### Adicionar Nova Forma  
  
1. No `Lexer.flex`, adicione o token:  
```java  
"pentagono"    { return symbol(sym.PENTAGONO); }  
```  
  
2. No `Parser.cup`, adicione o terminal e a regra:  
```java  
terminal PENTAGONO;  
  
forma ::= PENTAGONO:t ID:nome LBRACE propriedades:props RBRACE  
    {:  
        Forma f = new Forma("pentagono", nome, props, tleft, tright);  
        // Adicionar validação específica  
        RESULT = f;  
    :}  
;  
```  
  
### Adicionar Nova Propriedade  
  
1. No `Lexer.flex`:  
```java  
"espessura"    { return symbol(sym.ESPESSURA); }  
```  
  
2. No `Parser.cup`:  
```java  
terminal ESPESSURA;  
  
propriedade ::= ESPESSURA:t COLON NUMBER:n SEMICOLON  
    {:  
        RESULT = new Propriedade("espessura", n, tleft, tright);  
    :}  
;  
```  
  
## 📊 Saída do Compilador  
  
### Compilação Bem-Sucedida  
```  
=== Programa compilado com sucesso! ===  
Total de formas: 2  
  
CIRCULO 'meuCirculo' (linha 1, coluna 1):  
  - cor: vermelho  
  - posicao: (10.0, 20.0)  
  - raio: 5.5  
  
RETANGULO 'minhaJanela' (linha 6, coluna 1):  
  - cor: azul  
  - posicao: (0.0, 0.0)  
  - largura: 100.0  
  - altura: 50.0  
```  
  
### Compilação com Erros  
```  
*** ERROS ENCONTRADOS ***  
  Erro na linha 2, coluna 1: Círculo 'circuloErrado' deve ter propriedade 'raio'  
  Erro sintático na linha 5, coluna 5: token inesperado  
```  
  
## 🧪 Testes  
  
O arquivo `Main.java` inclui 5 exemplos de teste:  
1. Código correto com círculo e retângulo  
2. Círculo sem raio (erro semântico)  
3. Retângulo sem altura (erro semântico)  
4. Quadrado com erro sintático (falta ;)  
5. Triângulo com posição mal formatada (erro sintático)  
  
## 📚 Recursos Adicionais  
  
- [Documentação JFlex](https://jflex.de/manual.html)  
- [Documentação JCup](http://www2.cs.tum.edu/projects/cup/manual.html)  
- [Tutorial de Compiladores](https://www.tutorialspoint.com/compiler_design/index.htm)  
  
## 👨‍💻 Autor Giovane Barcelos  
  
Desenvolvido como exemplo educacional de compilador usando JFlex e JCup.  
  
## 📄 Licença  
  
Este projeto é de código aberto e está disponível para uso educacional.  
