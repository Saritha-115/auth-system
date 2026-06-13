Write-Host "=========================================" -ForegroundColor Cyan
Write-Host "Starting SonarQube" -ForegroundColor Cyan
Write-Host "=========================================" -ForegroundColor Cyan

docker-compose -f docker/docker-compose-sonar.yml up -d
Write-Host "`n? Waiting for SonarQube (60 seconds)..." -ForegroundColor Yellow
Start-Sleep -Seconds 60
Write-Host "? SonarQube started at http://localhost:9000" -ForegroundColor Green
