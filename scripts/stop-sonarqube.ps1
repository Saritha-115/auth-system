Write-Host "=========================================" -ForegroundColor Cyan
Write-Host "Stopping SonarQube" -ForegroundColor Cyan
Write-Host "=========================================" -ForegroundColor Cyan

docker-compose -f docker/docker-compose-sonar.yml down

Write-Host "✅ SonarQube stopped" -ForegroundColor Green