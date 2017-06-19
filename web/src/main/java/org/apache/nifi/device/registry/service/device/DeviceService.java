package org.apache.nifi.device.registry.service.device;

import java.util.List;

import org.apache.nifi.device.registry.api.device.Device;
import org.apache.nifi.device.registry.api.device.MiNiFiCPPDevice;
import org.apache.nifi.device.registry.api.device.MiNiFiJavaDevice;
import org.apache.nifi.device.registry.api.device.NiFiDevice;

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


public interface DeviceService {

    List<Device> getNiFiDevices();

    Device getNiFiDeviceById(String deviceId);

    void addNiFiDevice(NiFiDevice niFiDevice);

    void addMiNiFiCPPDevice(MiNiFiCPPDevice miNiFiCPPDevice);

    void addMiNiFiJavaDevice(MiNiFiJavaDevice miNiFiJavaDevice);
}
