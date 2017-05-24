#!/bin/bash

echo "This is the docker entrypoint script for Apache NiFi Device Registry that will start up all of the required components."

nohup java -jar $NIFI_DEVICE_REGISTRY_BASE_DIR/nifi-device-registry-web-1.2.0-SNAPSHOT.jar server $NIFI_DEVICE_REGISTRY_BASE_DIR/DeviceRegistry.yml &