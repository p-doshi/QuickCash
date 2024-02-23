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
# Set JAVA_HOME - SDK
#==============================
ENV ANDROID_SDK_ROOT=/opt/android
ENV PATH="$PATH:$ANDROID_SDK_ROOT/cmdline-tools/tools:$ANDROID_SDK_ROOT/cmdline-tools/tools/bin::$ANDROID_SDK_ROOT/tools/bin:$ANDROID_SDK_ROOT/platform-tools:$ANDROID_SDK_ROOT/build-tools/${BUILD_TOOLS}"
ENV DOCKER="true"

#==========================================================
# Install Dependenices and required Android CMD-line tools
#==========================================================
ARG ANDROID_CMD="commandlinetools-linux-11076708_latest.zip"
ARG PMD_VERSION="7.0.0-rc4"
ARG PMD_FILE="pmd-dist-$PMD_VERSION-bin.zip"
RUN apt update -y && apt install -y --no-install-recommends wget unzip  && \
    wget https://dl.google.com/android/repository/${ANDROID_CMD} -P /tmp && \
    unzip -d $ANDROID_SDK_ROOT /tmp/$ANDROID_CMD && \
    mkdir -p $ANDROID_SDK_ROOT/cmdline-tools/tools && \
    cd $ANDROID_SDK_ROOT/cmdline-tools && mv NOTICE.txt source.properties bin lib tools/ && cd / && \
    wget https://github.com/pmd/pmd/releases/download/pmd_releases%2F${PMD_VERSION}/${PMD_FILE} -P /tmp && \
    unzip -d /pmd /tmp/$PMD_FILE && \
    ln -s /pmd/pmd-bin-${PMD_VERSION}/bin/pmd /bin/pmd && \
    rm -rf /tmp/* && \
    apt remove -y wget unzip && \
    rm -rf /var/lib/apt/lists/*

#==============================
# Android SDK ARGS
#==============================
ARG ARCH="x86_64"
ARG TARGET="google_apis"
ARG API_LEVEL="34"
ARG BUILD_TOOLS="34.0.0"
ARG ANDROID_API_LEVEL="android-${API_LEVEL}"
ARG ANDROID_APIS="${TARGET};${ARCH}"
ARG PLATFORM_VERSION="platforms;${ANDROID_API_LEVEL}"
ARG BUILD_TOOL="build-tools;${BUILD_TOOLS}"
ARG ANDROID_SDK_PACKAGES="${PLATFORM_VERSION} ${BUILD_TOOL} platform-tools"

#============================================
# Install required package using SDK manager
#============================================
RUN yes | sdkmanager --licenses && \
    yes | sdkmanager --verbose --no_https ${ANDROID_SDK_PACKAGES}

#=======================
# framework entry point
#=======================
CMD [ "/bin/bash" ]