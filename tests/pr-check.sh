#!/bin/bash

if ./gradlew bootJar --no-daemon; then
    echo "Build passed"
else
    echo "Build Failed"
    exit 1
fi

if ./gradlew test ;then
    echo "Tests are passed"
else
    echo "Tests Failed"
    exit 1
fi

./gradlew clean
