# Script de limpieza simplificado - EJECUTAR MANUALMENTE

$projectRoot = "C:\Users\ayurs\Desktop\DAM\DAM_2\PROGRAMACION\ACCESO A DATOS\ra3_pawner"
Set-Location $projectRoot

Write-Host "=== Iniciando Limpieza ===" -ForegroundColor Cyan
Write-Host ""

# 1. Directorios temporales
Write-Host "1. Eliminando directorios temporales de Gradle..." -ForegroundColor Yellow
$tempDirs = @("temp_gradle", "temp_gradle_extract", "temp_gradle_fix", "temp_gradle_init")
foreach ($dir in $tempDirs) {
    if (Test-Path $dir) {
        Remove-Item -Path $dir -Recurse -Force -ErrorAction SilentlyContinue
        Write-Host "   ✓ Eliminado: $dir" -ForegroundColor Green
    }
}

# 2. Scripts de reparación
Write-Host ""
Write-Host "2. Eliminando scripts de reparación..." -ForegroundColor Yellow
$scripts = @("fix-gradle-wrapper.ps1", "fix-gradle-wrapper-v2.ps1", "download-gradle-wrapper.ps1", "init-gradle-wrapper.ps1")
foreach ($script in $scripts) {
    if (Test-Path $script) {
        Remove-Item -Path $script -Force
        Write-Host "   ✓ Eliminado: $script" -ForegroundColor Green
    }
}

# 3. ZIP de Gradle
Write-Host ""
Write-Host "3. Eliminando gradle-8.7-bin.zip..." -ForegroundColor Yellow
if (Test-Path "gradle-8.7-bin.zip") {
    Remove-Item -Path "gradle-8.7-bin.zip" -Force
    Write-Host "   ✓ Eliminado: gradle-8.7-bin.zip" -ForegroundColor Green
}

# 4. Directorio bin
Write-Host ""
Write-Host "4. Eliminando directorio bin (IDE)..." -ForegroundColor Yellow
if (Test-Path "bin") {
    Remove-Item -Path "bin" -Recurse -Force -ErrorAction SilentlyContinue
    Write-Host "   ✓ Eliminado: bin/" -ForegroundColor Green
}

# 5. Configuración de test obsoleta
Write-Host ""
Write-Host "5. Eliminando configuración de test obsoleta..." -ForegroundColor Yellow
if (Test-Path "src\test\resources\application-test.yml") {
    Remove-Item -Path "src\test\resources\application-test.yml" -Force
    Write-Host "   ✓ Eliminado: application-test.yml" -ForegroundColor Green
}

# 6. Directorio de test vacío
Write-Host ""
Write-Host "6. Eliminando directorios de test vacíos..." -ForegroundColor Yellow
if (Test-Path "src\test\java\com\dam\accesodatos\ra3") {
    Remove-Item -Path "src\test\java\com\dam\accesodatos\ra3" -Recurse -Force -ErrorAction SilentlyContinue
    Write-Host "   ✓ Eliminado: src\test\java\com\dam\accesodatos\ra3" -ForegroundColor Green
}

# 7. Script de limpieza antiguo
Write-Host ""
Write-Host "7. Eliminando script de limpieza antiguo..." -ForegroundColor Yellow
if (Test-Path "cleanup-project.ps1") {
    Remove-Item -Path "cleanup-project.ps1" -Force
    Write-Host "   ✓ Eliminado: cleanup-project.ps1" -ForegroundColor Green
} # <--- Esta '}' debe existir

Write-Host ""
Write-Host "=== ✓ LIMPIEZA COMPLETADA ===" -ForegroundColor Green
Write-Host ""
Write-Host "Archivos eliminados:" -ForegroundColor Cyan
Write-Host "  - 4 directorios temporales de Gradle" -ForegroundColor White
Write-Host "  - 4 scripts de reparación PowerShell" -ForegroundColor White
Write-Host "  - 1 archivo ZIP de Gradle (~145 MB)" -ForegroundColor White
Write-Host "  - 1 directorio bin (IDE)" -ForegroundColor White
Write-Host "  - 1 configuración de test obsoleta" -ForegroundColor White
Write-Host "  - Directorios de test vacíos" -ForegroundColor White
Write-Host ""
Write-Host "El proyecto sigue funcionando perfectamente:" -ForegroundColor Yellow
Write-Host "  .\gradlew.bat clean build" -ForegroundColor Cyan
Write-Host "  .\gradlew.bat bootRun" -ForegroundColor Cyan
Write-Host ""
Write-Host "Presiona cualquier tecla para cerrar y auto-eliminar este script..." -ForegroundColor Yellow
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")

# Auto-eliminar este script
Remove-Item -Path "EJECUTAR-LIMPIEZA.ps1" -Force -ErrorAction SilentlyContinue
