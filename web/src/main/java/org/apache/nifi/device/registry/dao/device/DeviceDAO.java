package org.apache.nifi.device.registry.dao.device;

import java.util.List;

import org.apache.nifi.device.registry.api.device.Device;
import org.apache.nifi.device.registry.dao.DBConstants;
import org.apache.nifi.device.registry.dao.cluster.impl.NiFiClusterMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;


/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Created on 3/16/17.
 */

@RegisterMapper(NiFiClusterMapper.class)
public interface DeviceDAO {

    @SqlQuery("SELECT * FROM " + DBConstants.DEVICE_TABLE)
    List<Device> getAllDevices();

    @SqlQuery("SELECT * FROM " + DBConstants.DEVICE_TABLE + " WHERE DEVICE_ID = :deviceId")
    Device getDeviceById(@Bind("deviceId") String deviceId);
}
