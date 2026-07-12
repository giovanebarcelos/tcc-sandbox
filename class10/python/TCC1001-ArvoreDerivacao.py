#!/usr/bin/env python3
"""TCC1001-ArvoreDerivacao.py | Aula 10
Gera árvore de derivação de uma GLC e detecta ambiguidade.
Gramática exemplo: E → E+E | E*E | (E) | id
"""
from dataclasses import dataclass, field
from typing import List, Optional


@dataclass
class No:
    simbolo: str
    filhos: List["No"] = field(default_factory=list)

    def eh_folha(self) -> bool:
        return not self.filhos


def imprime_arvore(no: No, prefixo: str = "", eh_ultimo: bool = True) -> None:
    """Imprime a árvore em formato ASCII (estilo tree)."""
    connector = "└── " if eh_ultimo else "├── "
    print(prefixo + connector + no.simbolo)
    novo_prefixo = prefixo + ("    " if eh_ultimo else "│   ")
    for i, filho in enumerate(no.filhos):
        imprime_arvore(filho, novo_prefixo, i == len(no.filhos) - 1)


def mermaid(no: No) -> str:
    """Gera representação Mermaid (flowchart) da árvore."""
    linhas = ["flowchart TD"]
    contador = [0]

    def _id():
        contador[0] += 1
        return f"n{contador[0]}"

    def _walk(no: No, pid: Optional[str]):
        nid = _id()
        rotulo = no.simbolo if no.simbolo else "ε"
        linhas.append(f'    {nid}["{rotulo}"]')
        if pid:
            linhas.append(f"    {pid} --> {nid}")
        for f in no.filhos:
            _walk(f, nid)

    _walk(no, None)
    return "\n".join(linhas)


def arvore_id_mais_id() -> No:
    """Derivação leftmost de id+id: E ⇒ E+E ⇒ id+E ⇒ id+id"""
    return No("E", [
        No("E", [No("id")]),
        No("+"),
        No("E", [No("id")]),
    ])


def arvore_id_vezes_id_mais_id_esq() -> No:
    """id*(id+id) — agrupamento à esquerda: (id*id)+id"""
    return No("E", [
        No("E", [
            No("E", [No("id")]),
            No("*"),
            No("E", [No("id")]),
        ]),
        No("+"),
        No("E", [No("id")]),
    ])


def arvore_id_vezes_id_mais_id_dir() -> No:
    """id*id+id — agrupamento à direita: id*(id+id) — AMBIGUIDADE"""
    return No("E", [
        No("E", [No("id")]),
        No("*"),
        No("E", [
            No("E", [No("id")]),
            No("+"),
            No("E", [No("id")]),
        ]),
    ])


if __name__ == "__main__":
    print("=== Árvore 1: id+id (leftmost) ===")
    imprime_arvore(arvore_id_mais_id())

    print("\n=== Árvore 2: id*id+id — agrupamento à esquerda (id*id)+id ===")
    imprime_arvore(arvore_id_vezes_id_mais_id_esq())

    print("\n=== Árvore 3: id*id+id — agrupamento à direita id*(id+id) ===")
    imprime_arvore(arvore_id_vezes_id_mais_id_dir())

    print("\n>>> AMBIGUIDADE: as árvores 2 e 3 derivam a MESMA palavra 'id*id+id'")
    print(">>> com estruturas diferentes → gramática ambígua!\n")

    print("=== Mermaid da Árvore 1 ===")
    print(mermaid(arvore_id_mais_id()))
