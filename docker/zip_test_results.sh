#!/bin/bash

# Assign the first argument to a variable
subdir=$1

# Zip the contents of the subdirectory and name the zip file as the subdirectory name
cd app/build/reports/tests
zip -r "${subdir}.zip" "$subdir"

# Copy the zip file to the current directory
cp "${subdir}.zip" ../../../../