package org.apache.nifi.device.registry.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.nifi.device.registry.api.Device;
import org.apache.nifi.device.registry.dao.DeviceDAO;

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
 * Created on 3/16/17.
 */


public class DeviceDAOImpl
    implements DeviceDAO {

    List<Device> db = new ArrayList<Device>();

    public Device insert(Device device) {
        for (Device d : db) {
            if (d.getPrimaryNICMac().equalsIgnoreCase(device.getPrimaryNICMac())) {
                //Device already exists in db.
                return device;
            }
        }
        db.add(device);
        return device;
    }

    public List<Device> findAll() {
        return db;
    }
}
