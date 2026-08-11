param(
    [string]$ServerIp = "192.168.1.41",
    [string]$DbUser = "postgres",
    [string]$DbPassword = "postgres"
)

$ErrorActionPreference = "Stop"
Set-Location "$PSScriptRoot\..\backend"

$env:DATABASE_URL = "jdbc:postgresql://$ServerIp`:5432/assistentia"
$env:DATABASE_USERNAME = $DbUser
$env:DATABASE_PASSWORD = $DbPassword
$env:FLYWAY_ENABLED = "false"

Write-Host "Cliente apuntando a servidor principal $ServerIp" -ForegroundColor Green
Write-Host "Iniciando aplicacion..." -ForegroundColor Cyan
.\mvnw spring-boot:run
