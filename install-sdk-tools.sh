#!/bin/bash

#########################################################
#                                                       #
#  To use the ANDROID_HOME and path variables exported  #
#  by this script, run the script as '. ./instal.sh'    #
#              or 'source ./install.sh'                 #
#                                                       #
#########################################################

RED='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No color

# Point ANDROID_HOME to local SDK
export ANDROID_HOME=$PWD/Android/Sdk
export ANDROID_NDK_HOME=$ANDROID_HOME/ndk-bundle

# Emulator must have higher precedence than tools
export PATH=$ANDROID_HOME/emulator:$ANDROID_HOME/emulator/bin:$ANDROID_HOME/platform-tools:$ANDROID_HOME/tools:$ANDROID_HOME/tools/bin:$PATH 

# Check for already installed files
if [ -d "Android/Sdk" ]; then
    echo
    echo -e "${YELLOW}Folder 'Android/Sdk' already exists."
    echo -e "Exported environment variables with reference to 'Android/Sdk'.${NC}"
    echo
    return 0
fi

echo
echo -e "${YELLOW}Downloading SDK Tools...${NC}"
echo

mkdir -p $ANDROID_HOME

curl -L https://dl.google.com/android/repository/sdk-tools-linux-4333796.zip -O

echo -e "${YELLOW}Unzipping SDK Tools...${NC}"
echo

unzip -oq sdk-tools-linux-4333796.zip -d $ANDROID_HOME
rm sdk-tools-linux-4333796.zip

echo
echo -e "${YELLOW}Installed SDK Tools at: ${NC}${ANDROID_HOME}"
echo

echo -e "${YELLOW}Using sdkmanager found at:${NC} `which sdkmanager`"
echo -e "${YELLOW}Version:${NC} `sdkmanager --version`"
echo
