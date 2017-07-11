package org.apache.nifi.device.registry.resource.c2.service.impl;

import java.sql.Timestamp;
import java.util.List;

import org.apache.nifi.device.registry.resource.c2.core.C2Payload;
import org.apache.nifi.device.registry.resource.c2.core.C2Response;
import org.apache.nifi.device.registry.resource.c2.core.device.NetworkInfo;
import org.apache.nifi.device.registry.resource.c2.core.device.SystemInfo;
import org.apache.nifi.device.registry.resource.c2.core.metrics.C2QueueMetrics;
import org.apache.nifi.device.registry.resource.c2.core.ops.C2Operation;
import org.apache.nifi.device.registry.resource.c2.dao.C2DeviceDAO;
import org.apache.nifi.device.registry.resource.c2.dao.C2HeartbeatDAO;
import org.apache.nifi.device.registry.resource.c2.dao.C2QueueMetricsDAO;
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

    public C2ServiceImpl(C2DeviceDAO c2DeviceDAO, C2QueueMetricsDAO c2QueueMetricsDAO, C2HeartbeatDAO c2HeartbeatDAO) {
        this.c2DeviceDAO = c2DeviceDAO;
        this.c2QueueMetricsDAO = c2QueueMetricsDAO;
        this.c2HeartbeatDAO = c2HeartbeatDAO;
    }

    @Transaction
    public C2Response registerHeartBeat(C2Payload heartbeatPayload) {

        NetworkInfo ni = null;
        SystemInfo si = null;
        if (heartbeatPayload.getDeviceInfo().getNetworkInfo() != null) {
            ni = heartbeatPayload.getDeviceInfo().getNetworkInfo().get(0);
        }
        if (heartbeatPayload.getDeviceInfo().getSystemInfo() != null) {
            si = heartbeatPayload.getDeviceInfo().getSystemInfo().get(0);
        }

        try {
            this.c2DeviceDAO.registerC2Device(ni.getDeviceid(), ni.getHostname(), ni.getIp(), si.getMachineArchitecture(), si.getPhysicalMemory(), si.getVcores());
        } catch (Exception ex) {
            // The device already exists so lets update it.
            this.c2DeviceDAO.updateC2Device(ni.getHostname(), ni.getIp(), si.getMachineArchitecture(), si.getPhysicalMemory(), si.getVcores(), ni.getDeviceid());
        }

        // Insert all of the queue metrics received from the device.
        if (heartbeatPayload.getMetrics().getQueueMetrics() != null) {
            for (C2QueueMetrics m : heartbeatPayload.getMetrics().getQueueMetrics()) {
                try {
                    this.c2QueueMetricsDAO.insertQueueMetrics(ni.getDeviceid(), m.getName(), m.getDataSize(),
                            m.getDataSizeMax(), m.getQueued(), m.getQueueMax());
                } catch (Exception ex) {
                    // The Queue Metric already exists so lets update it.
                    this.c2QueueMetricsDAO.updateQueueMetrics(ni.getDeviceid(), m.getName(), m.getDataSize(),
                            m.getDataSizeMax(), m.getQueued(), m.getQueueMax());
                }
            }
        }

        // Registers the heartbeat in the DB
        this.c2HeartbeatDAO.registerHeartbeat(ni.getDeviceid(), heartbeatPayload.getOperation(), heartbeatPayload.getState().getRunning(),
                heartbeatPayload.getState().getUptimeMilliseconds(), new Timestamp(System.currentTimeMillis()));

        // Create the C2Response
        C2Response response = new C2Response();

        response.setOperation(heartbeatPayload.getOperation());
        response.setOperations(operationsForDevice(heartbeatPayload));

        return response;
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
        return null;
    }
}
