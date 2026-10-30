#!/usr/bin/env bash
# testar_g4.sh | Aula 21
# Testa a calculadora ANTLR 4 em três versões, com a mesma bateria de testes:
#   - [py]       Python3: TCC2102Calc.g4        (gramática pura + Visitor no driver)
#   - [pyacoes]  Python3: TCC2103CalcAcoesPy.g4 (ações embutidas, sem visitor/listener, roda com pygrun)
#   - [java]     Java:    TCC2103CalcAcoes.g4   (ações embutidas, sem visitor/listener, roda com grun)
#
# Uso:
#   ./testar_g4.sh                 # roda a bateria de testes (válidos e inválidos)
#   ./testar_g4.sh entrada.txt     # testa cada linha do arquivo como uma entrada
#   ./testar_g4.sh -tokens "2+3;"  # mostra os tokens reconhecidos pelo lexer (Python)
#
# O .jar do ANTLR 4.13.1 é procurado em $ANTLR_JAR, $TCC_TOOLS, ~/ferramentas e
# ../../ferramentas (a pasta ferramentas/ deste repositório), nessa ordem.

set -u
cd "$(dirname "$0")"

GRAMMAR=TCC2102Calc
GRAMMAR_JAVA=TCC2103CalcAcoes
GRAMMAR_PYACOES=TCC2103CalcAcoesPy
BUILD=build-g4
BUILD_JAVA=build-g4-java
BUILD_PYACOES=build-g4-pyacoes
VENV=.venv-g4
ANTLR_VERSION=4.13.1

VERDE='\033[32m'; VERMELHO='\033[31m'; AMARELO='\033[33m'; NEGRITO='\033[1m'; RESET='\033[0m'

# --- 0. Ferramenta ANTLR 4.13.1 (gerador + runtime Java) ----------------------
JAR=""
for c in "${ANTLR_JAR:-}" "${TCC_TOOLS:-}/antlr-$ANTLR_VERSION-complete.jar" \
         "$HOME/ferramentas/antlr-$ANTLR_VERSION-complete.jar" \
         "../../ferramentas/antlr-$ANTLR_VERSION-complete.jar"; do
    [ -n "$c" ] && [ -f "$c" ] && { JAR="$(cd "$(dirname "$c")" && pwd)/$(basename "$c")"; break; }
done
if [ -z "$JAR" ]; then
    echo -e "${VERMELHO}antlr-$ANTLR_VERSION-complete.jar não encontrado (defina ANTLR_JAR ou TCC_TOOLS).${RESET}"
    exit 1
fi
antlr() { java -jar "$JAR" "$@"; }

# --- 1. Ambiente Python com o runtime do ANTLR (mesma versão da ferramenta) --
if [ ! -x "$VENV/bin/python" ]; then
    echo -e "${NEGRITO}>> Criando ambiente virtual $VENV${RESET}"
    python3 -m venv "$VENV" || exit 1
fi
PY="$PWD/$VENV/bin/python"
RUNTIME=$("$PY" -c "from importlib.metadata import version; print(version('antlr4-python3-runtime'))" 2>/dev/null)
if [ "$RUNTIME" != "$ANTLR_VERSION" ]; then
    echo -e "${NEGRITO}>> Instalando antlr4-python3-runtime==$ANTLR_VERSION${RESET}"
    "$PY" -m pip install -q --disable-pip-version-check "antlr4-python3-runtime==$ANTLR_VERSION" || exit 1
fi

# --- 2. Gerar o parser Python (com visitor) ---------------------------------
echo -e "${NEGRITO}>> Gerando parser Python a partir de $GRAMMAR.g4 (ANTLR $ANTLR_VERSION)${RESET}"
rm -rf "$BUILD" && mkdir -p "$BUILD"
antlr -Dlanguage=Python3 -visitor -no-listener -o "$BUILD" -Xexact-output-dir "$GRAMMAR.g4" || exit 1

# --- 2a. Gerar o parser Python com ações embutidas (sem visitor) -------------
echo -e "${NEGRITO}>> Gerando parser Python a partir de $GRAMMAR_PYACOES.g4 (sem visitor/listener)${RESET}"
rm -rf "$BUILD_PYACOES" && mkdir -p "$BUILD_PYACOES"
antlr -Dlanguage=Python3 -no-visitor -no-listener -o "$BUILD_PYACOES" -Xexact-output-dir "$GRAMMAR_PYACOES.g4" || exit 1

# --- 2b. Gerar e compilar o parser Java (ações embutidas, sem visitor) --------
echo -e "${NEGRITO}>> Gerando parser Java a partir de $GRAMMAR_JAVA.g4 (sem visitor/listener)${RESET}"
rm -rf "$BUILD_JAVA" && mkdir -p "$BUILD_JAVA"
antlr -no-visitor -no-listener -o "$BUILD_JAVA" -Xexact-output-dir "$GRAMMAR_JAVA.g4" || exit 1
javac -cp "$JAR" -d "$BUILD_JAVA" "$BUILD_JAVA"/*.java || exit 1

# --- 3. Driver Python: lê a entrada do stdin, imprime árvore e valor ---------
cat > "$BUILD/driver.py" <<'EOF'
import sys
from antlr4 import InputStream, CommonTokenStream
from antlr4.error.ErrorListener import ErrorListener
from TCC2102CalcLexer import TCC2102CalcLexer
from TCC2102CalcParser import TCC2102CalcParser
from TCC2102CalcVisitor import TCC2102CalcVisitor


class ColetorErros(ErrorListener):
    def __init__(self):
        self.erros = []

    def syntaxError(self, recognizer, offendingSymbol, line, column, msg, e):
        self.erros.append(f"line {line}:{column} {msg}")


class Avaliador(TCC2102CalcVisitor):
    def visitCalc(self, ctx):      return [self.visit(l) for l in ctx.linha()]
    def visitLinha(self, ctx):     return self.visit(ctx.expr())
    def visitAdd(self, ctx):       return self.visit(ctx.expr()) + self.visit(ctx.term())
    def visitSub(self, ctx):       return self.visit(ctx.expr()) - self.visit(ctx.term())
    def visitExprTerm(self, ctx):  return self.visit(ctx.term())
    def visitMul(self, ctx):       return self.visit(ctx.term()) * self.visit(ctx.factor())
    def visitDiv(self, ctx):       return self.visit(ctx.term()) / self.visit(ctx.factor())
    def visitTermFactor(self, ctx): return self.visit(ctx.factor())
    def visitParens(self, ctx):    return self.visit(ctx.expr())
    def visitNumber(self, ctx):    return float(ctx.NUM().getText())


texto = sys.stdin.read()
coletor = ColetorErros()

lexer = TCC2102CalcLexer(InputStream(texto))
lexer.removeErrorListeners()
lexer.addErrorListener(coletor)
tokens = CommonTokenStream(lexer)

if "-tokens" in sys.argv:
    tokens.fill()
    for t in tokens.tokens:
        nome = "EOF" if t.type == -1 else TCC2102CalcLexer.symbolicNames[t.type]
        print(f"{nome:<7} {t.text!r:<8} linha {t.line}:{t.column}")
    sys.exit(1 if coletor.erros else 0)

parser = TCC2102CalcParser(tokens)
parser.removeErrorListeners()
parser.addErrorListener(coletor)
arvore = parser.calc()

print("arvore:", arvore.toStringTree(recog=parser))
for erro in coletor.erros:
    print(erro, file=sys.stderr)
if coletor.erros:
    sys.exit(1)

try:
    print("valor:", " ".join(f"{v:g}" for v in Avaliador().visit(arvore)))
except ZeroDivisionError:
    print("erro semântico: divisão por zero", file=sys.stderr)
    sys.exit(1)
EOF

calc_py() { (cd "$BUILD" && "$PY" driver.py "$@"); }

# Python com ações: o pygrun (o grun do Python) chama a regra inicial; as ações imprimem
# "Resultado: X". Chamado como módulo porque o script pygrun do venv quebra com espaços no caminho.
calc_pyacoes() {
    local saida valores
    saida=$(cd "$BUILD_PYACOES" && "$PY" -m antlr4._pygrun "$GRAMMAR_PYACOES" calc 2>stderr.txt)
    if grep -q ZeroDivisionError "$BUILD_PYACOES/stderr.txt"; then
        echo "erro semântico: divisão por zero" >&2; return 1
    fi
    [ -s "$BUILD_PYACOES/stderr.txt" ] && { cat "$BUILD_PYACOES/stderr.txt" >&2; return 1; }
    valores=$(sed -n 's/^Resultado: //p' <<<"$saida")
    echo "valor: $(awk '{ printf "%s%g", (NR > 1 ? " " : ""), $1 }' <<<"$valores")"
}

# Java: o grun imprime "Resultado: X" por linha; erros de sintaxe vão para o stderr.
# Sem driver, a divisão por zero dá Infinity/NaN (double) — tratada aqui como erro semântico.
calc_java() {
    local saida valores
    saida=$(java -cp "$JAR:$BUILD_JAVA" org.antlr.v4.gui.TestRig "$GRAMMAR_JAVA" calc 2>"$BUILD_JAVA/stderr.txt")
    [ -s "$BUILD_JAVA/stderr.txt" ] && { cat "$BUILD_JAVA/stderr.txt" >&2; return 1; }
    valores=$(sed -n 's/^Resultado: //p' <<<"$saida")
    if grep -qE 'Infinity|NaN' <<<"$valores"; then
        echo "erro semântico: divisão por zero" >&2; return 1
    fi
    echo "valor: $(awk '{ printf "%s%g", (NR > 1 ? " " : ""), $1 }' <<<"$valores")"
}

# --- Modo tokens --------------------------------------------------------------
if [ "${1:-}" = "-tokens" ]; then
    printf '%s' "${2:?informe a expressão, ex: \"2+3;\"}" | calc_py -tokens
    exit $?
fi

# --- 4. Executar um caso de teste --------------------------------------------
# testar "<entrada>" <ok|erro> [valor esperado]
PASS=0; FAIL=0
testar() {
    local entrada="$1" esperado="$2" valor_esperado="${3:-}" alvo saida erros obtido valor
    for alvo in py pyacoes java; do
        saida=$(printf '%s' "$entrada" | calc_$alvo 2>"$BUILD/stderr.txt")
        [ $? -eq 0 ] && obtido=ok || obtido=erro
        erros=$(cat "$BUILD/stderr.txt")
        valor=$(sed -n 's/^valor: //p' <<<"$saida")

        if [ "$obtido" != "$esperado" ]; then
            FAIL=$((FAIL + 1)); echo -e "${VERMELHO}[FALHOU]${RESET} [$alvo] esperado=$esperado obtido=$obtido: $entrada"
        elif [ -n "$valor_esperado" ] && [ "$valor" != "$valor_esperado" ]; then
            FAIL=$((FAIL + 1)); echo -e "${VERMELHO}[FALHOU]${RESET} [$alvo] valor esperado=$valor_esperado obtido=$valor: $entrada"
        else
            PASS=$((PASS + 1)); echo -e "${VERDE}[PASSOU]${RESET} [$alvo] ($esperado${valor:+ = $valor}) $entrada"
        fi
        [ "$alvo" = py ] && sed -n 's/^arvore: /         árvore: /p' <<<"$saida"
        [ -n "$erros" ] && echo -e "${AMARELO}$(sed 's/^/         /' <<<"$erros")${RESET}"
    done
}

echo
if [ $# -ge 1 ]; then
    # Cada linha não vazia do arquivo é uma entrada (cada linha pode ter uma ou mais expressões)
    echo -e "${NEGRITO}>> Testando linhas de $1${RESET}"
    while IFS= read -r linha || [ -n "$linha" ]; do
        [ -z "${linha// }" ] && continue
        testar "$linha" ok
    done < "$1"
else
    echo -e "${NEGRITO}>> Entradas válidas${RESET}"
    testar "1 * 3;"                ok 3
    testar "2 + 3 * (4 - 1);"      ok 11
    testar "42;"                   ok 42
    testar "3.14 * 2;"             ok 6.28
    testar "10 - 4 - 3;"           ok 3      # associatividade à esquerda
    testar "8 / 2 / 2;"            ok 2      # associatividade à esquerda
    testar "((1 + 2) * (3 + 4));"  ok 21
    testar $'1\n+\t2 ;\n'          ok 3      # espaços em branco ignorados
    testar "1 * 3; 2 + 2;"         ok "3 4"  # mais de uma expressão
    testar $'1 * 3;\n2 + 3 * (4 - 1);\n' ok "3 11"  # uma expressão por linha

    echo
    echo -e "${NEGRITO}>> Entradas inválidas${RESET}"
    testar "2 + 3"                 erro   # falta ';'
    testar "2 + ;"                 erro   # falta operando
    testar "(2 + 3;"               erro   # parêntese não fechado
    testar "2 + 3);"               erro   # parêntese extra
    testar "2 \$ 3;"               erro   # caractere inválido (léxico)
    testar "3.;"                   erro   # número mal formado
    testar "1 * 3; 2 + 2"          erro   # falta ';' na segunda expressão
    testar ";"                     erro   # vazio
    testar "5 / (2 - 2);"          erro   # divisão por zero (semântico)
fi

# --- 5. Resumo ---------------------------------------------------------------
echo
TOTAL=$((PASS + FAIL))
if [ "$FAIL" -eq 0 ]; then
    echo -e "${VERDE}${NEGRITO}Todos os $TOTAL testes passaram.${RESET}"
else
    echo -e "${VERMELHO}${NEGRITO}$FAIL de $TOTAL testes falharam.${RESET}"
fi
[ "$FAIL" -eq 0 ]
