#!/usr/bin/env python3
"""TCC1301-ParadaExperimento.py | Aula 13
Experimento didático do Problema da Parada.
Demonstra por contradição que não existe um decisor universal `vai_parar(prog, entrada)`.
"""
from typing import Callable


def para_em_ate(prog: Callable[[str], None], entrada: str, max_passos: int = 1000) -> bool:
    """Simula `prog(entrada)` por até max_passos. Retorna True se parou, False se (provavelmente) loopou."""
    # Não é possível de fato isolar; aqui usamos timeout simulado via contador.
    # Em sala: explicar que um decisor real precisaria ser TOTAL — o que é impossível.
    import signal

    class TimeoutError(Exception):
        pass

    def handler(signum, frame):
        raise TimeoutError()

    try:
        signal.signal(signal.SIGALRM, handler)
        signal.setitimer(signal.ITIMER_REAL, max_passos / 1000.0)
        prog(entrada)
        signal.alarm(0)
        return True
    except TimeoutError:
        return False
    except Exception:
        return True  # parou (com erro, mas parou)


def loop_infinito(entrada: str) -> None:
    """Programa que sempre entra em loop."""
    while True:
        pass


def para_se_nao_vai_parar(entrada: str) -> None:
    """Programa paradoxal D: se `vai_parar(D, D)` então loop; senão para."""
    # Simulação: assumimos que `vai_parar` diria algo — aqui só ilustramos o paradoxo.
    # Se o decisor dissesse que D para, D loopa; se dissesse que D loopa, D para.
    while True:
        pass


def programa_ok(entrada: str) -> None:
    """Programa que sempre para."""
    print(f"  programa_ok executou com entrada='{entrada}' e parou.")


def demonstrar_paradoxo() -> None:
    """Demonstra o paradoxo do problema da parada (argumento de Turing)."""
    print("=== Argumento do Problema da Parada (Turing, 1936) ===\n")
    print("Suponha que exista um decisor universal H(prog, entrada):")
    print("  H(P, x) = True  se P(x) para")
    print("  H(P, x) = False se P(x) loopa\n")
    print("Construa o programa paradoxal D(x):")
    print("  se H(x, x) == True:  loop infinito")
    print("  senão:               para\n")
    print("Agora pergunte: H(D, D) = ?")
    print("  Se H(D,D)=True  → D(D) loopa  → H(D,D) deveria ser False. Contradição!")
    print("  Se H(D,D)=False → D(D) para    → H(D,D) deveria ser True.  Contradição!")
    print("\n∴ Não existe decisor universal H. O problema da parada é INDECIDÍVEL.\n")


if __name__ == "__main__":
    print("=== Teste: programa_ok (sempre para) ===")
    ok = para_em_ate(programa_ok, "teste", max_passos=50)
    print(f"  Parou? {ok}\n")

    print("=== Teste: loop_infinito (nunca para) ===")
    ok = para_em_ate(loop_infinito, "x", max_passos=50)
    print(f"  Parou? {ok} (esperado: False — loop detectado por timeout)\n")

    demonstrar_paradoxo()

    print("=== Classes de complexidade (noção) ===")
    print("  P  : problemas resolúveis em tempo polinomial (decidíveis)")
    print("  NP : soluções verificáveis em tempo polinomial")
    print("  Decidível : existe MT que sempre para com SIM ou NÃO")
    print("  Indecidível : não existe tal MT (ex.: problema da parada)")
