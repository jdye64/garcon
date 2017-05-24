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

ARG HBASE_URL=https://archive.apache.org/dist/hbase/1.2.5/hbase-1.2.5-bin.tar.gz
ARG PHOENIX_URL=https://archive.apache.org/dist/phoenix/apache-phoenix-4.10.0-HBase-1.2/bin/apache-phoenix-4.10.0-HBase-1.2-bin.tar.gz

ARG HBASE_VER=hbase-1.2.5
ARG PHOENIX_VER=4.10.0-HBase-1.2

# Setup HBase binaries
RUN wget -nv ${HBASE_URL} -O /hbase.tgz
RUN tar -xzvf /hbase.tgz
RUN mv /${HBASE_VER} /hbase

# Setup Phoenix binaries
RUN wget -nv ${PHOENIX_URL} -O /phoenix.tgz
RUN tar -xzvf /phoenix.tgz
RUN mv /apache-phoenix-${PHOENIX_VER}-bin /phoenix
RUN cp /phoenix/phoenix-${PHOENIX_VER}-server.jar /hbase/lib

ADD conf /hbase/conf
ADD mysql /mysql
ADD scripts /scripts
CMD source /root/.bashrc; sh /hbase/bin/start-hbase.sh; sleep 10; /phoenix/bin/queryserver.py

# Install MySQL for NiFi Device Registry Web tier
RUN rpm -ivh /mysql/mysql57-community-release-el7-9.noarch.rpm
RUN yum install -y mysql-server

# Expose all needed ports
EXPOSE 8080
EXPOSE 8181
EXPOSE 8282
EXPOSE 9090

# Startup NiFi
CMD /scripts/docker-entrypoint.sh