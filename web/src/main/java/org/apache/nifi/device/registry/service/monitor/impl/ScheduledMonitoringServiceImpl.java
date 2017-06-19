package org.apache.nifi.device.registry.service.monitor.impl;

import java.util.Iterator;
import java.util.List;

import org.apache.nifi.api.client.NiFiClient;
import org.apache.nifi.api.client.impl.NiFiAPIClient;
import org.apache.nifi.api.client.service.ProcessGroups;
import org.apache.nifi.api.client.service.impl.ProcessGroupsImplementation;
import org.apache.nifi.device.registry.api.cluster.NiFiCluster;
import org.apache.nifi.device.registry.api.monitor.MetricThreshold;
import org.apache.nifi.device.registry.dao.cluster.NiFiClusterDAO;
import org.apache.nifi.device.registry.dao.device.NiFiDeviceDAO;
import org.apache.nifi.device.registry.dao.monitor.MetricThresholdDAO;
import org.apache.nifi.device.registry.service.container.docker.DockerEngineService;
import org.apache.nifi.device.registry.service.container.docker.impl.DockerEngineServiceRESTImpl;
import org.apache.nifi.device.registry.service.monitor.MonitoringService;
import org.apache.nifi.web.api.entity.ConnectionEntity;
import org.apache.nifi.web.api.entity.ConnectionsEntity;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p>
 * Created on 6/7/17.
 */


public class ScheduledMonitoringServiceImpl
    extends Thread
    implements MonitoringService {

    private NiFiClient niFiClient = null;
    private NiFiClusterDAO clusterDAO = null;
    private NiFiDeviceDAO nodeDAO = null;
    private MetricThresholdDAO metricThresholdDAO = null;
    private ProcessGroups processGroups = null;
    private DockerEngineService dockerEngineService = null;
    private String startedContainerId = null;

    public ScheduledMonitoringServiceImpl(NiFiClusterDAO clusterDAO, NiFiDeviceDAO nodeDAO, MetricThresholdDAO metricThresholdDAO) {
        niFiClient = new NiFiAPIClient("http://jdyer-garcon0.field.hortonworks.com", "8080");
        this.clusterDAO = clusterDAO;
        this.nodeDAO = nodeDAO;
        this.metricThresholdDAO = metricThresholdDAO;
        this.dockerEngineService = new DockerEngineServiceRESTImpl();
    }

    public void run() {
        List<MetricThreshold> thresholds = this.metricThresholdDAO.getAllMetricThresholds();
        if (thresholds != null) {
            for (MetricThreshold t : thresholds) {
                System.out.println("Threshold Name: " + t.getName());
                System.out.println("Getting the cluster for this threshold");
                NiFiCluster c = this.clusterDAO.getClusterById(t.getClusterId());
                System.out.println("Cluster Name: " + c.getName() + " Cluster URI: " + c.getHostname());

                String apiMethod = "http://" + c.getHostname() + ":" + c.getPort() + t.getApiMethod();
                System.out.println("API Method: " + apiMethod);

                this.processGroups = new ProcessGroupsImplementation(c.getHostname(), c.getPort());
                ConnectionsEntity connections = this.processGroups.getConnections("whocares", t.getComponentId());
                if (connections != null && connections.getConnections().size() > 0) {
                    System.out.println("We found some connections!!!!");
                    Iterator<ConnectionEntity> itr = connections.getConnections().iterator();
                    while (itr.hasNext()) {
                        ConnectionEntity ce = itr.next();


                        if (ce.getStatus().getAggregateSnapshot().getFlowFilesQueued() > 10 && startedContainerId == null) {
                            System.out.println("Connection found with over 10 flowfiles queued so starting a new docker container");
                            this.startedContainerId = this.dockerEngineService.startNiFiContainer("apache/nifi:1.2.0", 1);
                            System.out.println("Started container with ID: " + this.startedContainerId);
                        } else if (ce.getStatus().getAggregateSnapshot().getFlowFilesQueued() < 10 && startedContainerId != null) {
                            this.dockerEngineService.stopNiFiContainer(this.startedContainerId);
                            this.startedContainerId = null;
                        }
                    }
                } else {
                    System.out.println("Connections are empty currently .....");
                }
            }
        } else {
            System.out.println("There are currently no metric thresholds defined in Garcon");
        }
    }
}
