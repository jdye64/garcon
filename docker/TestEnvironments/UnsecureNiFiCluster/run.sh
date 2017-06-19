#!/bin/bash

echo "Stopping all containers that are already running ...."
docker stop $(docker ps -a -q)
echo "Removing all containers ...."
docker rm $(docker ps -a -q)

echo "Starting Apache Zookeeper Quorum of containers"
docker run -it -d -h zookeeper1 --name zookeeper1 -P zookeeper:3.4

echo "Starting Apache NiFi 1.2.0 Unsecured Cluster to Garcon testing"
docker run -it -d -h nifi1 --name nifi1 --link zookeeper1 -p 8080:8080 -p 8444:8444 jdye64/nifi:garcon-1.2.0-SNAPSHOT
docker run -it -d -h nifi2 --name nifi2 --link zookeeper1 -p 8081:8080 -p 8445:8444 jdye64/nifi:garcon-1.2.0-SNAPSHOT
docker run -it -d -h nifi3 --name nifi3 --link zookeeper1 --link nifi1 --link nifi2 -p 8082:8080 -p 8446:8444 jdye64/nifi:garcon-1.2.0-SNAPSHOT