# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements. See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership. The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License. You may obtain a copy of the License at
#
#   http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied. See the License for the
# specific language governing permissions and limitations
# under the License.
#

FROM openjdk:8
MAINTAINER Jeremy Dyer <jdye64@gmail.com>

ARG NIFI_DEVICE_REGISTRY_VERSION
ARG NIFI_DEVICE_REGISTRY_BINARY
ARG NIFI_DEVICE_REGISTRY_YAML

ENV NIFI_DEVICE_REGISTRY_BASE_DIR /opt/nifideviceregistry
RUN mkdir -p $NIFI_DEVICE_REGISTRY_BASE_DIR

ADD $NIFI_DEVICE_REGISTRY_BINARY $NIFI_DEVICE_REGISTRY_BASE_DIR
ADD $NIFI_DEVICE_REGISTRY_YAML $NIFI_DEVICE_REGISTRY_BASE_DIR

# Web HTTP Port
EXPOSE 8080

# Startup NiFi
CMD java -jar $NIFI_DEVICE_REGISTRY_BASE_DIR/nifi-device-registry-web-1.2.0-SNAPSHOT.jar server $NIFI_DEVICE_REGISTRY_BASE_DIR/DeviceRegistry.yml