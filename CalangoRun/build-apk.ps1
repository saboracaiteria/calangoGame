# Script para buildar o APK do Calango Run Deluxe
# Execute no PowerShell

Write-Host "======================================" -ForegroundColor Cyan
Write-Host "  Calango Run Deluxe - APK Builder" -ForegroundColor Cyan
Write-Host "======================================" -ForegroundColor Cyan
Write-Host ""

# Verificar se gradlew existe
if (-Not (Test-Path "gradlew.bat")) {
    Write-Host "ERRO: gradlew.bat nao encontrado!" -ForegroundColor Red
    Write-Host "Abra o projeto no Android Studio primeiro para gerar os arquivos Gradle." -ForegroundColor Yellow
    exit 1
}

Write-Host "Escolha o tipo de build:" -ForegroundColor Yellow
Write-Host "1. Debug APK (para testes)" -ForegroundColor White
Write-Host "2. Release APK (para publicacao)" -ForegroundColor White
Write-Host ""
$choice = Read-Host "Digite 1 ou 2"

switch ($choice) {
    "1" {
        Write-Host ""
        Write-Host "Iniciando build Debug..." -ForegroundColor Green
        .\gradlew.bat assembleDebug
        
        if ($LASTEXITCODE -eq 0) {
            Write-Host ""
            Write-Host "Build concluido com sucesso!" -ForegroundColor Green
            Write-Host ""
            $apkPath = "app\build\outputs\apk\debug\app-debug.apk"
            if (Test-Path $apkPath) {
                Write-Host "APK gerado em: $apkPath" -ForegroundColor Cyan
                Write-Host ""
                $open = Read-Host "Deseja abrir a pasta do APK? (s/n)"
                if ($open -eq "s" -or $open -eq "S") {
                    explorer.exe "/select,$(Convert-Path $apkPath)"
                }
            }
        } else {
            Write-Host ""
            Write-Host "ERRO no build! Verifique os logs acima." -ForegroundColor Red
        }
    }
    "2" {
        Write-Host ""
        Write-Host "Iniciando build Release..." -ForegroundColor Green
        Write-Host "NOTA: Release requer configuracao de signing." -ForegroundColor Yellow
        Write-Host ""
        .\gradlew.bat assembleRelease
        
        if ($LASTEXITCODE -eq 0) {
            Write-Host ""
            Write-Host "Build concluido com sucesso!" -ForegroundColor Green
            Write-Host ""
            $apkPath = "app\build\outputs\apk\release\app-release.apk"
            if (Test-Path $apkPath) {
                Write-Host "APK gerado em: $apkPath" -ForegroundColor Cyan
            }
        } else {
            Write-Host ""
            Write-Host "ERRO no build! Verifique os logs acima." -ForegroundColor Red
        }
    }
    default {
        Write-Host "Opcao invalida!" -ForegroundColor Red
    }
}

Write-Host ""
Write-Host "Pressione qualquer tecla para sair..." -ForegroundColor Gray
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
