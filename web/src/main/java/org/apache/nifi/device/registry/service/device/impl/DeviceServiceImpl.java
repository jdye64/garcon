package org.apache.nifi.device.registry.service.device.impl;

import java.util.List;

import org.apache.nifi.device.registry.GarconConfiguration;
import org.apache.nifi.device.registry.api.device.Device;
import org.apache.nifi.device.registry.api.device.MiNiFiCPPDevice;
import org.apache.nifi.device.registry.api.device.MiNiFiJavaDevice;
import org.apache.nifi.device.registry.api.device.NiFiDevice;
import org.apache.nifi.device.registry.dao.DBConstants;
import org.apache.nifi.device.registry.dao.device.DeviceDAO;
import org.apache.nifi.device.registry.service.device.DeviceService;
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
 * Created on 3/10/17.
 */


public class DeviceServiceImpl
    implements DeviceService {

    private static final Logger logger = LoggerFactory.getLogger(DeviceServiceImpl.class);
    private GarconConfiguration garconConfiguration = null;

    // DAO
    private DeviceDAO deviceDAO = null;

    public DeviceServiceImpl(GarconConfiguration configuration, DeviceDAO deviceDAO) {
        this.garconConfiguration = configuration;
        this.deviceDAO = deviceDAO;
    }

    public List<Device> getNiFiDevices() {
        return this.deviceDAO.getAllNiFiDevices();
    }

    public Device getNiFiDeviceById(String deviceId) {
        return this.deviceDAO.getNiFiDeviceById(deviceId);
    }

    public void addNiFiDevice(NiFiDevice niFiDevice) {
        try {
            deviceDAO.insertNiFiDeviceTransaction(niFiDevice);
        } catch (Exception ex) {
            logger.error("Error inserting NiFiDevice object into {} with exception {}", DBConstants.DEVICE_TABLE, ex.getMessage());
        }
    }

    public void addMiNiFiJavaDevice(MiNiFiJavaDevice mjd) {
        try {
            deviceDAO.insertMiNiFiJavaDeviceTransaction(mjd);
        } catch (Exception ex) {
            logger.error("Error inserting MiNiFiJavaDevice object into {} with exception {}", DBConstants.MINIFI_DEVICE_TABLE, ex.getMessage());
        }
    }

    public void addMiNiFiCPPDevice(MiNiFiCPPDevice mjd) {
        try {
            deviceDAO.insertMiNiFiCPPDeviceTransaction(mjd);
        } catch (Exception ex) {
            logger.error("Error inserting MiNiFiCPPDevice object into {} with exception {}", DBConstants.MINIFI_DEVICE_TABLE, ex.getMessage());
        }
    }
}
