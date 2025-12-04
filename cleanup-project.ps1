# Script de limpieza del proyecto ra3_pawner

$projectRoot = "C:\Users\ayurs\Desktop\DAM\DAM_2\PROGRAMACION\ACCESO A DATOS\ra3_pawner"

Write-Host "=== Limpieza del Proyecto ra3_pawner ===" -ForegroundColor Cyan
Write-Host ""

$itemsToDelete = @(
    # Directorios temporales de Gradle
    "temp_gradle",
    "temp_gradle_extract", 
    "temp_gradle_fix",
    "temp_gradle_init",
    
    # Scripts de reparación (ya no necesarios)
    "fix-gradle-wrapper.ps1",
    "fix-gradle-wrapper-v2.ps1",
    "download-gradle-wrapper.ps1",
    "init-gradle-wrapper.ps1",
    
    # Archivo ZIP de Gradle (ya no necesario)
    "gradle-8.7-bin.zip",
    
    # Directorio bin (generado por IDE)
    "bin",
    
    # Configuración de test obsoleta (usa H2, proyecto usa MySQL)
    "src\test\resources\application-test.yml",
    
    # Directorios de test vacíos
    "src\test\java\com\dam\accesodatos\ra3"
)

$deletedCount = 0
$notFoundCount = 0

foreach ($item in $itemsToDelete) {
    $fullPath = Join-Path $projectRoot $item
    
    if (Test-Path $fullPath) {
        try {
            $isDirectory = (Get-Item $fullPath) -is [System.IO.DirectoryInfo]
            
            if ($isDirectory) {
                Remove-Item -Path $fullPath -Recurse -Force
                Write-Host "[ELIMINADO] Directorio: $item" -ForegroundColor Green
            } else {
                Remove-Item -Path $fullPath -Force
                Write-Host "[ELIMINADO] Archivo: $item" -ForegroundColor Green
            }
            
            $deletedCount++
        } catch {
            Write-Host "[ERROR] No se pudo eliminar: $item - $_" -ForegroundColor Red
        }
    } else {
        Write-Host "[NO EXISTE] $item" -ForegroundColor Yellow
        $notFoundCount++
    }
}

Write-Host ""
Write-Host "=== Resumen de Limpieza ===" -ForegroundColor Cyan
Write-Host "Elementos eliminados: $deletedCount" -ForegroundColor Green
Write-Host "Elementos no encontrados: $notFoundCount" -ForegroundColor Yellow
Write-Host ""
Write-Host "✓ Limpieza completada" -ForegroundColor Green
