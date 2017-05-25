#!/bin/bash

echo "This is the docker entrypoint script for Apache NiFi Device Registry that will start up all of the required components."

nohup java -jar $NIFI_DEVICE_REGISTRY_BASE_DIR/nifi-device-registry-web-1.2.0-SNAPSHOT.jar server $NIFI_DEVICE_REGISTRY_BASE_DIR/DeviceRegistry.yml &
/opt/nifi/nifi-1.2.0/bin/nifi.sh start

# Set MySQL environment parameters
export MYSQL_ROOT_PASSWORD=nifidevregistry
export MYSQL_DATABASE=DEVICE_REGISTRY

# Start MySQL
/scripts/mysql.sh mysqld --user=root &

while true; do
  sleep 30
done