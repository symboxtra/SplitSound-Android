#########################################################
#                                                       #
#      Setup Android tools and packages on Windows	#
#		                                        #
#########################################################

""

# Set ANDROID environment variables

$env:ANDROID_HOME="$env:LOCALAPPDATA\Android\sdk"
[Environment]::SetEnvironmentVariable("ANDROID_HOME", $env:ANDROID_HOME, [EnvironmentVariableTarget]::Machine)
[Environment]::SetEnvironmentVariable("ANDROID_NDK_HOME", "$env:ANDROID_HOME\ndk-bundle", [EnvironmentVariableTarget]::Machine)
[Environment]::SetEnvironmentvariable("Path", $env:Path + ";$env:ANDROID_HOME\platform-tools;$env:ANDROID_HOME\emulator;$env:ANDROID_HOME\emulator\bin;$env:ANDROID_HOME\tools;$env:ANDROID_HOME\tools\bin;$env:ANDROID_HOME", [EnvironmentVariableTarget]::Machine)

$env:Path=[Environment]::GetEnvironmentVariable("Path", [EnvironmentVariableTarget]::Machine) + ";" + [Environment]::GetEnvironmentVariable("Path", [EnvironmentVariableTarget]::Machine)

# Check if Path is already defined
if(Test-Path -path $env:ANDROID_HOME)
{
	Write-Host "Folder '$env:LOCALAPPDATA\Android\sdk' already exists." -Foreground Cyan
	Write-Host "Exported environment variables with reference to 'LOCALAPPDATA'." -Foreground Cyan
	""
	Write-Host "To install required SDK packages, run '.\install-sdk-packages'"
	""

	exit 0
}

Write-Host "Downloading SDK Tools..." -Foreground Yellow
""

mkdir -p $env:ANDROID_HOME > $null

$destination= $PSScriptRoot + '\sdk-tools-windows.zip'
(New-Object System.Net.WebClient).DownloadFile('https://dl.google.com/android/repository/sdk-tools-windows-4333796.zip', $destination)

Write-Host "Unzipping SDK Tools..." -Foreground Yellow
""

Expand-Archive 'sdk-tools-windows.zip' -DestinationPath $env:ANDROID_HOME
rm ./sdk-tools-windows.zip

Write-Host ("Installed SDK Tools at: " + $env:ANDROID_HOME) -Foreground Yellow
""

Write-Host ("Using sdkmanager version: "+ $(sdkmanager --version)) -Foreground Yellow

# Install necessary SDK packages
./install-sdk-packages.ps1
