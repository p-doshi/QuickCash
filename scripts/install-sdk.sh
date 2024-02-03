#!/bin/bash
# This script installs the Android SDK and the build tools

# Download the command line tools
wget https://dl.google.com/android/repository/commandlinetools-linux-7302050_latest.zip -O commandlinetools.zip

# Extract the command line tools
unzip commandlinetools.zip -d $ANDROID_SDK_ROOT

# Accept the licenses
yes | $ANDROID_SDK_ROOT/cmdline-tools/bin/sdkmanager --licenses

# Install the SDK platform and the build tools
$ANDROID_SDK_ROOT/cmdline-tools/bin/sdkmanager "platforms;android-$ANDROID_SDK_VERSION" "build-tools;$ANDROID_BUILD_TOOLS_VERSION"
