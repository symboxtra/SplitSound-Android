#!/bin/bash

#########################################################
#                                                       #
#      Run test suite and generate coverage reports     #
#                                                       #
#########################################################

RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No color
echo

avd="PixelTest" # Default AVD
image="system-images;android-27;google_apis;x86"
headless=false

# Parse args
while getopts ":a:sh" opt; do
    case $opt in
        a)
            avd="$OPTARG"
            ;;
        s)
            headless=true
            echo "${YELLOW}Running in headless mode. Remove -s to run non-headless.${NC}"
            echo
            ;;
        h)
            echo "Usage: test.sh [-a=avd device name] [-s] [-h]"
            echo "-s    Run emulator headless."
            exit 1
            ;;
    esac
done

# Check that emulator was provided as an argument
if [ -z `avdmanager list avd | grep "Name: $avd$"` ]; then
    echo -e "${YELLOW}Creating test AVD...${NC}"
    echo

    echo no | avdmanager create avd -n "$avd" -k "$image" > /dev/null

    # Check if create failed
    if [ $? != 0 ]; then
        echo
        echo -e "${RED}Failed to create test AVD with package '$image'.${NC}"
        echo
        exit 1
    fi

	echo -e "${YELLOW}Created AVD device: $avd${NC}"
	echo

fi

# Check that device exists in avdmanager
if [ -z `avdmanager list avd | grep "Name: $avd$"` ]; then
    echo "$avd was not a valid device in avdmanager."
    echo "Valid devices are: "
    avdmanager list avd | grep "Name:" --color=never
    echo
    exit 1
fi

echo -e "${YELLOW}Using AVD device: $avd"
echo
echo -e "${YELLOW}=== Starting emulator in background... ===${NC}"
echo

# Start emulator
if $headless; then
    emulator -avd "$avd" -no-skin -no-audio -no-window -gpu off -no-accel -no-boot-anim &
else
    emulator -avd "$avd" &
fi

# Wait for emulator
echo -e "${YELLOW}Waiting for emulator to boot...${NC}"
adb wait-for-device
while [ `adb shell getprop sys.boot_completed | tr -d '\r'` != "1" ];
do
    sleep 2
done
echo -e "${YELLOW}BOOT SUCCESSFUL${NC}"

echo
echo -e "${YELLOW}=== Starting build/test... ===${NC}"
echo

# Run test and generate coverage reports
chmod +x ./gradlew
./gradlew clean jacocoTestReport

echo
echo -e "${YELLOW}Shutting down emulator...${NC}"

# Shut down emulator
adb -s emulator-5554 emu kill
adb kill-server

echo

