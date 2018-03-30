@echo off
echo.

REM -- Check that emulator was provided as an argument
IF "%~1" == "" (echo No emulator name provided. && echo Usage: test.bat emulator-name && goto fail)

REM -- Make sure device exists in avdmanager
avdmanager list avd | find "Name: %1" >nul 2>&1 || (echo Emulator was not a valid device in avdmanager. && echo. && avdmanager list avd && goto l)

echo Using AVD device: %1
echo Starting emulator in background...
echo.

REM -- Start emulator in background
START /B emulator -avd %1

REM -- Wait for emulator before proceeding
adb wait-for-device

echo Starting build/test...
echo.

gradlew clean jacocoTestReport

:fail
