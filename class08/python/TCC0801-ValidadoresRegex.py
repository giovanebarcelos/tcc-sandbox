#!/usr/bin/env python3
"""TCC0801-ValidadoresRegex.py | Aula 08
Validadores com expressões regulares: CPF, e-mail, URL.
"""
import re

# CPF: xxx.xxx.xxx-xx
CPF = r'^\d{3}\.\d{3}\.\d{3}-\d{2}$'

# E-mail simplificado
EMAIL = r'^[\w.+-]+@[\w-]+\.[\w.]+$'

# URL simplificada
URL = r'^https?://[\w.-]+(?:\.[\w.-]+)+(?:/[\w./-]*)?$'

# Identificador Java/Python
IDENTIFICADOR = r'^[a-zA-Z_][a-zA-Z0-9_]*$'

# Número inteiro (com ou sem sinal)
INTEIRO = r'^[+-]?\d+$'


def validar(nome, pattern, exemplos_validos, exemplos_invalidos):
    print(f"\n=== {nome} ===")
    print(f"Pattern: {pattern}")
    for ex in exemplos_validos:
        ok = bool(re.fullmatch(pattern, ex))
        print(f"  {'✓' if ok else '✗'} {ex}")
    for ex in exemplos_invalidos:
        ok = bool(re.fullmatch(pattern, ex))
        print(f"  {'✓' if not ok else '✗'} {ex} (inválido)")


if __name__ == "__main__":
    validar("CPF", CPF,
            ["123.456.789-00", "000.000.000-00"],
            ["123.456.789-0", "12345678900", "12.345.678-900"])
    validar("E-mail", EMAIL,
            ["user@example.com", "a.b+c@domain.co"],
            ["user@", "@domain.com", "user@domain", ""])
    validar("Identificador", IDENTIFICADOR,
            ["x", "x1", "_total", "MAX_VALUE"],
            ["1x", "x-y", "", "class"])
    validar("Inteiro", INTEIRO,
            ["0", "42", "-7", "+100"],
            ["", "3.14", "1_000", "abc"])
