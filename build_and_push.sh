#!/usr/bin/env bash
TAG=${1:latest}
FULL_TAG=ghcr.io/finance-control/bookkeeper:"${TAG}"
VERSION="v${TAG}"

git tag "${VERSION}" -m "${2}" && git push --tags

if [[ $? -ne 0 ]]; then
    echo "smth went wrong"
    exit 1
fi

./gradlew bootJar
docker build . -t "$FULL_TAG" --platform linux/amd64 -f Dockerfile-light
docker push "${FULL_TAG}"