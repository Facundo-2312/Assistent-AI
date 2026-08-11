$ErrorActionPreference = "Stop"
Set-Location "$PSScriptRoot\.."

if (-not (Test-Path dist)) {
    New-Item -ItemType Directory -Path dist | Out-Null
}

$zipPath = "dist\assistentia-team-share.zip"
if (Test-Path $zipPath) {
    Remove-Item $zipPath -Force
}

Compress-Archive -Path backend,frontend,docs,docker-compose.yml,README.md,scripts -DestinationPath $zipPath -CompressionLevel Optimal
Write-Host "ZIP generado: $zipPath" -ForegroundColor Green
