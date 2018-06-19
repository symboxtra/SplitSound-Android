[![Jenkins Server Status](https://img.shields.io/badge/dynamic/json.svg?label=Jenkins%20Server&url=http%3A%2F%2Fsymboxtra.tk%2Fjenkins%2F/status_badge.php&query=status&colorB=0b7cbd)](http://jenkins.symboxtra.tk "Jenkins Server Status")
[![](http://jenkins.symboxtra.tk/buildStatus/icon?job=SplitSound-Android)](http://jenkins.symboxtra.tk/job/SplitSound-Android "Jenkins Build Status")
[![Travis Build Status](https://travis-ci.org/symboxtra/SplitSound-Android.svg?branch=master)](https://travis-ci.org/symboxtra/SplitSound-Android/builds "Travis Build Status (unit tests only)")
[![Coverage Status](https://codecov.io/gh/symboxtra/SplitSound-Android/branch/master/graph/badge.svg)](https://codecov.io/gh/symboxtra/SplitSound-Android)
[![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](https://www.opensource.org/licenses/GPL-3.0)

# Contributing to SplitSound-Android #

**Project status**: Early development

We welcome contributions from outside collaborators. Feel free to fork and open a PR!

## Branching ##

Every feature/line of development should have it's own branch. 
Unless you know what you're doing, this branch should typically be a direct child of the `master` branch (see example below).
Releases accumulate in release branches before being merged into master.

Merging to master requires passing at least Jenkins CI (see [CI](#continuous-integration)) and a review from at least one member of the team.

#### Example: ####
```
git checkout master                     # Switch to the master branch
git pull origin master                  # Pull down latest changes
git checkout -b add-crazy-new-feature   # Create your new branch
                                        # do werk
```

## Development ##

Building the Android application does not require installation of Android Studio. 
For active development, however, Android studio is the recommended environment.

## Android Studio ##

1. Open the project with Android Studio and use the build button to build the application.
2. Accept any dialogs that prompt for updates or dependence installs.
3. Use the run button to run the application on a device or emulator.

Before commiting, make sure Android Studio hasn't automatically updated any of your gradle dependencies.


## Command Line (CLI) Building and Testing ##

Building from the CLI was initially developed for use on our Continuous Integration (CI) servers.
For most of us now, it also makes running the test suite super easy and can give a good idea of whether or not CI will pass before committing.
    
### Environment requirements ###

Preferred environments:
- Windows: PowerShell
- Unix: bash

To simplify building and testing, a number of platform specific scripts are included in the repository. 

### Build/Test Scripts ###

The commands `./run` and `./test` can be used to run (install the application on an emulator and/or attached device) and test the application. 
`./test` is definitely the go-to for most of us. It's also what the CI server uses and again is a pretty good indication of whether or not anything broke before you push.

Gradle tasks (ex. build, clean, test, installDebug) can always be run using `./gradlew task_name` from the project root.
This is essentially what's happening under the hood of the other scripts.

More information on testing and the test script can be found [here](#testing).

### Install script ###
 
#### Unix/Unix-like ####
 
If you don't want to worry about packages and they're associated nonsense, executing `source install-sdk-tools.sh` will install the Android SDK and required packages (listed below) to `$HOME/Android/Sdk`. 
The `source` part of the command ensures that any exported variables are brought into your current shell session. This means that the installed tools will be on `PATH` and usable for the rest of your shell session.

Before installing anything, the script also checks if `$HOME/Android/Sdk` already exists.
If it does, it ONLY exports the required variables to your current session, making the tools usable again.

To use the tools repeatedly, adding them to `PATH` yourself, sourcing this script manually every time you start a new instance of bash, or sourcing it automatically in your [`~/.bashrc`](https://stackoverflow.com/questions/4952177/include-additional-files-in-bashrc) are all options.

#### Windows ####
The Windows setup is slightly less scripted than Unix right now. The packages listed below can all be installed via CLI with `sdkmanager`, but the process is not currently automated.
A standard GUI installation of Android Studio followed by adding the installation's SDK and tools to PATH is the best option for most.
The installation usually puts the SDK in `%LocalAppData%\Android\Sdk`

After installing Android Studio, open Environment Variables, and add/modify the following.
```
ANDROID_HOME=%LocalAppData%\Android\Sdk
```
And the following entries to `PATH`
```
%ANDROID_HOME%\tools
%ANDROID_HOME%\tools\bin
%ANDROID_HOME%\platform-tools
%ANDROID_HOME%\emulator
```

This will hopefully be automated soon.

From here, everything should be relatively similar for both operating systems.

This is a copy of our installed sdkmanager packages (updated 6/9/2018). If yours matches, you should be able to build:

  Path                                                | Version      | Description                                     | Location
  -------                                             | -------      | -------                                         | -------
  build-tools;27.0.3                                  | 27.0.3       | Android SDK Build-Tools 27.0.3                  | build-tools\27.0.3\ 
  cmake;3.6.4111459                                   | 3.6.4111459  | CMake 3.6.4111459                               | cmake\3.6.4111459\ 
  emulator                                            | 27.2.9       | Android Emulator                                | emulator\ 
  extras;android;m2repository                         | 47.0.0       | Android Support Repository                      | extras\android\m2repository\ 
  extras;google;m2repository                          | 58           | Google Repository                               | extras\google\m2repository\ 
  extras;intel;Hardware_Accelerated_Execution_Manager | 6.2.1        | Intel x86 Emulator Accelerator (HAXM installer) | extras\intel\Hardware_Accelerated_Execution_Manager\ 
  ndk-bundle                                          | 17.0.4754217 | NDK                                             | ndk-bundle\ 
  patcher;v4                                          | 1            | SDK Patch Applier v4                            | patcher\v4\ 
  platform-tools                                      | 27.0.1       | Android SDK Platform-Tools                      | platform-tools\ 
  platforms;android-27                                | 1            | Android SDK Platform 27                         | platforms\android-27\ 
  sources;android-27                                  | 1            | Sources for Android 27                          | sources\android-27\ 
  system-images;android-27;google_apis;x86            | 6            | Google APIs Intel x86 Atom System Image         | system-images\android-27\google_apis\x86\ 
  tools                                               | 26.1.1       | Android SDK Tools                               | tools\ 

Note: `system-images;android-27;google_apis;x86` is architecture dependent in the above configuration.
If you don't have an x86 based computer, you might be out of luck.
Google stopped distributing ARM based images at API level 25.



## Testing ##

Unit tests can be found under `mobile/src/test/`.

Instrumented tests can be found under `mobile/src/androidTest`. 
We're currently working through an issue where separating our instrumentation tests into more than one file causes our test orchestrator process to crash.
For the time being, all instrumented tests are stored in a single file.

Test can be run on all platforms using the command `./test`. 
With no options, the script will create an avd image, start an emulator using that avd image, and run the test suite on both the emulator and any connected devices.
The following options are currently available:

```
Usage: test [-a=avd device name] [-s] [-h]
   -a Use the provided avd image instead of the default
   -s Run emulator with no visible window (headless).
   -p Use an attached phone. Don't start the emulator.
```



## Continuous Integration ##

The two CI services for this project, Jenkins and Travis, handle the automated building and testing of the application after every push to GitHub.

Travis handles only unit tests and is a shared service hosted at https://travis-ci.org/symboxtra/SplitSound-Android/builds.

Jenkins handles both unit and integration tests and is our own service hosted at http://jenkins.symboxtra.tk.

The Jenkins server has two separate pipelines. One serves the `master` branch while the other handles all other branches.
Because of the way Jenkins handles badging, this ensures that the status of the `master` is not obfuscated by any experimental development.



## Code Coverage ##

Code coverage can be acquired using the custom gradle task `jacocoTestReport`. 

The XML and HTML reports can be found at `mobile/build/reports/jacoco/jacocoTestReport`.

Coverage data is uploaded to and tracked on [codecov.io](https://codecov.io/gh/symboxtra/SplitSound-Android).
The CI server performs the upload after successfully running the test suite.

