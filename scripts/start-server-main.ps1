param(
    [string]$DbUser = "postgres",
    [string]$DbPassword = "postgres"
)

$ErrorActionPreference = "Stop"
Set-Location "$PSScriptRoot\.."

Write-Host "[1/4] Levantando PostgreSQL con Docker Compose..." -ForegroundColor Cyan
docker compose up -d postgres

Write-Host "[2/4] Aplicando variables de entorno para servidor principal..." -ForegroundColor Cyan
$env:DATABASE_URL = "jdbc:postgresql://localhost:5432/assistentia"
$env:DATABASE_USERNAME = $DbUser
$env:DATABASE_PASSWORD = $DbPassword
$env:FLYWAY_ENABLED = "false"

Write-Host "[3/4] Servidor principal listo en esta PC. IP LAN sugerida para clientes: 192.168.1.41" -ForegroundColor Green
Write-Host "[4/4] Iniciando backend (deja esta terminal abierta)..." -ForegroundColor Cyan
Set-Location "backend"
.\mvnw spring-boot:run
