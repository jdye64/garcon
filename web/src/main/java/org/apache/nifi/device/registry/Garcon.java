/**
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.nifi.device.registry;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.nifi.device.registry.dao.cluster.NiFiClusterDAO;
import org.apache.nifi.device.registry.dao.device.DeviceDAO;
import org.apache.nifi.device.registry.dao.device.NiFiDeviceDAO;
import org.apache.nifi.device.registry.dao.monitor.MetricThresholdDAO;
import org.apache.nifi.device.registry.resource.DeviceRegistryDashboardResource;
import org.apache.nifi.device.registry.resource.NiFiDeviceWebSocketNotifier;
import org.apache.nifi.device.registry.resource.c2.C2Resource;
import org.apache.nifi.device.registry.resource.c2.core.config.C2DeviceFlowFileConfig;
import org.apache.nifi.device.registry.resource.c2.dao.*;
import org.apache.nifi.device.registry.resource.device.DeviceResource;
import org.apache.nifi.device.registry.resource.operations.ConnectionsResource;
import org.apache.nifi.device.registry.resource.operations.ProcessorsResource;
import org.apache.nifi.device.registry.service.device.DeviceService;
import org.apache.nifi.device.registry.service.device.impl.DeviceServiceImpl;
import org.apache.nifi.device.registry.service.monitor.impl.ScheduledMonitoringServiceImpl;
import org.skife.jdbi.v2.DBI;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.websockets.WebsocketBundle;

public class Garcon
        extends Application<GarconConfiguration> {

    private WebsocketBundle websocketBundle;

    @Override
    public void initialize(Bootstrap<GarconConfiguration> bootstrap) {

        //Creates an Asset bundle to serve up static content. Served from http://localhost:8080/assets/
        bootstrap.addBundle(new AssetsBundle());

        websocketBundle = new WebsocketBundle(NiFiDeviceWebSocketNotifier.class);
        bootstrap.addBundle(websocketBundle);
    }

    @Override
    public void run(GarconConfiguration configuration, Environment environment) throws Exception {

        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(environment, configuration.getDataSourceFactory(), "mysql");

        final NiFiClusterDAO clusterDAO = jdbi.onDemand(NiFiClusterDAO.class);
        final NiFiDeviceDAO nodeDAO = jdbi.onDemand(NiFiDeviceDAO.class);
        final MetricThresholdDAO metricThresholdDAO = jdbi.onDemand(MetricThresholdDAO.class);
        final DeviceDAO deviceDAO = jdbi.onDemand(DeviceDAO.class);

        // C2 JDBI Instances
        final C2DeviceDAO c2DeviceDAO = jdbi.onDemand(C2DeviceDAO.class);
        final C2QueueMetricsDAO c2QueueMetricsDAO = jdbi.onDemand(C2QueueMetricsDAO.class);
        final C2HeartbeatDAO c2HeartbeatDAO = jdbi.onDemand(C2HeartbeatDAO.class);
        final C2OperationDAO c2OperationDAO = jdbi.onDemand(C2OperationDAO.class);
        final C2ProcessMetricsDAO c2ProcessMetricsDAO = jdbi.onDemand(C2ProcessMetricsDAO.class);
        final C2ComponentDAO c2ComponentDAO = jdbi.onDemand(C2ComponentDAO.class);
        final C2DeviceFlowFileConfigDAO c2DeviceFlowFileConfigDAO = jdbi.onDemand(C2DeviceFlowFileConfigDAO.class);
        final C2DeviceFlowFileConfigMappingDAO c2DeviceFlowFileConfigMappingDAO = jdbi.onDemand(C2DeviceFlowFileConfigMappingDAO.class);

//        //Add managed instances.
//        environment.lifecycle().manage(new Site2SiteManagedProxy(configuration));

        //Register your Web Resources like below.
        DeviceService deviceService = new DeviceServiceImpl(configuration, deviceDAO);
        environment.jersey().register(new DeviceResource(configuration, deviceService));

        //Operational resources
        environment.jersey().register(new DeviceRegistryDashboardResource(configuration));
        environment.jersey().register(new ConnectionsResource(configuration));
        environment.jersey().register(new ProcessorsResource(configuration));
        environment.jersey().register(
                new C2Resource(configuration, c2DeviceDAO,
                        c2QueueMetricsDAO, c2HeartbeatDAO, c2OperationDAO,
                        c2ProcessMetricsDAO,c2ComponentDAO,c2DeviceFlowFileConfigDAO,c2DeviceFlowFileConfigMappingDAO));

        //Create an instance of the MonitorService in a new thread that will be ran periodically.
        //TODO: Move this to a managed instance.
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(new ScheduledMonitoringServiceImpl(clusterDAO, nodeDAO, metricThresholdDAO), 0, 5, TimeUnit.SECONDS);

//        DockerEngineService dockerEngineService = new DockerEngineServiceRESTImpl();
//        String containerId = dockerEngineService.startNiFiContainer("apache/nifi:1.2.0", 1);
//        System.out.println("Docker container starting with container ID: " + containerId);
//        System.out.println("Stopping docker container now ....");
//        dockerEngineService.stopNiFiContainer(containerId);
//        System.out.println("Docker container stopped");
    }

    public static void main(String[] args) throws Exception {
        new Garcon().run(args);
    }
}
