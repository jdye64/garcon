#!/bin/bash
echo "Building Docker image for Garcon testing from Apache NiFi 1.2.0 base image"
docker build -t jdye64/nifi:garcon-1.2.0-SNAPSHOT .