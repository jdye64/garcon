#!/bin/bash

echo "This is the docker entrypoint script for Apache NiFi Device Registry that will start up all of the required components."

# Set MySQL environment parameters
export MYSQL_ROOT_PASSWORD=nifidevregistry
export MYSQL_DATABASE=GARCON

# Start MySQL
/scripts/mysql.sh mysqld --user=root &

/opt/nifi/nifi-1.2.0/bin/nifi.sh start

# Let MySQL finish starting
while ! mysqlshow --user=root --password=${MYSQL_ROOT_PASSWORD}; do
    echo "MySQL not yet started... waiting"
    sleep 2
done

echo "Starting Device Registry UI"

nohup java -jar $NIFI_DEVICE_REGISTRY_BASE_DIR/nifi-device-registry-web-1.3.0-SNAPSHOT.jar server $NIFI_DEVICE_REGISTRY_BASE_DIR/Garcon.yml &

while true; do
  sleep 30
done
