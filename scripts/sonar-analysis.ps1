Write-Host "=========================================" -ForegroundColor Cyan
Write-Host "SonarCloud Analysis" -ForegroundColor Cyan
Write-Host "=========================================" -ForegroundColor Cyan

# Set project root
$ProjectRoot = "C:\Users\USER\git\auth-system"
Set-Location $ProjectRoot

# Check token
if (-not (Test-Path ".sonar-token")) {
    Write-Host "`n❌ No token found!" -ForegroundColor Red
    Write-Host "Get token from: https://sonarcloud.io/account/security/" -ForegroundColor Yellow
    exit 1
}

$env:SONAR_TOKEN = (Get-Content .sonar-token -Raw).Trim()
Write-Host "`n✅ SonarCloud token loaded" -ForegroundColor Green

# Generate coverage reports
Write-Host "`n📊 Generating coverage reports..." -ForegroundColor Yellow
Push-Location auth-service
mvn jacoco:report -q
Pop-Location
Push-Location user-service
mvn jacoco:report -q
Pop-Location
Write-Host "✅ Coverage reports ready" -ForegroundColor Green

# Run SonarCloud analysis
Write-Host "`n🔍 Running SonarCloud analysis..." -ForegroundColor Yellow
Write-Host "This may take 2-3 minutes..." -ForegroundColor Cyan

mvn sonar:sonar

if ($LASTEXITCODE -eq 0) {
    Write-Host "`n=========================================" -ForegroundColor Green
    Write-Host "✅ ANALYSIS COMPLETE!" -ForegroundColor Green
    Write-Host "=========================================" -ForegroundColor Green
    Write-Host "`n📊 View results: https://sonarcloud.io/dashboard?id=Saritha-115_auth-system" -ForegroundColor Cyan
} else {
    Write-Host "`n❌ Analysis failed!" -ForegroundColor Red
    exit 1
}
