#!/bin/bash

echo "Starting Apache NiFi 1.2.0"

NIFI_HOST=$(hostname)
PORT_APPEND=${NIFI_HOST:4}

# Change the values that need changed in the Apache NiFi nifi.properties file before starting this instance.
sed -i "s/nifi.remote.input.host=/nifi.remote.input.host=$NIFI_HOST/" /opt/nifi/nifi-1.2.0/conf/nifi.properties
sed -i "s/nifi.cluster.node.address=/nifi.cluster.node.address=$NIFI_HOST/" /opt/nifi/nifi-1.2.0/conf/nifi.properties
sed -i "s/nifi.cluster.node.protocol.port=/nifi.cluster.node.protocol.port=833$PORT_APPEND/" /opt/nifi/nifi-1.2.0/conf/nifi.properties

# Start the instance.
/opt/nifi/nifi-1.2.0/bin/nifi.sh start

while true; do
  sleep 3
  echo "sleeping finished ...."
done