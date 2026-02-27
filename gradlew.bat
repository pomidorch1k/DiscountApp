@rem Gradle startup script for Windows
@if "%DEBUG%"=="" @echo off

@rem Find project root
set DIRNAME=%~dp0
if "%DIRNAME%"=="" set DIRNAME=.
cd /d "%DIRNAME%"

@rem Use Gradle wrapper
set WRAPPER_JAR=%~dp0gradle\wrapper\gradle-wrapper.jar
if exist "%WRAPPER_JAR%" (
    java -jar "%WRAPPER_JAR%" %*
) else (
    echo Gradle wrapper not found. Please open project in Android Studio to generate it.
    echo Or run: gradle wrapper
    exit /b 1
)
