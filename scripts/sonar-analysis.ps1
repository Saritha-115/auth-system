Write-Host "=========================================" -ForegroundColor Cyan
Write-Host "SonarQube Analysis" -ForegroundColor Cyan
Write-Host "=========================================" -ForegroundColor Cyan

if (-not (Test-Path ".sonar-token")) {
    Write-Host "`n? No token found!" -ForegroundColor Red
    Write-Host "Get token from: http://localhost:9000 ? My Account ? Security" -ForegroundColor Yellow
    exit 1
}

$env:SONAR_TOKEN = (Get-Content .sonar-token -Raw).Trim()
Write-Host "`n? Token loaded" -ForegroundColor Green

# Generate coverage reports
Write-Host "`n?? Generating coverage reports..." -ForegroundColor Yellow
Push-Location auth-service
mvn jacoco:report -q
Pop-Location
Push-Location user-service
mvn jacoco:report -q
Pop-Location
Write-Host "? Coverage reports ready" -ForegroundColor Green

# Run analysis
Write-Host "`n?? Running SonarQube analysis..." -ForegroundColor Yellow
Push-Location auth-service
mvn sonar:sonar "-Dsonar.host.url=http://localhost:9000" "-Dsonar.login=$env:SONAR_TOKEN" "-Dsonar.projectKey=auth-service"
Pop-Location

Push-Location user-service
mvn sonar:sonar "-Dsonar.host.url=http://localhost:9000" "-Dsonar.login=$env:SONAR_TOKEN" "-Dsonar.projectKey=user-service"
Pop-Location

Write-Host "`n? Analysis complete! View at: http://localhost:9000" -ForegroundColor Green
