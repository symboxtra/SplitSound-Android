#########################################################
#                                                       #
#  Install necessary Android packages to run project	#
#                                                       #
#########################################################

""

# Prompt package download
Write-Host "Are you sure you want to install ALL needed packages for this project?"
$sure = Read-Host -Prompt "This may take awhile....		[Y/n]"
""

# Check for user response
$sure=$sure -replace "^[Yy]$", 'y' 
if($sure -ne "y")
{
	exit 0
}

# Check for SDK Home
if(-NOT (Test-Path -path $env:ANDROID_HOME))
{
	Write-Host "ANDROID_HOME not defined" -Foreground Red
	""
	Write-Host "Assuming SDK is at $env:LOCALAPPDATA\Android\sdk" -Foreground Yellow
	""
	[Environment]::SetEnvironmentVariable("ANDROID_HOME", "$env:LOCALAPPDATA\Android\sdk", "Machine")
}

# Constants
$BUILD_TOOLS_VERSION="27.0.3"
$ANDROID_TARGET="android-27"
$ANDROID_ABI="x86"
$CMAKE_VERSION="3.6.4111459"

# Packages to be installed
$PACKAGES=@("ndk-bundle", "platform-tools", "build-tools;$BUILD_TOOLS_VERSION", "platforms;$ANDROID_TARGET", "system-images;$ANDROID_TARGET;google_apis;$ANDROID_ABI", "extras;android;m2repository", "cmake;$CMAKE_VERSION")

Write-Host "======== Installing SDK Packages ========" -Foreground Yellow
""

# Install all packages
foreach($package in $PACKAGES)
{
	Write-Host "Installing $package..."

	sdkmanager $package

	if($LastExitCode -ne 0)
	{
		exit $LastExitCode
	}

	""
	Write-Host "Installed $package" -Foreground Yellow
	""
}

Write-Host "Installation successful!" -Foreground Cyan
""
