Write-Host "=========================================" -ForegroundColor Cyan
Write-Host "Cleaning All Services" -ForegroundColor Cyan
Write-Host "=========================================" -ForegroundColor Cyan

Write-Host "`n🧹 Cleaning Maven targets..." -ForegroundColor Yellow

# Clean Auth Service
Write-Host "  Cleaning auth-service..." -ForegroundColor Gray
Push-Location auth-service
mvn clean
Pop-Location

# Clean User Service
Write-Host "  Cleaning user-service..." -ForegroundColor Gray
Push-Location user-service
mvn clean
Pop-Location

# Clean API Gateway
Write-Host "  Cleaning api-gateway..." -ForegroundColor Gray
Push-Location api-gateway
mvn clean
Pop-Location

Write-Host "`n✅ All Maven targets cleaned!" -ForegroundColor Green

# Optional: Clean Docker volumes
Write-Host "`n📦 Docker cleanup:" -ForegroundColor Yellow
$response = Read-Host "Do you want to clean SonarQube Docker volumes? (y/N)"
if ($response -eq 'y' -or $response -eq 'Y') {
    Write-Host "  Cleaning Docker volumes..." -ForegroundColor Gray
    docker-compose -f docker/docker-compose-sonar.yml down -v 2>&1 | Out-Null
    Write-Host "✅ Docker volumes cleaned!" -ForegroundColor Green
} else {
    Write-Host "⏭️ Skipping Docker cleanup" -ForegroundColor Gray
}

Write-Host "`n=========================================" -ForegroundColor Green
Write-Host "✅ Clean completed successfully!" -ForegroundColor Green
Write-Host "=========================================" -ForegroundColor Cyan
