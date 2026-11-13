# =============================================================================
# testar.ps1 — roda os casos de teste de um enunciado contra o SEU compilador (Windows PowerShell 5.1+).
#
# Uso:  .\testar.ps1 <pasta_do_enunciado> <comando do seu compilador...>
#   ex.: .\testar.ps1 04_calculadora java -cp "build\classes;C:\ferramentas\antlr-4.13.1-complete.jar" Main
#        cd 04_calculadora; ..\testar.ps1 . java -cp build\classes Main
#        ..\testar.ps1 . .\executar.bat        (08, 09, 30, 31: gera, compila com gcc e executa)
# Se o Windows bloquear scripts:  powershell -ExecutionPolicy Bypass -File .\testar.ps1 ...
#
# Mesmas regras do testar.sh: ok_* precisam de código de saída 0 e saída igual a ok_N.ext.esperado;
# erro_* precisam de código de saída diferente de 0 e alguma mensagem na saída de erro.
# =============================================================================
if ($args.Count -lt 2) {
    Get-Content -LiteralPath $PSCommandPath -TotalCount 11 | Select-Object -Skip 2 | ForEach-Object { $_ -replace '^# ?', '' }
    exit 2
}
$Pasta = $args[0]
$Exe = [string]$args[1]
$ArgsFixos = @()
if ($args.Count -gt 2) { $ArgsFixos = @($args[2..($args.Count - 1)] | ForEach-Object { [string]$_ }) }
$Testes = Join-Path $Pasta 'testes'
if (-not (Test-Path -LiteralPath $Testes)) { Write-Host "Pasta de testes não encontrada: $Testes"; exit 2 }

$UTF8 = New-Object System.Text.UTF8Encoding($false)
try { [Console]::OutputEncoding = $UTF8 } catch { }

# testes\config: MODO, MANTER, IGNORAR, SUBST (formato KEY='valor')
$Cfg = @{ MODO = 'completa'; MANTER = ''; IGNORAR = ''; SUBST = '' }
$arqCfg = Join-Path $Testes 'config'
if (Test-Path -LiteralPath $arqCfg) {
    foreach ($l in [IO.File]::ReadAllLines((Resolve-Path -LiteralPath $arqCfg).ProviderPath, $UTF8)) {
        if ($l -match "^\s*([A-Z]+)=(.*)$") { $Cfg[$Matches[1]] = $Matches[2].Trim().Trim("'") }
    }
}

function Quote-Arg([string]$a) {
    if ($a -eq '') { return '""' }
    if ($a -notmatch '[\s"]') { return $a }
    $s = $a -replace '(\\*)"', '$1$1\"'
    $s = $s -replace '(\\+)$', '$1$1'
    return '"' + $s + '"'
}

# executa "<comando> arquivo" e devolve código de saída, stdout e stderr
function Invoke-Compilador([string]$arquivo) {
    $psi = New-Object System.Diagnostics.ProcessStartInfo
    $psi.FileName = $Exe
    $psi.Arguments = (@(($ArgsFixos + @($arquivo)) | ForEach-Object { Quote-Arg $_ }) -join ' ')
    $psi.UseShellExecute = $false
    $psi.RedirectStandardOutput = $true
    $psi.RedirectStandardError = $true
    $psi.StandardOutputEncoding = $UTF8
    $psi.StandardErrorEncoding = $UTF8
    $psi.WorkingDirectory = (Get-Location).ProviderPath
    $p = [System.Diagnostics.Process]::Start($psi)
    $o = $p.StandardOutput.ReadToEndAsync()
    $e = $p.StandardError.ReadToEndAsync()
    $codigo = 0
    if (-not $p.WaitForExit(20000)) { try { $p.Kill() } catch { }; $p.WaitForExit(); $codigo = 124 }
    else { $p.WaitForExit(); $codigo = $p.ExitCode }
    return @{ codigo = $codigo; out = $o.Result; err = $e.Result }
}

# mesma normalização do testar.sh
function Normalizar([string]$texto) {
    $linhas = @(($texto -replace "`r", '') -split "`n" | ForEach-Object { $_.TrimEnd() })
    if ($Cfg.MODO -eq 'execucao') {
        $ultimo = -1
        for ($i = 0; $i -lt $linhas.Count; $i++) { if ($linhas[$i].StartsWith('=== Execução')) { $ultimo = $i } }
        if ($ultimo -ge 0) { $linhas = @($linhas | Select-Object -Skip ($ultimo + 1)) }
        $linhas = @($linhas | Where-Object { -not $_.StartsWith('(código de saída') })
    }
    if ($Cfg.MANTER) { $linhas = @($linhas | Where-Object { $_ -match $Cfg.MANTER }) }
    if ($Cfg.IGNORAR) { $linhas = @($linhas | Where-Object { $_ -notmatch $Cfg.IGNORAR }) }
    if ($Cfg.SUBST -match '^s/(.*)/(.*)/$') {
        $de = $Matches[1]; $para = $Matches[2]
        $linhas = @($linhas | ForEach-Object { $_ -replace $de, $para })
    }
    while ($linhas.Count -gt 0 -and $linhas[$linhas.Count - 1] -eq '') { $linhas = @($linhas | Select-Object -First ($linhas.Count - 1)) }
    return ,$linhas
}

$passou = 0; $falhou = 0
$arquivos = Get-ChildItem -LiteralPath $Testes -File | Sort-Object Name
foreach ($f in ($arquivos | Where-Object { $_.Name -like 'ok_*' -and $_.Name -notlike '*.esperado' })) {
    $r = Invoke-Compilador $f.FullName
    $obtido = Normalizar $r.out
    $esperado = Normalizar ([IO.File]::ReadAllText("$($f.FullName).esperado", $UTF8))
    $igual = ($obtido -join "`n") -ceq ($esperado -join "`n")
    if ($r.codigo -eq 0 -and $igual) { $passou++; Write-Host "  OK  $($f.Name)" -ForegroundColor Green }
    else {
        $falhou++
        if ($r.codigo -ne 0) {
            Write-Host "  X   $($f.Name): código de saída $($r.codigo) (esperado 0)" -ForegroundColor Red
            ($r.err -split "`n" | Select-Object -First 5) | ForEach-Object { Write-Host "      $_" }
        } else { Write-Host "  X   $($f.Name): saída diferente da esperada (< esperado, > obtido)" -ForegroundColor Red }
        if ($obtido.Count -eq 0) { Write-Host '      (a saída padrão ficou vazia)' }
        else { Compare-Object $esperado $obtido -SyncWindow 1000 | Select-Object -First 30 | ForEach-Object {
            $lado = if ($_.SideIndicator -eq '<=') { '<' } else { '>' }
            Write-Host "      $lado $($_.InputObject)"
        } }
    }
}
foreach ($f in ($arquivos | Where-Object { $_.Name -like 'erro_*' -and $_.Name -notlike '*.erro' })) {
    $r = Invoke-Compilador $f.FullName
    if ($r.codigo -ne 0 -and $r.err.Trim() -ne '') { $passou++; Write-Host "  OK  $($f.Name) (rejeitado)" -ForegroundColor Green }
    else {
        $falhou++
        if ($r.codigo -eq 0) { Write-Host "  X   $($f.Name): foi aceito (código de saída 0), mas deveria ser rejeitado" -ForegroundColor Red }
        else { Write-Host "  X   $($f.Name): código de saída $($r.codigo), mas nada foi escrito na saída de erro" -ForegroundColor Red }
    }
    $ref = ([IO.File]::ReadAllLines("$($f.FullName).erro", $UTF8))[0]
    Write-Host "      referência: $ref" -ForegroundColor DarkGray
    if ($r.err.Trim() -ne '') { Write-Host "      obtido    : $(($r.err.Trim() -split "`n")[0].TrimEnd())" -ForegroundColor DarkGray }
}
$total = $passou + $falhou
if ($falhou -eq 0) { Write-Host "== $total/$total teste(s) passaram ==" -ForegroundColor Green; exit 0 }
Write-Host "== $passou de $total teste(s) passaram, $falhou falharam ==" -ForegroundColor Yellow
exit 1
