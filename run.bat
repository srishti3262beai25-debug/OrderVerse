@echo off
javac -cp "lib/*" -d bin src\\com\\restaurant\\*.java
if errorlevel 1 (
    pause
    exit /b
)
java -cp "bin;lib/*" com.restaurant.Main
pause
