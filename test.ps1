#########################################################
#                                                       #
#      Run test suite and generate coverage reports     #
#                                                       #
#########################################################

""
$avd = "PixelTest"
$image = "system-images;android-27;google_apis;x86"

# Check if emulator was provided as argument, set to default if not
if("$args" -ne "")
{
    $avd = "$args"
}

# Check if device exists in avdmanager, else
# create a new test AVD
if((avdmanager list avd | findstr /e "Name: $avd") -eq $null)
{
    Write-Host "Creating test AVD..." -Foreground Yellow
    ""
    
    echo no | avdmanager create avd -n $avd -k "$image" > $null
    
    # Check if create failed
    if ( -not $? )
    {
        ""
        Write-Host "Failed to create test AVD with package '$image'." -Foreground Red
        ""
        exit 1
    }

	cp config.ini $HOME/.android/avd/$avd.avd/

    Write-Host "Created AVD device: $avd" -Foreground Yellow
    ""
}

Write-Host "Using AVD device: $avd" -Foreground Yellow
""
Write-Host "=== Starting emulator in the background... ===" -Foreground Yellow
""

# Start emulator in background
Start-Job -ScriptBlock {emulator -avd $args[0]} -ArgumentList $avd > $null

# Wait for emulator before proceeding
Write-Host "Waiting for emulator to boot..." -ForegroundColor Yellow
adb wait-for-device
while((adb shell getprop sys.boot_completed) -ne "1")
{
    sleep 5
}
Write-Host "BOOT SUCCESSFUL" -Foreground Yellow

""
Write-Host "=== Starting build/test... ===" -Foreground Yellow

./gradlew clean jacocoTestReport

""
Write-Host "Shutting down emulator..." -Foreground Yellow

# Shut down emulator
adb -s emulator-5554 emu kill
adb kill-server

""


