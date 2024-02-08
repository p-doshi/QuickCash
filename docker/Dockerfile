#======================================================================================================================
# Credit to Amr Salem for creating most of this.
#
# Here is his blog post: https://medium.com/innovies-club/running-android-emulator-in-a-docker-container-19ecb68e1909
# Here is his github repo: https://github.com/amrsa1/Android-Emulator-image/tree/main
#======================================================================================================================

FROM openjdk:17-jdk-slim

LABEL maintainer "ethroz"

ENV DEBIAN_FRONTEND noninteractive

WORKDIR /
SHELL ["/bin/bash", "-c"]

#==============================
# Android SDK ARGS
#==============================
ARG ARCH="x86_64"
ARG TARGET="google_apis"
ARG API_LEVEL="34"
ARG BUILD_TOOLS="34.0.0"
ARG ANDROID_ARCH=${ANDROID_ARCH_DEFAULT}
ARG ANDROID_API_LEVEL="android-${API_LEVEL}"
ARG ANDROID_APIS="${TARGET};${ARCH}"
ARG EMULATOR_PACKAGE="system-images;${ANDROID_API_LEVEL};${ANDROID_APIS}"
ENV EMULATOR_PACKAGE=$EMULATOR_PACKAGE
ARG PLATFORM_VERSION="platforms;${ANDROID_API_LEVEL}"
ARG BUILD_TOOL="build-tools;${BUILD_TOOLS}"
ARG ANDROID_CMD="commandlinetools-linux-11076708_latest.zip"
ARG ANDROID_SDK_PACKAGES="${EMULATOR_PACKAGE} ${PLATFORM_VERSION} ${BUILD_TOOL} platform-tools"

#==============================
# Set JAVA_HOME - SDK
#==============================
ENV ANDROID_SDK_ROOT=/opt/android
ENV PATH="$PATH:$ANDROID_SDK_ROOT/cmdline-tools/tools:$ANDROID_SDK_ROOT/cmdline-tools/tools/bin:$ANDROID_SDK_ROOT/emulator:$ANDROID_SDK_ROOT/tools/bin:$ANDROID_SDK_ROOT/platform-tools:$ANDROID_SDK_ROOT/build-tools/${BUILD_TOOLS}"
ENV DOCKER="true"

#=============================
# Install Dependenices and required Android CMD-line tools
#=============================
RUN apt-get update -y && apt-get install -y --no-install-recommends \
    wget unzip bzip2 libdrm-dev libxkbcommon-dev libgbm-dev libasound-dev libnss3 \
    libxcursor1 libxshmfence-dev xauth libdbus-glib-1-2 && \
    wget https://dl.google.com/android/repository/${ANDROID_CMD} -P /tmp && \
    unzip -d $ANDROID_SDK_ROOT /tmp/$ANDROID_CMD && \
    mkdir -p $ANDROID_SDK_ROOT/cmdline-tools/tools && \
    cd $ANDROID_SDK_ROOT/cmdline-tools &&  mv NOTICE.txt source.properties bin lib tools/  && \
    cd / && rm -rf /tmp/* && \
    apt-get remove -y wget unzip && \
    rm -rf /var/lib/apt/lists/*

#============================================
# Install required package using SDK manager
#============================================
RUN yes | sdkmanager --licenses && \
    yes | sdkmanager --verbose --no_https ${ANDROID_SDK_PACKAGES}

#============================================
# Create required emulator
#============================================
ARG EMULATOR_NAME="Pixel"
ARG EMULATOR_DEVICE="pixel_7"
ENV EMULATOR_NAME=$EMULATOR_NAME
ENV DEVICE_NAME=$EMULATOR_DEVICE
RUN echo "no" | avdmanager --verbose create avd --force --name "${EMULATOR_NAME}" --device "${EMULATOR_DEVICE}" --package "${EMULATOR_PACKAGE}"

#===================
# Alias
#===================
ENV EMU_HEADLESS=/start_emu_headless.sh

#==================================
# Copying Emulator Script to root
#==================================
COPY . /
RUN chmod a+x $EMU_HEADLESS

#=======================
# framework entry point
#=======================
CMD [ "/bin/bash" ]