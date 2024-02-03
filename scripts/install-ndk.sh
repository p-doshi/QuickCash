#!/bin/bash
# This script installs the Android NDK

# Download the NDK
wget https://dl.google.com/android/repository/android-ndk-r23b-linux-x86_64.zip -O ndk.zip

# Extract the NDK
unzip ndk.zip -d $ANDROID_NDK_ROOT
