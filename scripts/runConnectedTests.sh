#!/bin/bash

adb logcat *:S TestRunner:V -T 1 & LOGCAT_PID=$! ; \
./gradlew :app:connectedDebugAndroidTest ; \
if [ -n "$LOGCAT_PID" ] ; then kill $LOGCAT_PID; fi