#!/bin/bash

#########################################################
#                                                       #
#      Run test suite and generate coverage reports     #
#                                                       #
#########################################################

RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[1;96m'
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
            echo -e "${BLUE}Running in headless mode. Remove -s to run non-headless.${NC}"
            echo
            ;;
        h)
            echo "Usage: test.sh [-a=avd device name] [-s] [-h]"
            echo "    -s    Run emulator headless."
            echo
            exit 1
            ;;
    esac
done

# Check that emulator was provided as an argument
result=`avdmanager list avd | grep "Name: $avd$"`
if [ -z "$result" ]; then
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

	cp config.ini $HOME/.android/avd/$avd.avd/

	echo -e "${YELLOW}Created AVD device: $avd${NC}"
	echo

fi

# Check that device exists in avdmanager
result=`avdmanager list avd | grep "Name: $avd$"`
if [ -z "$result" ]; then
    echo "$avd was not a valid device in avdmanager."
    echo "Valid devices are: "
    avdmanager list avd | grep "Name:" --color=never
    echo
    exit 1
fi

echo -e "${YELLOW}Using AVD device: $avd${NC}"
echo
echo -e "${YELLOW}=== Starting emulator in background... ===${NC}"
echo

echo
echo -e "${YELLOW}=== Starting build/test... ===${NC}"
echo

# Run test and generate coverage reports
error=0
./gradlew clean jacocoTestReport
error=$?

# Exit with error code from build
exit $error
