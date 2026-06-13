Write-Host "=========================================" -ForegroundColor Cyan
Write-Host "Running All Tests for Auth System" -ForegroundColor Cyan
Write-Host "=========================================" -ForegroundColor Cyan

$failed = $false
$results = @()

# Test Auth Service
Write-Host "`n?? Testing Auth Service..." -ForegroundColor Yellow
Push-Location auth-service
$testResult = mvn clean test 2>&1
if ($LASTEXITCODE -eq 0) {
    Write-Host "? Auth Service PASSED" -ForegroundColor Green
    $results += "? Auth Service: PASSED"
} else {
    Write-Host "? Auth Service FAILED" -ForegroundColor Red
    $results += "? Auth Service: FAILED"
    $failed = $true
}
Pop-Location

# Test User Service
Write-Host "`n?? Testing User Service..." -ForegroundColor Yellow
Push-Location user-service
$testResult = mvn clean test 2>&1
if ($LASTEXITCODE -eq 0) {
    Write-Host "? User Service PASSED" -ForegroundColor Green
    $results += "? User Service: PASSED"
} else {
    Write-Host "? User Service FAILED" -ForegroundColor Red
    $results += "? User Service: FAILED"
    $failed = $true
}
Pop-Location

# Test API Gateway
Write-Host "`n?? Testing API Gateway..." -ForegroundColor Yellow
Push-Location api-gateway
$testResult = mvn clean test 2>&1
if ($LASTEXITCODE -eq 0) {
    Write-Host "? API Gateway PASSED" -ForegroundColor Green
    $results += "? API Gateway: PASSED"
} else {
    Write-Host "? API Gateway FAILED" -ForegroundColor Red
    $results += "? API Gateway: FAILED"
    $failed = $true
}
Pop-Location

# Summary
Write-Host "`n=========================================" -ForegroundColor Cyan
Write-Host "TEST SUMMARY" -ForegroundColor Cyan
Write-Host "=========================================" -ForegroundColor Cyan
foreach ($result in $results) {
    Write-Host $result
}
Write-Host "=========================================" -ForegroundColor Cyan

if ($failed) {
    Write-Host "`n? Some tests FAILED!" -ForegroundColor Red
    exit 1
} else {
    Write-Host "`n? ALL tests PASSED!" -ForegroundColor Green
    
    # Generate coverage reports
    Write-Host "`n?? Generating coverage reports..." -ForegroundColor Yellow
    Push-Location auth-service
    mvn jacoco:report -q
    Pop-Location
    Push-Location user-service
    mvn jacoco:report -q
    Pop-Location
    Write-Host "? Coverage reports generated" -ForegroundColor Green
    
    exit 0
}
