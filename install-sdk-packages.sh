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

# Confirm packages download
echo -e "${NC}Are you sure you want to install ALL needed packages for this project?${NC}"
read -p "This may take awhile...       [Y/n]: "
echo

# Exit if no
if [[ ! $REPLY =~ ^[Yy]$ ]]
then
    return 0 2> /dev/null # If sourced
    exit 0 # If run as script
fi

# Check for SDK Home
if [ -z $ANDROID_HOME ]; then
    echo -e "${RED}ANDROID_HOME was not defined.${NC}"
    echo
    echo -e "${YELLOW}Assuming SDK is at $HOME/Android/Sdk."
    echo
    ANDROID_HOME=$HOME/Android/Sdk
fi

# Constants
BUILD_TOOLS_VERSION=27.0.3
ANDROID_TARGET=android-27
ANDROID_ABI=x86
CMAKE_VERSION=3.6.4111459

# Define packages to be installed
PACKAGES=("ndk-bundle" "platform-tools" "build-tools;${BUILD_TOOLS_VERSION}" "platforms;${ANDROID_TARGET}" "system-images;${ANDROID_TARGET};google_apis;${ANDROID_ABI}" "extras;android;m2repository" "cmake;${CMAKE_VERSION}")

echo -e "${YELLOW}======== Installing SDK Packages ========"
echo

# Install all packages
for package in ${PACKAGES[@]}; do

    echo -e "${NC}Installing ${package}...${NC}"

    echo y | sdkmanager "${package}"

     # Check if install failed
    if [ $? != 0 ]; then
        echo
        echo -e "${RED}Failed to install $package.${NC}"
        echo

        return 1 2> /dev/null # If sourced
        exit 1 # If run as script
    fi

    echo
    echo -e "${YELLOW}Installed ${package}.${NC}"
    echo
done

echo -e "${BLUE}Installation successful!${NC}"
echo
