#!/bin/bash

#########################################################
#                                                       #
#      Run test suite and generate coverage reports     #
#                                                       #
#########################################################

RED='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No color
echo

# Parse args
avd=none
headless=false
while getopts ":a:h" opt; do
    case $opt in
        a)
            avd=$OPTARG
            ;;
        t)
            headless=true
            echo "${YELLOW}Running in headless mode. Remove -h to run non-headless."
            echo
            ;;
    esac
done        

# Check that emulator was provided as an argument
if [ -z $avd ]; then
    echo "No emulator name provided."
    echo "Usage: test.sh -a emulator-name [-t]"
    exit 1
fi

# Check that device exists in avdmanager
if [ -z `avdmanager list avd | grep "Name: $avd"` ]; then
    echo "$avd was not a valid device in avdmanager."
    echo "Valid devices are: "
    avdmanager list avd | grep "Name:" --color=never
    echo
    exit 1
fi

echo -e "${YELLOW}Using AVD device: $avd"
echo -e "Starting emulator in background...${NC}"
echo

# Start emulator
if $headless; then
    emulator -avd $avd -no-skin -no-audio -no-window -gpu off -no-accel -no-boot-anim &
else
    emulator -avd $avd &
fi

# Wait for emulator
adb wait-for-device

while [ `adb shell getprop sys.boot_completed | tr -d '\r'` != "1" ]; 
do
    sleep 2
    echo -e "${YELLOW}Waiting for emulator to boot...${NC}"
done

# Unlock old devices -- might not be needed
adb shell input keyevent 82 &

echo
echo -e "${YELLOW}Starting build/test...${NC}"
echo

# Run test and generate coverage reports
chmod +x ./gradlew
./gradlew clean test jacocoTestReport

# Remove default coverage report
# Only need merged Jacoco report
rm -r ./mobile/build/reports/coverage
