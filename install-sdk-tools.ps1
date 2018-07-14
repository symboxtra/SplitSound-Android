#########################################################
#                                                       #
#	  Setup Android tools and packages on Windows		#
#		                                                #
#########################################################

""

# Set ANDROID environment variables
[Environment]::SetEnvironmentVariable("ANDROID_HOME", "$env:LOCALAPPDATA/Android/sdk", "Machine")
[Environment]::SetEnvironmentVariable("ANDROID_NDK_HOME", "$env:ANDROID_HOME/ndk-bundle", "Machine")
[Environment]::SetEnvironmentvariable("Path", "$env:Path;$env:ANDROID_HOME/tools;$env:ANDROID_HOME/tools/bin", "Machine")
[Environment]::SetEnvironmentVariable("Path", "$env:Path;$env:ANDROID_HOME/emulator/bin;$env:ANDROID_HOME", "Machine")

# Check if Path is already defined
if(-NOT (Test-Path -path $env:ANDROID_HOME))
{
	Write-Host "Folder '$env:LOCALAPPDATA/Android/sdk' already exists." -Foreground Cyan
	Write-Host "Exported environment variables with reference to 'LOCALAPPDATA'." -Foreground Cyan
	""
	Write-Host "To install required SDK packages, run './install-sdk-packages'"
	""

	$host.SetShouldExit(0)
}

Write-Host "Downloading SDK Tools" -Foreground Yellow
""

mkdir -p $env:ANDROID_HOME

Invoke-WebRequest -Uri 'https://dl.google.com/android/repository/sdk-tools-windows-4333796.zip' -OutFile sdk-tools-windows.zip

Write-Host "Unzipping SDK Tools..." -Foreground Yellow
""

Expand-Archive 'sdk-tools-windows.zip' -DestinationPath $env:ANDROID_HOME
rm ./sdk-tools-windows.zip

Write-Host ("Installed SDK Tools at: " + $env:ANDROID_HOME) -Foreground Yellow
""

Write-Host ("Using sdkmanager version: "+ ${sdkmanager --version}) -Foreground Yellow

# Install SDK packages
./install-sdk-packages.ps1
