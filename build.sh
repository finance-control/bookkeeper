#!/usr/bin/env bash

gradle bootJar
docker build . -t gh