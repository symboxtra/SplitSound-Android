""
$var = "PixelTest"
$image = "system-images;android-27;google_apis;x86"

# Check if emulator was provided as argument, set to default if not
if("$args" -ne "")
{
	$var = "$args"
}

# Check if device exists in avdmanager, else
# create a new test AVD
if((avdmanager list avd | find "Name: $var") -eq $null)
{
	Write-Host "Creating test AVD..." -Foreground Yellow
	""
	echo no | avdmanager create avd -n $var -k $image

	# Check if create failed
	if ( -not $? )
    {
        Write-Host "Failed to create test AVD." -Foreground Red
		""
		# Write-Host "Please ensure: '$image' is installed."
		# ""
        exit 1
    }

	Write-Host "Created AVD device: $var" -Foreground Green

}

echo "Using AVD device: $var"
""
echo "=== Starting emulator in the background ==="

# Start emulator in background
Start-Job -ScriptBlock {emulator -avd $var}

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
