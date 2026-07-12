#!/usr/bin/env python3
"""TCC0301-ClassificadorGramatica.py | Aula 03
Classifica uma gramática formal segundo a Hierarquia de Chomsky (tipos 0-3).

Estratégia: começa assumindo tipo 3 (mais restrito) e "degrada" o tipo
conforme encontra violações.
"""


def classificar(producoes: dict, V: set) -> int:
    """Retorna o tipo de Chomsky (3, 2, 1 ou 0) da gramática.

    Args:
        producoes: dict mapeando lhs (str) -> lista de rhs (list[str]).
        V: conjunto de variáveis (não-terminais).
    """
    tipo = 3  # começa otimista

    for lhs, rhs_list in producoes.items():
        for rhs in rhs_list:
            # --- tipo 2+: lado esquerdo deve ser UMA única variável ---
            if lhs not in V or len(lhs) != 1:
                tipo = min(tipo, 1)  # cai para tipo 1 ou 0
                # tipo 1: |alpha| <= |beta| ?
                if len(lhs) > len(rhs):
                    tipo = 0
                continue  # já não pode ser 3 nem 2

            # --- tipo 3: linear à direita ou à esquerda ---
            var_count = sum(1 for c in rhs if c in V)
            if var_count > 1:
                tipo = min(tipo, 2)  # 2+ variáveis no rhs -> não é tipo 3
            elif var_count == 1:
                # exatamente uma variável: verificar se está na borda
                var_pos = next(i for i, c in enumerate(rhs) if c in V)
                tem_terminal_no_meio = any(
                    c not in V and i > var_pos and any(c2 in V for c2 in rhs[i + 1:])
                    for i, c in enumerate(rhs)
                )
                # simplificação: se a variável não está na última posição
                # (linear à direita) nem na primeira (linear à esquerda)
                # com os demais símbolos sendo terminais, não é tipo 3
                direita = var_pos == len(rhs) - 1  # A -> ...X (X último)
                esquerda = var_pos == 0  # A -> X... (X primeiro)
                if not direita and not esquerda:
                    tipo = min(tipo, 2)
                # linear à esquerda: rhs deve ser Var Terminal* (simplificado)
                if esquerda and len(rhs) > 1:
                    restante = rhs[1:]
                    if any(c in V for c in restante):
                        tipo = min(tipo, 2)

            # se rhs é vazio (ε) e lhs != símbolo inicial, é tipo 1 ou 0
            if rhs == "" or rhs == "ε":
                # permitido para tipo 2/3 se for S (símbolo inicial)
                # mas nossa simplificação: aceitamos ε no tipo 3 se lhs é S
                pass

    return tipo


TIPO_NOME = {
    3: "Tipo 3 — Regular",
    2: "Tipo 2 — Livre de Contexto",
    1: "Tipo 1 — Sensível ao Contexto",
    0: "Tipo 0 — Irrestrita",
}


def testar(nome, producoes, V, esperado):
    tipo = classificar(producoes, V)
    status = "✓" if tipo == esperado else f"✗ (esperado {esperado})"
    print(f"{status} {nome}: {TIPO_NOME[tipo]}")


if __name__ == "__main__":
    V_bin = {"S", "A"}

    # Tipo 3: gramática regular para números binários
    testar("Binários (tipo 3)", {
        "S": ["0S", "1S", "0A", "1A"],
        "A": ["0", "1"],
    }, {"S", "A"}, 3)

    # Tipo 2: GLC para aⁿbⁿ
    testar("aⁿbⁿ (tipo 2)", {
        "S": ["aSb", ""],
    }, {"S"}, 2)

    # Tipo 2: gramática dos naturais (N -> D | DN)
    testar("Naturais N→D|DN (tipo 2)", {
        "N": ["D", "DN"],
        "D": ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9"],
    }, {"N", "D"}, 2)

    # Tipo 2: palíndromos
    testar("Palíndromos (tipo 2)", {
        "S": ["aSa", "bSb", "a", "b", ""],
    }, {"S"}, 2)

    # Tipo 3: linear à esquerda
    testar("Linear à esquerda (tipo 3)", {
        "S": ["Sa", "Sb", "a", "b"],
    }, {"S"}, 3)

    # Tipo 0: irrestrita (|alpha| > |beta|)
    testar("Irrestrita (tipo 0)", {
        "AB": ["C"],
    }, {"A", "B", "C"}, 0)
