$var = "PixelTest"

# Check if emulator was provided as argument, set to default if not
if("$args" -ne "")
{
	$var = "$args"
}

# Check if device exists in avdmanager, else
# create a new test AVD
if((avdmanager list avd | find "Name: $var") -eq $null)
{
	echo "Creating test AVD..."
	echo no | avdmanager create avd -n $var -k "system-images;android-25;google_apis;x86">$null
}

echo "Using AVD device: $var"
""
echo "=== Starting emulator in the background ==="

# Start emulator in background
Start-Job -ScriptBlock {emulator -avd PixelTest} > $null

# Wait for emulator before proceeding
Write-Host "Waiting for emulator to boot..." -ForegroundColor Yellow
while((adb shell getprop sys.boot_completed) -ne "1")
{
	sleep 5
}
Write-Host "BOOT SUCCESSFUL" -Foreground Green

""
echo "=== Starting build/test... ==="

./gradlew clean jacocoTestReport
