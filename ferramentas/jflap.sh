#!/usr/bin/env bash
# Abre o JFLAP 7.1 (ajuste uiScale conforme o monitor: 1.0, 1.5 ou 2.0)
DIR="$(cd "$(dirname "$0")" && pwd)"
java -Dsun.java2d.uiScale=2.0 -jar "$DIR/JFLAP7.1.jar" "$@"
