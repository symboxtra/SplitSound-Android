#!/bin/bash

#########################################################
#                                                       #
#  To use the ANDROID_HOME and path variables exported  #
#  by this script, run the script as '. ./install.sh'   #
#              or 'source ./install.sh'                 #
#                                                       #
#########################################################

RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[1;96m'
NC='\033[0m' # No color
echo

# Point ANDROID_HOME to local SDK
export ANDROID_HOME=$HOME/Android/Sdk
export ANDROID_NDK_HOME=$ANDROID_HOME/ndk-bundle # Export NDK location

# Emulator must have higher precedence than tools
export PATH=$ANDROID_HOME/tools:$ANDROID_HOME/tools/bin:$PATH # Add SDK tools to path
export PATH=$ANDROID_HOME/emulator:$ANDROID_HOME/emulator/bin:$ANDROID_HOME/platform-tools/:$ANDROID_HOME/platform-tools:$PATH

# Check for already installed files
if [ -d "$ANDROID_HOME" ]; then
    echo -e "${BLUE}Folder '$HOME/Android/Sdk' already exists."
    echo -e "${BLUE}Exported environment variables with reference to '$HOME/Android/Sdk'.${NC}"
    echo
    echo -e "To install required SDK Packages, run 'source ./install-sdk-packages'."
    echo
    return 0 2> /dev/null # If sourced
    exit 0 # If run as script
fi

echo -e "${YELLOW}Downloading SDK Tools...${NC}"
echo

mkdir -p $ANDROID_HOME

curl -L https://dl.google.com/android/repository/sdk-tools-linux-4333796.zip -O

echo -e "${YELLOW}Unzipping SDK Tools...${NC}"
echo

unzip -oq sdk-tools-linux-4333796.zip -d $ANDROID_HOME
rm ./sdk-tools-linux-4333796.zip

# Make bin executable by all
(cd $ANDROID_HOME/tools/bin && chmod +x *)

echo -e "${YELLOW}Installed SDK Tools at: ${NC}${ANDROID_HOME}"
echo

echo -e "${YELLOW}Using sdkmanager found at:${NC} `which sdkmanager`"
echo -e "${YELLOW}Version:${NC} `sdkmanager --version`"
echo

# Install packages (prompts first)
source ./install-sdk-packages.sh
