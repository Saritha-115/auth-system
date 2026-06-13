Write-Host "=========================================" -ForegroundColor Cyan
Write-Host "Building All Services" -ForegroundColor Cyan
Write-Host "=========================================" -ForegroundColor Cyan

Write-Host "`n??? Building Auth Service..." -ForegroundColor Yellow
Push-Location auth-service
mvn clean package -DskipTests
Pop-Location

Write-Host "`n??? Building User Service..." -ForegroundColor Yellow
Push-Location user-service
mvn clean package -DskipTests
Pop-Location

Write-Host "`n??? Building API Gateway..." -ForegroundColor Yellow
Push-Location api-gateway
mvn clean package -DskipTests
Pop-Location

Write-Host "`n? All services built successfully!" -ForegroundColor Green
