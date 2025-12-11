@echo off
REM Parking Reservation App - Start Script for Windows
REM This script starts the backend application with default development settings

echo ==========================================
echo Parking Reservation App - Backend Startup
echo ==========================================
echo.

REM Set default environment variables if not already set
if not defined JWT_SECRET set JWT_SECRET=my-super-secret-jwt-key-with-at-least-32-characters-for-security
if not defined DB_URL set DB_URL=jdbc:postgresql://localhost:5432/auth_db
if not defined DB_USERNAME set DB_USERNAME=postgres
if not defined DB_PASSWORD set DB_PASSWORD=postgres
if not defined SERVER_PORT set SERVER_PORT=8080
if not defined MAIL_ENABLED set MAIL_ENABLED=false

echo Environment Configuration:
echo   Database URL: %DB_URL%
echo   Database User: %DB_USERNAME%
echo   Server Port: %SERVER_PORT%
echo   Mail Enabled: %MAIL_ENABLED%
echo.

REM Build the application
echo Building the application with Maven...
echo.
call mvn clean package -DskipTests

if errorlevel 1 (
    echo.
    echo Build failed! Please check the error messages above.
    pause
    exit /b 1
)

echo.
echo Build successful!
echo.

REM Find the JAR file
for /f "delims=" %%i in ('dir /b /s target\*.jar ^| findstr /v "sources.jar" ^| findstr /v "javadoc.jar"') do set JAR_FILE=%%i

if not defined JAR_FILE (
    echo Could not find the JAR file in target directory
    pause
    exit /b 1
)

echo Starting the application...
echo JAR file: %JAR_FILE%
echo.
echo ==========================================
echo Application is starting...
echo Press Ctrl+C to stop the application
echo ==========================================
echo.

REM Start the application
java -jar "%JAR_FILE%"
