#!/bin/bash
antlr4 Do.g4
javac *.java
java DoParser do.txt
