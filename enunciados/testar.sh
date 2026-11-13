#!/usr/bin/env bash
# =============================================================================
# testar.sh — roda os casos de teste de um enunciado contra o SEU compilador.
#
# Uso:  ./testar.sh <pasta_do_enunciado> <comando do seu compilador...>
#   ex.: ./testar.sh 04_calculadora java -cp "build/classes:$ANTLR_JAR" Main
#        cd 04_calculadora && ../testar.sh . java -cp build/classes Main
#        ../testar.sh . ./executar.sh          (08, 09, 30, 31: gera, compila com gcc e executa)
#
# Para cada testes/ok_*  : executa "<comando> testes/ok_N.ext", exige código de saída 0 e compara
#                          a saída padrão com testes/ok_N.ext.esperado.
# Para cada testes/erro_*: exige código de saída diferente de 0 e alguma mensagem na saída de erro;
#                          mostra a mensagem de referência (testes/erro_N.ext.erro) ao lado da sua.
# testes/config define como comparar (MODO=completa|execucao, MANTER, IGNORAR, SUBST).
# =============================================================================
if [ $# -lt 2 ]; then
  sed -n '3,15p' "$0" | sed 's/^# \{0,1\}//'; exit 2
fi
PASTA="$1"; shift
TESTES="$PASTA/testes"
[ -d "$TESTES" ] || { echo "Pasta de testes não encontrada: $TESTES"; exit 2; }

MODO=completa; MANTER=""; IGNORAR=""; SUBST=""
# shellcheck disable=SC1091
[ -f "$TESTES/config" ] && source "$TESTES/config"

if [ -t 1 ]; then V=$'\e[1;32m'; R=$'\e[1;31m'; A=$'\e[1;33m'; C=$'\e[0;90m'; F=$'\e[0m'; else V=; R=; A=; C=; F=; fi
TMP="$(mktemp -d)"; trap 'rm -rf "$TMP"' EXIT
TIMEOUT=""; command -v timeout >/dev/null && TIMEOUT="timeout 20"

# normaliza uma saída: sem \r, sem espaços no fim, (modo execução) só a parte depois do último
# "=== Execução", sem "(código de saída", e com os filtros do config
normalizar() {
  local txt; txt="$(tr -d '\r' <"$1" | sed -E 's/[[:space:]]+$//')"
  if [ "$MODO" = execucao ]; then
    local n; n="$(printf '%s\n' "$txt" | grep -n '^=== Execução' | tail -1 | cut -d: -f1)"
    [ -n "$n" ] && txt="$(printf '%s\n' "$txt" | tail -n +"$((n + 1))")"
    txt="$(printf '%s\n' "$txt" | grep -v '^(código de saída')"
  fi
  [ -n "$MANTER" ] && txt="$(printf '%s\n' "$txt" | grep -E -- "$MANTER")"
  [ -n "$IGNORAR" ] && txt="$(printf '%s\n' "$txt" | grep -Ev -- "$IGNORAR")"
  [ -n "$SUBST" ] && txt="$(printf '%s\n' "$txt" | sed -E "$SUBST")"
  printf '%s\n' "$txt" | sed -e :a -e '/^\n*$/{$d;N;ba' -e '}'    # remove linhas vazias no fim
}

passou=0; falhou=0
for t in "$TESTES"/ok_*; do
  case "$t" in *.esperado) continue ;; esac
  [ -f "$t" ] || continue
  nome="$(basename "$t")"
  $TIMEOUT "$@" "$t" >"$TMP/out" 2>"$TMP/err"; st=$?
  normalizar "$TMP/out" >"$TMP/obtido"; normalizar "$t.esperado" >"$TMP/esperado"
  if [ $st -eq 0 ] && diff -q "$TMP/esperado" "$TMP/obtido" >/dev/null; then
    passou=$((passou + 1)); echo "${V}  ✓ $nome${F}"
  else
    falhou=$((falhou + 1))
    if [ $st -ne 0 ]; then echo "${R}  ✗ $nome: código de saída $st (esperado 0)${F}"; sed 's/^/      /' "$TMP/err" | head -5
    else echo "${R}  ✗ $nome: saída diferente da esperada (- esperado, + obtido)${F}"; fi
    diff -u "$TMP/esperado" "$TMP/obtido" | tail -n +3 | head -30 | sed 's/^/      /'
  fi
done
for t in "$TESTES"/erro_*; do
  case "$t" in *.erro) continue ;; esac
  [ -f "$t" ] || continue
  nome="$(basename "$t")"
  $TIMEOUT "$@" "$t" >"$TMP/out" 2>"$TMP/err"; st=$?
  if [ $st -ne 0 ] && [ -s "$TMP/err" ]; then
    passou=$((passou + 1)); echo "${V}  ✓ $nome (rejeitado)${F}"
  else
    falhou=$((falhou + 1))
    if [ $st -eq 0 ]; then echo "${R}  ✗ $nome: foi aceito (código de saída 0), mas deveria ser rejeitado${F}"
    else echo "${R}  ✗ $nome: código de saída $st, mas nada foi escrito na saída de erro (stderr)${F}"; fi
  fi
  echo "${C}      referência: $(head -1 "$t.erro")${F}"
  [ -s "$TMP/err" ] && echo "${C}      obtido    : $(head -1 "$TMP/err")${F}"
done
total=$((passou + falhou))
if [ $falhou -eq 0 ]; then echo "${V}━━ $total/$total teste(s) passaram ━━${F}"
else echo "${A}━━ $passou de $total teste(s) passaram, $falhou falharam ━━${F}"; exit 1; fi
