@echo off
title Vosiq EduGames - IntelliJ Runner
echo ============================================
echo   VOSIQ INTERNATIONAL SCHOOL - EDUGAMES
echo ============================================
echo.
echo [1] IntelliJ IDEA da ochish
echo [2] Maven bilan ishga tushirish
echo [3] JAR yaratish
echo.
set /p choice=Tanlang (1/2/3): 

if "%choice%"=="1" (
    echo IntelliJ IDEA ochilmoqda...
    start "" "C:\Program Files\JetBrains\IntelliJ IDEA 2025.2\bin\idea64.exe" "%~dp0"
)

if "%choice%"=="2" (
    echo Maven bilan ishga tushirilmoqda...
    cd /d "%~dp0"
    mvn javafx:run
)

if "%choice%"=="3" (
    echo JAR yaratilmoqda...
    cd /d "%~dp0"
    mvn clean package
    echo.
    echo JAR tayyor: target\VosiqEduGames-jar-with-dependencies.jar
    echo Ishga tushirish: java -jar target\VosiqEduGames-jar-with-dependencies.jar
    pause
)
