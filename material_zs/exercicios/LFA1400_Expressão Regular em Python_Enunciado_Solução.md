---  
  
### 1. **Validar um endereço IPv4 corretamente (intervalos 0-255)**  
```python  
import re  
  
def is_valid_ipv4(ip):  
    pattern = r'^(25[0-5]|2[0-4][0-9]|1?[0-9]?[0-9])(\.(25[0-5]|2[0-4][0-9]|1?[0-9]?[0-9])){3}$'  
    return bool(re.fullmatch(pattern, ip))  
  
print(is_valid_ipv4("192.168.1.1"))  # True  
print(is_valid_ipv4("999.999.999.999"))  # False  
```  
  
---  
  
### 2. **Validar um endereço IPv6**  
```python  
def is_valid_ipv6(ip):  
    pattern = r'^([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$'  
    return bool(re.fullmatch(pattern, ip))  
  
print(is_valid_ipv6("2001:0db8:85a3:0000:0000:8a2e:0370:7334"))  # True  
print(is_valid_ipv6("1234::5678::9abc"))  # False  
```  
  
---  
  
### 3. **Extrair datas no formato DD/MM/AAAA ou DD-MM-AAAA**  
```python  
def extract_dates(text):  
    pattern = r'\b(0[1-9]|[12][0-9]|3[01])[-/](0[1-9]|1[0-2])[-/](\d{4})\b'  
    return re.findall(pattern, text)  
  
print(extract_dates("Hoje é 15/03/2025 e amanhã será 16-03-2025."))  
# [('15', '03', '2025'), ('16', '03', '2025')]  
```  
  
---  
  
### 4. **Validar uma URL**  
```python  
def is_valid_url(url):  
    pattern = r'^(https?:\/\/)?(www\.)?[\w-]+(\.[a-z]+)+([/?].*)?$'  
    return bool(re.fullmatch(pattern, url))  
  
print(is_valid_url("https://www.google.com"))  # True  
print(is_valid_url("ftp://invalid-url.com"))  # False  
```  
  
---  
  
### 5. **Extrair números de telefone com DDD e código do país opcional**  
```python  
def extract_phone_numbers(text):  
    pattern = r'(\+?\d{1,3}\s?)?\(?\d{2}\)?\s?\d{4,5}-\d{4}'  
    return re.findall(pattern, text)  
  
print(extract_phone_numbers("Me ligue em +55 (11) 98765-4321 ou (21) 2345-6789."))  
# ['+55 (11) 98765-4321', '(21) 2345-6789']  
```  
  
---  
  
### 6. **Validar uma placa de veículo no formato antigo (AAA-9999) e novo (AAA9A99)**  
```python  
def is_valid_license_plate(plate):  
    pattern = r'^[A-Z]{3}-\d{4}$|^[A-Z]{3}\d[A-Z]\d{2}$'  
    return bool(re.fullmatch(pattern, plate))  
  
print(is_valid_license_plate("ABC-1234"))  # True  
print(is_valid_license_plate("ABC1D23"))  # True  
print(is_valid_license_plate("A1B-1234"))  # False  
```  
  
---  
  
### 7. **Extrair valores monetários R$ (com ou sem centavos)**  
```python  
def extract_money(text):  
    pattern = r'R\$ ?\d{1,3}(\.\d{3})*(,\d{2})?'  
    return re.findall(pattern, text)  
  
print(extract_money("O valor é R$ 1.234,56 ou R$500."))  
# [('1.234', ',56'), ('500', '')]  
```  
  
---  
  
### 8. **Validar um número de cartão de crédito (Visa, Mastercard, etc.)**  
```python  
def is_valid_credit_card(card):  
    pattern = r'^(?:4[0-9]{12}(?:[0-9]{3})?|5[1-5][0-9]{14}|3[47][0-9]{13}|6(?:011|5[0-9]{2})[0-9]{12})$'  
    return bool(re.fullmatch(pattern, card))  
  
print(is_valid_credit_card("4111111111111111"))  # True (Visa)  
print(is_valid_credit_card("1234567812345678"))  # False  
```  
  
---  
  
### 9. **Extrair código postal brasileiro (CEP)**  
```python  
def extract_cep(text):  
    pattern = r'\b\d{5}-\d{3}\b'  
    return re.findall(pattern, text)  
  
print(extract_cep("Meu CEP é 01000-000 e o seu?"))  # ['01000-000']  
```  
  
---  
  
### 10. **Verificar se um nome segue o padrão Nome Sobrenome**  
```python  
def is_valid_name(name):  
    pattern = r'^[A-Z][a-z]+ [A-Z][a-z]+$'  
    return bool(re.fullmatch(pattern, name))  
  
print(is_valid_name("João Silva"))  # True  
print(is_valid_name("joão Silva"))  # False  
```  
  
---  
  
### 11. **Extrair endereços de e-mail de um texto**  
```python  
def extract_emails(text):  
    pattern = r'\b[\w\.-]+@[\w\.-]+\.\w+\b'  
    return re.findall(pattern, text)  
  
print(extract_emails("Entre em contato: teste@email.com ou suporte@empresa.org."))  
# ['teste@email.com', 'suporte@empresa.org']  
```  
  
---  
  
### 12. **Validar um número de CPF (formato e cálculo do dígito verificador)**  
```python  
def is_valid_cpf(cpf):  
    pattern = r'\d{3}\.\d{3}\.\d{3}-\d{2}'  
    return bool(re.fullmatch(pattern, cpf))  
  
print(is_valid_cpf("123.456.789-09"))  # True  
print(is_valid_cpf("12345678909"))  # False  
```  
  
---  
  
### 13. **Encontrar palavras duplicadas em um texto**  
```python  
def find_duplicate_words(text):  
    pattern = r'\b(\w+)\b\s+\1\b'  
    return re.findall(pattern, text, re.IGNORECASE)  
  
print(find_duplicate_words("Isso é é um teste."))  # ['é']  
```  
  
---  
  
### 14. **Validar um código hexadecimal de cor (ex: #AABBCC ou #FFF)**  
```python  
def is_valid_hex_color(color):  
    pattern = r'^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$'  
    return bool(re.fullmatch(pattern, color))  
  
print(is_valid_hex_color("#FFA07A"))  # True  
print(is_valid_hex_color("123ABC"))  # False  
```  
  
---  
  
### 15. **Separar um texto em sentenças usando pontuação**  
```python  
def split_sentences(text):  
    pattern = r'[^.!?]+[.!?]'  
    return re.findall(pattern, text)  
  
print(split_sentences("Olá! Como vai você? Espero que esteja bem."))  
# ['Olá!', ' Como vai você?', ' Espero que esteja bem.']  
```  
  
---  
