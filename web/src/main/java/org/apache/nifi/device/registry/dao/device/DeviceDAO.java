package org.apache.nifi.device.registry.dao.device;

import java.util.List;

import org.apache.nifi.device.registry.api.device.Device;
import org.apache.nifi.device.registry.api.device.MiNiFiCPPDevice;
import org.apache.nifi.device.registry.api.device.MiNiFiJavaDevice;
import org.apache.nifi.device.registry.api.device.NiFiDevice;
import org.apache.nifi.device.registry.dao.DBConstants;
import org.apache.nifi.device.registry.dao.cluster.impl.NiFiClusterMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.Transaction;
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
public abstract class DeviceDAO {

    @SqlQuery("SELECT * FROM " + DBConstants.DEVICE_TABLE)
    public abstract List<Device> getAllNiFiDevices();


    @SqlQuery("SELECT * FROM " + DBConstants.DEVICE_TABLE + " WHERE DEVICE_ID = :deviceId")
    public abstract Device getNiFiDeviceById(@Bind("deviceId") String deviceId);

    @SqlQuery("SELECT * FROM " + DBConstants.MINIFI_DEVICE_TABLE + " WHERE DEVICE_ID =:deviceId")
    public abstract MiNiFiCPPDevice getMiNiFiCPPDeviceById(@Bind("deviceId") String deviceId);

    @SqlUpdate("INSERT INTO " + DBConstants.DEVICE_TABLE + "(DEVICE_ID, IP, HOSTNAME, NUM_PROCESSORS, TOTAL_SYS_MEM, AVAIL_SYS_MEM, CONSUMED_SYS_MEM) " +
            "VALUES (:deviceId, :ip, :hostname, :numProcessors, :totalSysMem, :availSysMem, :consumedSysMem)")
    @GetGeneratedKeys
    public abstract long insertDevice(@Bind("deviceId") String pnicMac, @Bind("ip") String privateIP,
            @Bind("hostname") String hostname, @Bind("numProcessors") int numProcessors, @Bind("totalSysMem") long totalSysMem,
            @Bind("availSysMem") long availSysMem, @Bind("consumedSysMem") long consumedSysMem);


    @SqlUpdate("INSERT INTO " + DBConstants.NIFI_DEVICE_TABLE + "(NODE_URI, DEVICE_ID) VALUES (:nodeURI, :deviceID)")
    public abstract  void insertNiFiDevice(@Bind("nodeURI") String nodeURI, @Bind("deviceID") String deviceId);


    @SqlUpdate("INSERT INTO " + DBConstants.MINIFI_DEVICE_TABLE + "(DEVICE_ID, DEVICE_NAME, DEVICE_TYPE) VALUES (:deviceId, :deviceName, :deviceType)")
    public abstract void insertMiNiFiDevice(@Bind("deviceId") String deviceId, @Bind("deviceName") String deviceName, @Bind("deviceType") String deviceType);


    @Transaction
    public NiFiDevice insertNiFiDeviceTransaction(NiFiDevice niFiDevice) {
        insertDevice(niFiDevice.getDeviceId(), niFiDevice.getIp(),
                niFiDevice.getHostname(), niFiDevice.getAvailableProcessors(), niFiDevice.getTotalSystemMemory(),
                niFiDevice.getAvailableSystemMemory(), niFiDevice.getConsumedMemory());
        insertNiFiDevice(niFiDevice.getUri(), niFiDevice.getDeviceId());
        return niFiDevice;
    }

    @Transaction
    public MiNiFiJavaDevice insertMiNiFiJavaDeviceTransaction(MiNiFiJavaDevice md) {
        insertDevice(md.getDeviceId(), md.getIp(),
                md.getHostname(), md.getAvailableProcessors(), md.getTotalSystemMemory(),
                md.getAvailableSystemMemory(), md.getConsumedMemory());
        insertMiNiFiDevice(md.getDeviceId(), md.getDeviceName(), "JAVA");
        return md;
    }


    @Transaction
    public MiNiFiCPPDevice insertMiNiFiCPPDeviceTransaction(MiNiFiCPPDevice md) {
        insertDevice(md.getDeviceId(), md.getIp(),
                md.getHostname(), md.getAvailableProcessors(), md.getTotalSystemMemory(),
                md.getAvailableSystemMemory(), md.getConsumedMemory());
        insertMiNiFiDevice(md.getDeviceId(), md.getDeviceName(), "CPP");
        return md;
    }

}
