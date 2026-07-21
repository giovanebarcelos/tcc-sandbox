📌 Organização das tarefas:  
  
- Do exercício **1 ao 5** → iniciantes (uso básico de âncoras e classes).  
  
- Do **6 ao 10** → prática com quantificadores e condições.  
  
- Do **11 ao 15** → cenários do mundo real.  
  
- Do **16 ao 20** → regex avançadas, boas para projetos e discussões.  
  
---  
  
# 📘 Lista de Exercícios de Expressões Regulares  
  
---  
  
## 🔹 Grupo 1 – Correspondência simples (fáceis)  
  
**1. Verificar se uma string contém apenas dígitos.**  
Regex:  
  
```  
^\d+$  
```  
  
Exemplos:  
  
- `12345` → válido  
  
- `123a` → inválido  
  
---  
  
**2. Verificar se uma string contém apenas letras maiúsculas.**  
Regex:  
  
```  
^[A-Z]+$  
```  
  
Exemplos:  
  
- `HELLO` → válido  
  
- `Hello` → inválido  
  
---  
  
**3. Encontrar todas as ocorrências da palavra "data" em um texto (case insensitive).**  
Regex:  
  
```  
(?i)data  
```  
  
Exemplo:  
  
- Texto: `Database and Big Data` → corresponde a `Data`, `data`  
  
---  
  
**4. Identificar strings que começam com "cat".**  
Regex:  
  
```  
^cat  
```  
  
Exemplos:  
  
- `catdog` → válido  
  
- `dogcat` → inválido  
  
---  
  
**5. Verificar se uma string termina com `.txt`.**  
Regex:  
  
```  
\.txt$  
```  
  
Exemplos:  
  
- `document.txt` → válido  
  
- `document.pdf` → inválido  
  
---  
  
## 🔹 Grupo 2 – Classes e quantificadores  
  
**6. Validar um número de telefone simples no formato `9999-9999`.**  
Regex:  
  
```  
^\d{4}-\d{4}$  
```  
  
Exemplos:  
  
- `1234-5678` → válido  
  
- `12345-678` → inválido  
  
---  
  
**7. Encontrar todas as palavras que começam com vogal.**  
Regex:  
  
```  
\b[aeiouAEIOU]\w*  
```  
  
Exemplo:  
  
- Texto: `Apple orange banana` → corresponde a `Apple`, `orange`  
  
---  
  
**8. Identificar números decimais com duas casas após a vírgula.**  
Regex:  
  
```  
^\d+\.\d{2}$  
```  
  
Exemplos:  
  
- `12.34` → válido  
  
- `12.3` → inválido  
  
---  
  
**9. Validar uma senha com pelo menos 8 caracteres, contendo letras e números.**  
Regex:  
  
```  
^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$  
```  
  
Exemplo:  
  
- `abc12345` → válido  
  
- `abcdefg` → inválido  
  
---  
  
**10. Encontrar todas as palavras com exatamente 5 letras.**  
Regex:  
  
```  
\b\w{5}\b  
```  
  
Exemplo:  
  
- Texto: `apple house car` → corresponde a `apple`, `house`  
  
---  
  
## 🔹 Grupo 3 – Estruturas comuns  
  
**11. Validar um endereço de e-mail.**  
Regex:  
  
```  
^[\w._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$  
```  
  
Exemplos:  
  
- `user@example.com` → válido  
  
- `user@.com` → inválido  
  
---  
  
**12. Validar um endereço IPv4.**  
Regex:  
  
```  
^(\d{1,3}\.){3}\d{1,3}$  
```  
  
Exemplo:  
  
- `192.168.0.1` → válido  
  
- `999.999.999.999` → (aceito pela regex mas inválido logicamente → discutir em sala)  
  
---  
  
**13. Extrair todas as hashtags de um texto.**  
Regex:  
  
```  
#\w+  
```  
  
Exemplo:  
  
- Texto: `#ciência #computação #regex` → corresponde a `#ciência`, `#computação`, `#regex`  
  
---  
  
**14. Validar um CPF no formato `999.999.999-99`.**  
Regex:  
  
```  
^\d{3}\.\d{3}\.\d{3}-\d{2}$  
```  
  
Exemplo:  
  
- `123.456.789-00` → válido  
  
- `12345678900` → inválido  
  
---  
  
**15. Identificar todas as palavras repetidas lado a lado.**  
Regex:  
  
```  
\b(\w+)\s+\1\b  
```  
  
Exemplo:  
  
- Texto: `ele falou falou e saiu` → corresponde a `falou falou`  
  
---  
  
## 🔹 Grupo 4 – Casos avançados  
  
**16. Validar uma data no formato `DD/MM/AAAA`.**  
Regex:  
  
```  
^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\d{4}$  
```  
  
Exemplos:  
  
- `31/12/2024` → válido  
  
- `32/13/2024` → inválido  
  
---  
  
**17. Validar placas de carro no padrão Mercosul (ex: `ABC1D23`).**  
Regex:  
  
```  
^[A-Z]{3}\d[A-Z]\d{2}$  
```  
  
Exemplo:  
  
- `BRA2E19` → válido  
  
- `ABC1234` → inválido  
  
---  
  
**18. Extrair o conteúdo entre aspas em um texto.**  
Regex:  
  
```  
"([^"]*)"  
```  
  
Exemplo:  
  
- Texto: `Ele disse "olá mundo" ontem` → corresponde a `olá mundo`  
  
---  
  
**19. Validar URLs com http ou https.**  
Regex:  
  
```  
^https?:\/\/[A-Za-z0-9.-]+\.[A-Za-z]{2,}([\/\w.-]*)*\/?$  
```  
  
Exemplos:  
  
- `https://example.com` → válido  
  
- `ftp://example.com` → inválido  
  
---  
  
**20. Encontrar todas as tags HTML em um texto.**  
Regex:  
  
```  
<[^>]+>  
```  
  
Exemplo:  
  
- Texto: `<p>Olá</p><br>` → corresponde a `<p>`, `</p>`, `<br>`  
  
---  
  
  
