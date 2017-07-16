package org.apache.nifi.device.registry.resource.c2.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.apache.nifi.device.registry.resource.c2.core.C2Heartbeat;
import org.apache.nifi.device.registry.resource.c2.core.C2Payload;
import org.apache.nifi.device.registry.resource.c2.core.C2Response;
import org.apache.nifi.device.registry.resource.c2.core.components.Component;
import org.apache.nifi.device.registry.resource.c2.core.components.Components;
import org.apache.nifi.device.registry.resource.c2.core.config.C2DeviceFlowFileConfig;
import org.apache.nifi.device.registry.resource.c2.core.device.DeviceInfo;
import org.apache.nifi.device.registry.resource.c2.core.device.NetworkInfo;
import org.apache.nifi.device.registry.resource.c2.core.device.SystemInfo;
import org.apache.nifi.device.registry.resource.c2.core.metrics.C2ProcessMetrics;
import org.apache.nifi.device.registry.resource.c2.core.metrics.C2QueueMetrics;
import org.apache.nifi.device.registry.resource.c2.core.ops.C2Operation;
import org.apache.nifi.device.registry.resource.c2.dao.*;
import org.apache.nifi.device.registry.resource.c2.dto.C2HUD;
import org.apache.nifi.device.registry.resource.c2.dto.CreateOperationRequest;
import org.apache.nifi.device.registry.resource.c2.service.C2Service;
import org.skife.jdbi.v2.sqlobject.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * Created on 7/7/17.
 */


public class C2ServiceImpl
    implements C2Service {

    private static final Logger logger = LoggerFactory.getLogger(C2ServiceImpl.class);

    private C2DeviceDAO c2DeviceDAO;
    private C2QueueMetricsDAO c2QueueMetricsDAO;
    private C2HeartbeatDAO c2HeartbeatDAO;
    private C2OperationDAO c2OperationDAO;
    private C2ProcessMetricsDAO c2ProcessMetricsDAO;
    private C2ComponentDAO c2componentDAO;
    private C2DeviceFlowFileConfigDAO c2DeviceFlowFileConfigDAO;
    private C2DeviceFlowFileConfigMappingDAO c2DeviceFlowFileConfigMappingDAO;

    public C2ServiceImpl(C2DeviceDAO c2DeviceDAO, C2QueueMetricsDAO c2QueueMetricsDAO,
            C2HeartbeatDAO c2HeartbeatDAO, C2OperationDAO c2OperationDAO,
            C2ProcessMetricsDAO c2ProcessMetricsDAO, C2ComponentDAO c2componentDAO) {
        this.c2DeviceDAO = c2DeviceDAO;
        this.c2QueueMetricsDAO = c2QueueMetricsDAO;
        this.c2HeartbeatDAO = c2HeartbeatDAO;
        this.c2OperationDAO = c2OperationDAO;
        this.c2ProcessMetricsDAO = c2ProcessMetricsDAO;
        this.c2componentDAO = c2componentDAO;
    }

    @Transaction
    public C2Response registerHeartBeat(C2Payload heartbeatPayload) {

        NetworkInfo ni = null;
        SystemInfo si = null;
        if (heartbeatPayload.getDeviceInfo().getNetworkInfo() != null) {
            ni = heartbeatPayload.getDeviceInfo().getNetworkInfo();
        }
        if (heartbeatPayload.getDeviceInfo().getSystemInfo() != null) {
            si = heartbeatPayload.getDeviceInfo().getSystemInfo();
        }

        try {
            this.c2DeviceDAO.registerC2Device(ni.getDeviceid(), ni.getHostname(), ni.getIp(), si.getMachineArchitecture(), si.getPhysicalMemory(), si.getVcores());
        } catch (Exception ex) {
            // The device already exists so lets update it.
            this.c2DeviceDAO.updateC2Device(ni.getHostname(), ni.getIp(), si.getMachineArchitecture(), si.getPhysicalMemory(), si.getVcores(), ni.getDeviceid());
        }

        if (ni != null) {
            Map<String,String> components = heartbeatPayload.getComponents();
            final String deviceId = ni.getDeviceid();
            components.entrySet().forEach(
                    componentEntry -> c2componentDAO.updateComponentStatus(deviceId,componentEntry.getKey(), componentEntry.getValue().equals("enabled") ? true : false ));
        }

        // Insert all of the queue metrics received from the device.
        if (heartbeatPayload.getMetrics() != null && heartbeatPayload.getMetrics().getQueueMetrics() != null) {
            Iterator<String> itr = heartbeatPayload.getMetrics().getQueueMetrics().keySet().iterator();
            while (itr.hasNext()) {
                String key = itr.next();
                C2QueueMetrics m = heartbeatPayload.getMetrics().getQueueMetrics().get(key);
                try {
                    this.c2QueueMetricsDAO.insertQueueMetrics(ni.getDeviceid(), key, m.getDataSize(),
                            m.getDataSizeMax(), m.getQueued(), m.getQueueMax());
                } catch (Exception ex) {
                    // The Queue Metric already exists so lets update it.
                    this.c2QueueMetricsDAO.updateQueueMetrics(ni.getDeviceid(), key, m.getDataSize(),
                            m.getDataSizeMax(), m.getQueued(), m.getQueueMax());
                }
            }
        }

        // Inserts or updates the ProcessMetrics.
        if (heartbeatPayload.getMetrics() != null) {
            C2ProcessMetrics pm = heartbeatPayload.getMetrics().getProcessMetricss();
            if (pm != null) {
                long memoryMaxRSS = 0l;
                long cpuInvolcs = 0l;
                if (pm.getMemoryMetrics() != null) {
                    memoryMaxRSS = pm.getMemoryMetrics().getMaxrss();
                }
                if (pm.getCpuMetrics() != null) {
                    cpuInvolcs = pm.getCpuMetrics().getInvolcs();
                }
                try {
                    this.c2ProcessMetricsDAO.insertProcessMetrics(ni.getDeviceid(), memoryMaxRSS, cpuInvolcs);
                } catch (Exception ex) {
                    // Update the Process metrics.
                    this.c2ProcessMetricsDAO.updateProcessMetrics(ni.getDeviceid(), memoryMaxRSS, cpuInvolcs);
                }
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("No Process Metrics present in JSON payload. Not writing to DB");
                }
            }
        }

        // Registers or updates the heartbeat in the DB.
        try {
            this.c2HeartbeatDAO.registerHeartbeat(ni.getDeviceid(), heartbeatPayload.getOperation(), heartbeatPayload.getState().isRunning(),
                    heartbeatPayload.getState().getUptimeMilliseconds());
        } catch (Exception ex) {
            // UPdate the Heartbeat since it already exists.
            this.c2HeartbeatDAO.udpateHeartbeat(ni.getDeviceid(), heartbeatPayload.getOperation(), heartbeatPayload.getState().isRunning(),
                    heartbeatPayload.getState().getUptimeMilliseconds());
        }

        // Create the C2Response
        C2Response response = new C2Response();

        response.setOperation(heartbeatPayload.getOperation());
        response.setOperations(operationsForDevice(heartbeatPayload));

        return response;
    }

    public void ackOperation(long operationId) {
        this.c2OperationDAO.ackOperation(operationId);
    }

    public C2DeviceFlowFileConfig getDeviceLatestFlowFileConfig(String deviceId) {
        // Get the mapping for this device.
        //C2DeviceFlowFileConfigMapping mapping = this.c2DeviceFlowFileConfigMappingDAO

        // Retrieve the actual configuration
        //C2DeviceFlowFileConfig ffc = this.c2DeviceFlowFileConfigDAO.getDeviceFlowFileConfiguration();

        return null;
    }

    public void createOpearationForDevice(CreateOperationRequest cor) {

        List<String> contentList = Lists.newArrayList();
        if (null != cor.getContent()) {
            cor.getContent().entrySet().forEach(contentEntry -> contentList.add(contentEntry.getKey() + ":" + contentEntry.getValue()));
        }

        String content = Joiner.on(",").join(contentList);
        this.c2OperationDAO.createOperationForDevice(cor.getOperation(), cor.getName(), cor.getDeviceId(), content);
    }


    public List<C2Operation> getOperationHistoryForDevice(String deviceId) {
        return this.c2OperationDAO.getDeviceOperationHistory(deviceId);
    }

    public List<DeviceInfo> getDevice(String deviceId) {
        if (deviceId == null) {
            return this.c2DeviceDAO.getDeviceWithLimit(50);
        } else {
            return this.c2DeviceDAO.getDeviceWithLimit(50);
        }
    }


    public List<C2QueueMetrics> getConnectionsForDevice(String deviceId){
        List<C2QueueMetrics> metrics = Lists.newArrayList();

        if (deviceId != null){
            metrics.addAll ( this.c2QueueMetricsDAO.getConnectionsForDevice(deviceId) );

        }
        return metrics;
    }

    public List<Component> getComponentsForDevice(String deviceId){
        List<Component> components = Lists.newArrayList();

        if (deviceId != null){
            components.addAll ( this.c2componentDAO.getComponentStatus(deviceId) );

        }
        return components;
    }

    public C2HUD getC2HUD() {
        C2HUD hud = new C2HUD();
        hud.setTotalDevices(c2DeviceDAO.totalNumDevices());
        List<C2Heartbeat> deviceHeartBeats = this.c2HeartbeatDAO.getLatestDeviceHeartbeat();
        long running = 0l;
        long stopped = 0l;
        if (deviceHeartBeats != null) {
            // Loop through and count the running and stopped devices.
            for (C2Heartbeat hb : deviceHeartBeats) {
                if (hb.isRunning()) {
                    running++;
                } else {
                    stopped++;
                }
            }
        }
        hud.setRunningDevices(running);
        hud.setStoppedDevices(stopped);
        return hud;
    }

    /**
     * Retrieves the list of pending operations for the device from the backing store.
     *
     * @param heartbeat
     *  Heartbeat received from the device.
     *
     * @return
     *  List of Operations that the device should perform.
     */
    private List<C2Operation> operationsForDevice(C2Payload heartbeat) {
        logger.info("Device ID is {}", heartbeat.getDeviceInfo().getNetworkInfo().getDeviceid());
        return this.c2OperationDAO.getPendingOperationsForDevice(heartbeat.getDeviceInfo().getNetworkInfo().getDeviceid());
    }
}
