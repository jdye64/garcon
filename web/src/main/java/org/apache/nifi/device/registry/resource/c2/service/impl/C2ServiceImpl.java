package org.apache.nifi.device.registry.resource.c2.service.impl;

import java.util.List;

import org.apache.nifi.device.registry.api.device.MiNiFiCPPDevice;
import org.apache.nifi.device.registry.dao.device.DeviceDAO;
import org.apache.nifi.device.registry.resource.c2.core.C2Payload;
import org.apache.nifi.device.registry.resource.c2.core.C2Response;
import org.apache.nifi.device.registry.resource.c2.core.ops.C2Operation;
import org.apache.nifi.device.registry.resource.c2.dao.C2PayloadDAO;
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

    private C2PayloadDAO heartbeatDAO;
    private DeviceDAO deviceDAO;

    public C2ServiceImpl(C2PayloadDAO payloadDAO, DeviceDAO deviceDAO) {
        this.heartbeatDAO = payloadDAO;
        this.deviceDAO = deviceDAO;
    }

    @Transaction
    public C2Response registerHeartBeat(C2Payload heartbeatPayload) {

        //First save the MiNiFi Device in the DB if it doesn't already exist.
        MiNiFiCPPDevice cppDevice = new MiNiFiCPPDevice();
        if (heartbeatPayload.getDeviceInfo() != null) {
            cppDevice.setPrimaryNICMac(heartbeatPayload.getDeviceInfo().getNetworkInfo().get(0).getDeviceid());
            cppDevice.setPublicIPAddress(heartbeatPayload.getDeviceInfo().getNetworkInfo().get(0).getIp());
            cppDevice.setHostname(heartbeatPayload.getDeviceInfo().getNetworkInfo().get(0).getHostname());
            cppDevice.setAvailableProcessors(heartbeatPayload.getDeviceInfo().getSystemInfo().get(0).getVcores());
            cppDevice.setTotalSystemMemory(heartbeatPayload.getDeviceInfo().getSystemInfo().get(0).getPhysicalMemory());

            this.deviceDAO.insertMiNiFiCPPDeviceTransaction(cppDevice);

            this.heartbeatDAO.registerHeartbeat(heartbeatPayload);

            // Create the response.
            C2Response response = new C2Response();
            response.setOperation(heartbeatPayload.getOperation());
            response.setOperations(operationsForDevice(heartbeatPayload));
            return response;
        } else {
            logger.warn("DeviceInfo in the Heartbeat payload is NULL!");
            return null;
        }
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
