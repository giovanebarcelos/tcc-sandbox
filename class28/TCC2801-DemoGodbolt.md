# TCC2801-DemoGodbolt.md | Aula 28

**Demonstração do Compiler Explorer (godbolt.org)**

## O que é o Compiler Explorer?

O [Compiler Explorer](https://godbolt.org) é uma ferramenta web gratuita que mostra o **código assembly** gerado por compiladores reais (gcc, clang, MSVC) a partir de código-fonte em C, C++, Rust, Go, etc. Permite comparar **níveis de otimização** (-O0, -O1, -O2, -O3) e ver como o compilador transforma o código.

---

## Demo 1: Constant Folding

### Código C

```c
int demo_const_fold() {
    int x = 3 + 4 * 2;
    return x;
}
```

### Com -O0 (sem otimização)

```asm
demo_const_fold:
    push   rbp
    mov    rbp, rsp
    mov    DWORD PTR [rbp-4], 11     ; 3 + 4*2 = 11 calculado em runtime
    mov    eax, DWORD PTR [rbp-4]
    pop    rbp
    ret
```

### Com -O2 (com otimização)

```asm
demo_const_fold:
    mov    eax, 11                   ; constant folding: resultado pré-calculado!
    ret
```

**Observação:** o compilador calculou `3 + 4 * 2 = 11` em tempo de compilação e eliminou a variável.

---

## Demo 2: Dead Code Elimination

### Código C

```c
int demo_dead_code(int x) {
    int y = x * 2;
    int z = 100;          // código morto: z nunca é usado
    return y;
}
```

### Com -O2

```asm
demo_dead_code:
    lea    eax, [rdi+rdi]    ; y = x + x (otimizado: *2 → +)
    ret                       ; z foi eliminado!
```

---

## Demo 3: Loop Unrolling

### Código C

```c
int demo_loop(int n) {
    int sum = 0;
    for (int i = 0; i < 4; i++) {
        sum += i;
    }
    return sum;
}
```

### Com -O2

```asm
demo_loop:
    mov    eax, 6              ; 0+1+2+3 = 6 calculado em compile time!
    ret
```

---

## Como reproduzir

1. Acesse https://godbolt.org
2. Cole o código C no painel esquerdo
3. Selecione o compilador (ex.: gcc 13.2 x86-64)
4. Adicione flags: `-O0`, `-O1`, `-O2`, `-O3`
5. Compare o assembly gerado no painel direito

---

## Tópicos para discussão

- **Constant folding**: o compilador avalia expressões constantes em compile time (vimos isso no TCC2502!)
- **Dead code elimination**: código inalcançável ou variáveis não usadas são removidas
- **Loop unrolling**: loops com limite conhecido são desenrolados
- **Inlining**: funções pequenas são embutidas no chamador
- **Register allocation**: variáveis são mapeadas para registradores em vez de memória
- **O trade-off -O0 vs -O3**: otimização vs tempo de compilação vs debuggabilidade

---

## Conexão com o curso

| Otimização do compilador | Onde vimos no curso |
|---|---|
| Constant folding | Aula 25 — [`TCC2502-Otimizador.py`](https://raw.githubusercontent.com/giovanebarcelos/tcc-sandbox/main/class25/python/TCC2502-Otimizador.py) |
| Dead code elimination | Aula 25 — [`TCC2502-Otimizador.java`](https://raw.githubusercontent.com/giovanebarcelos/tcc-sandbox/main/class25/java/TCC2502-Otimizador.java) |
| Geração de código C | Aula 25 — [`TCC2501-GeradorCodigo.py`](https://raw.githubusercontent.com/giovanebarcelos/tcc-sandbox/main/class25/python/TCC2501-GeradorCodigo.py) |
| Pipeline do compilador | Aula 15 — [`TCC1501-FasesCompilador.py`](https://raw.githubusercontent.com/giovanebarcelos/tcc-sandbox/main/class15/python/TCC1501-FasesCompilador.py) |
