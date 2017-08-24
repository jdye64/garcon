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

FROM centos:7.2.1511
MAINTAINER Jeremy Dyer <jeremy.dyer@apache.org>

ARG NIFI_DEVICE_REGISTRY_VERSION
ARG NIFI_DEVICE_REGISTRY_BINARY
ARG NIFI_DEVICE_REGISTRY_YAML

ENV NIFI_DEVICE_REGISTRY_BASE_DIR /opt/nifideviceregistry
RUN mkdir -p $NIFI_DEVICE_REGISTRY_BASE_DIR

ADD $NIFI_DEVICE_REGISTRY_BINARY $NIFI_DEVICE_REGISTRY_BASE_DIR
ADD $NIFI_DEVICE_REGISTRY_YAML $NIFI_DEVICE_REGISTRY_BASE_DIR

RUN yum install -y java-1.8.0-openjdk-devel wget unzip net-tools
RUN echo "export JAVA_HOME=/usr/lib/jvm/java" >> /root/.bashrc

ADD mysql /mysql
ADD scripts /scripts
RUN chmod +x /scripts/*

# Install MySQL for NiFi Device Registry Web tier
RUN rpm -ivh /mysql/mysql57-community-release-el7-9.noarch.rpm
RUN yum install -y mysql-server
ADD docker-entrypoint-initdb.d /docker-entrypoint-initdb.d

# Install the NARs that are built as part of this project and needed for for Apache NiFi Device Registry ecosystem to function properly
RUN mkdir -p /opt/nifi/nifideps
ADD nifi /opt/nifi/nifideps
ADD nifi-backpressure-nar-$NIFI_DEVICE_REGISTRY_VERSION.nar /opt/nifi/nifi-1.2.0/lib/.
ADD nifi-clusterstate-nar-$NIFI_DEVICE_REGISTRY_VERSION.nar /opt/nifi/nifi-1.2.0/lib/.
ADD nifi-provenance-nar-$NIFI_DEVICE_REGISTRY_VERSION.nar /opt/nifi/nifi-1.2.0/lib/.
ADD nifi-rt-nar-$NIFI_DEVICE_REGISTRY_VERSION.nar /opt/nifi/nifi-1.2.0/lib/.
ADD nifi-sla-nar-$NIFI_DEVICE_REGISTRY_VERSION.nar /opt/nifi/nifi-1.2.0/lib/.

# Expose all needed ports
EXPOSE 8080 8181 8282 9090
# Startup NiFi
CMD /scripts/docker-entrypoint.sh
