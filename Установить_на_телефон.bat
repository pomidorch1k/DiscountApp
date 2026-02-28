@echo off
cd /d "%~dp0"

if not exist "gradle\wrapper\gradle-wrapper.jar" (
    echo Gradle wrapper not found!
    echo.
    echo To fix: Open Android Studio, create NEW empty project,
    echo then copy folder gradle\wrapper from new project
    echo to this project folder. Or open this project in Android
    echo Studio and wait for sync - it may create the wrapper.
    echo.
    pause
    exit /b 1
)

echo Building APK...
call gradlew.bat assembleDebug -q
if errorlevel 1 (
    echo Build failed!
    pause
    exit /b 1
)

set ADB=%LOCALAPPDATA%\Android\Sdk\platform-tools\adb.exe
if not exist "%ADB%" set ADB=%ANDROID_HOME%\platform-tools\adb.exe

if not exist "%ADB%" (
    echo ADB not found. Install Android SDK.
    pause
    exit /b 1
)

"%ADB%" devices | findstr /r "device$" >nul 2>&1
if errorlevel 1 (
    echo Connect phone via USB and enable USB debugging.
    pause
    exit /b 1
)

echo Installing to phone...
"%ADB%" install -r app\build\outputs\apk\debug\app-debug.apk
if errorlevel 1 (
    echo Install failed!
    pause
    exit /b 1
)

echo Done! App installed on phone.
pause
