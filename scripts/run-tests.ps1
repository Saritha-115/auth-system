Write-Host "=========================================" -ForegroundColor Cyan
Write-Host "Running All Tests for Auth System" -ForegroundColor Cyan
Write-Host "=========================================" -ForegroundColor Cyan

$failed = $false

Write-Host "`n📦 Testing Auth Service..." -ForegroundColor Yellow
Push-Location auth-service
mvn clean test
if ($LASTEXITCODE -eq 0) {
    Write-Host "✅ Auth Service PASSED" -ForegroundColor Green
} else {
    Write-Host "❌ Auth Service FAILED" -ForegroundColor Red
    $failed = $true
}
Pop-Location

Write-Host "`n📦 Testing User Service..." -ForegroundColor Yellow
Push-Location user-service
mvn clean test
if ($LASTEXITCODE -eq 0) {
    Write-Host "✅ User Service PASSED" -ForegroundColor Green
} else {
    Write-Host "❌ User Service FAILED" -ForegroundColor Red
    $failed = $true
}
Pop-Location

Write-Host "`n📦 Testing API Gateway..." -ForegroundColor Yellow
Push-Location api-gateway
mvn clean test
if ($LASTEXITCODE -eq 0) {
    Write-Host "✅ API Gateway PASSED" -ForegroundColor Green
} else {
    Write-Host "❌ API Gateway FAILED" -ForegroundColor Red
    $failed = $true
}
Pop-Location

Write-Host "`n=========================================" -ForegroundColor Cyan
Write-Host "TEST SUMMARY" -ForegroundColor Cyan
Write-Host "=========================================" -ForegroundColor Cyan

if ($failed) {
    Write-Host "❌ Some tests FAILED!" -ForegroundColor Red
    exit 1
} else {
    Write-Host "✅ ALL tests PASSED!" -ForegroundColor Green
    
    Write-Host "`n📊 Generating coverage reports..." -ForegroundColor Yellow
    Push-Location auth-service
    mvn jacoco:report -q
    Pop-Location
    
    Push-Location user-service
    mvn jacoco:report -q
    Pop-Location
    
    Write-Host "✅ Coverage reports generated" -ForegroundColor Green
    
    exit 0
}
