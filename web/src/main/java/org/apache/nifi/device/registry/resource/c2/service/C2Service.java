package org.apache.nifi.device.registry.resource.c2.service;

import java.util.List;

import org.apache.nifi.device.registry.resource.c2.core.C2Payload;
import org.apache.nifi.device.registry.resource.c2.core.C2Response;
import org.apache.nifi.device.registry.resource.c2.core.config.C2DeviceFlowFileConfig;
import org.apache.nifi.device.registry.resource.c2.core.device.DeviceInfo;
import org.apache.nifi.device.registry.resource.c2.dto.C2HUD;

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


public interface C2Service {

    /**
     * Handles the C2Payload JSON object received from the MiNifi device and writes all values
     * into the underlying database.
     *
     * @param heartbeatPayload
     * @return
     */
    C2Response registerHeartBeat(C2Payload heartbeatPayload);

    /**
     * Acknowledges that the operation with the specified id was completed on the client side.
     *
     * @param operationId
     */
    void ackOperation(long operationId);

    /**
     * Gets the latest flowfile configuration for the minifi device.
     *
     * @param deviceId
     * @return
     */
    C2DeviceFlowFileConfig getDeviceLatestFlowFileConfig(String deviceId);

    /**
     * Retrieves the specified device from the DB. If the DeviceID is empty
     * then all devices up to the DB return limit will be retrieved.
     *
     * @param deviceId
     * @return
     */
    List<DeviceInfo> getDevice(String deviceId);

    /**
     * Creates the metrics for the UI to display the C2 Heads Up Display
     *
     * @return
     *  C2HUD object representing the current HUD state.
     */
    C2HUD getC2HUD();
}
