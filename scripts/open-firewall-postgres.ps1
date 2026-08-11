$ErrorActionPreference = "Stop"

$ruleName = "AssistentIA PostgreSQL 5432"

if (-not (Get-NetFirewallRule -DisplayName $ruleName -ErrorAction SilentlyContinue)) {
    New-NetFirewallRule -DisplayName $ruleName -Direction Inbound -Protocol TCP -LocalPort 5432 -Action Allow | Out-Null
    Write-Host "Regla creada: $ruleName" -ForegroundColor Green
} else {
    Write-Host "La regla ya existe: $ruleName" -ForegroundColor Yellow
}
