#!/usr/bin/env bash
TAG=${1:latest}
FULL_TAG=ghcr.io/finance-control/bookkeeper:"${TAG}"

./gradlew bootJar
docker build . -t "$FULL_TAG" --platform linux/amd64 -f Dockerfile-light
docker push "${FULL_TAG}"