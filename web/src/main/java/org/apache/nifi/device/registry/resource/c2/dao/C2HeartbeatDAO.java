package org.apache.nifi.device.registry.resource.c2.dao;

import java.util.List;

import org.apache.nifi.device.registry.dao.DBConstants;
import org.apache.nifi.device.registry.resource.c2.core.C2Heartbeat;
import org.apache.nifi.device.registry.resource.c2.dao.impl.C2HeartbeatMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

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
 * Created on 7/11/17.
 */

@RegisterMapper(C2HeartbeatMapper.class)
public abstract class C2HeartbeatDAO {

    @SqlUpdate("INSERT INTO " + DBConstants.C2_HEARTBEATS + "(DEVICE_ID, OPERATION, RUNNING, UPTIME, HEARTBEAT_TIMESTAMP) " +
            "VALUES (:deviceId, :operation, :running, :uptime, NOW())")
    public abstract void registerHeartbeat(@Bind("deviceId") String deviceId, @Bind("operation") String operation, @Bind("running") boolean running,
            @Bind("uptime") long uptime);

    @SqlUpdate("UPDATE " + DBConstants.C2_HEARTBEATS + " SET OPERATION = :operation, RUNNING = :running, UPTIME = :uptime, HEARTBEAT_TIMESTAMP = NOW()" +
            " WHERE DEVICE_ID = :deviceId")
    public abstract void udpateHeartbeat(@Bind("deviceId") String deviceId, @Bind("operation") String operation, @Bind("running") boolean running,
            @Bind("uptime") long uptime);

    @SqlQuery("SELECT * FROM " + DBConstants.C2_HEARTBEATS)
    public abstract List<C2Heartbeat> getLatestDeviceHeartbeat();
}
