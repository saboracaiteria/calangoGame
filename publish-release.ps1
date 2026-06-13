# Script to build APK locally and publish it to GitHub Releases
# Run this from the root directory of the project.

Write-Host "=============================================" -ForegroundColor Cyan
Write-Host "  Calango Run - Build & Publish to Releases  " -ForegroundColor Cyan
Write-Host "=============================================" -ForegroundColor Cyan
Write-Host ""

# Set JAVA_HOME to Android Studio JBR for correct JDK version
$env:JAVA_HOME = "C:\Program Files\Android\Android Studio\jbr"

# 1. Build APK locally
Write-Host "[1/5] Building Android APK locally..." -ForegroundColor Yellow
cd CalangoRun
.\gradlew.bat assembleDebug
if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: Gradle compilation failed!" -ForegroundColor Red
    cd ..
    exit 1
}
cd ..

# 2. Generate timestamp and paths
$timestamp = Get-Date -Format "yyyy-MM-dd_HH-mm"
$apkSource = "CalangoRun\app\build\outputs\apk\debug\app-debug.apk"
$apkDestName = "calango-debug-$timestamp.apk"

if (-Not (Test-Path $apkSource)) {
    Write-Host "ERROR: Compiled APK not found at $apkSource" -ForegroundColor Red
    exit 1
}

# 3. Clean up older APKs in the root to save space
Write-Host "[2/5] Cleaning up old APKs..." -ForegroundColor Yellow
Get-ChildItem -Filter "calango-debug-*.apk" | Remove-Item -Force -ErrorAction SilentlyContinue

# 4. Copy new APK to root
Write-Host "[3/5] Copying new APK to root as $apkDestName..." -ForegroundColor Yellow
Copy-Item $apkSource -Destination $apkDestName -Force

# 5. Git Commit and Push to Main
Write-Host "[4/5] Pushing changes to GitHub main branch..." -ForegroundColor Yellow
git add index.html
git add $apkDestName
git commit -m "build: local build $apkDestName"
git push origin main
if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: Failed to push to GitHub main branch!" -ForegroundColor Red
    exit 1
}

# 6. Create Tag and Push Tag to Trigger Release
Write-Host "[5/5] Creating and pushing tag to trigger GitHub Release..." -ForegroundColor Yellow
$tagName = "v-$timestamp"
git tag $tagName
git push origin $tagName

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "SUCCESS! APK successfully compiled and pushed." -ForegroundColor Green
    Write-Host "GitHub Release creation triggered for tag: $tagName" -ForegroundColor Green
    Write-Host "Users can download the APK from the GitHub Releases page." -ForegroundColor Green
    Write-Host ""
} else {
    Write-Host "ERROR: Failed to push git tag to GitHub!" -ForegroundColor Red
}
