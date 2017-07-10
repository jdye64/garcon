package org.apache.nifi.device.registry.resource.c2.dao;

import java.sql.Timestamp;

import org.apache.nifi.device.registry.dao.DBConstants;
import org.apache.nifi.device.registry.resource.c2.core.C2Payload;
import org.apache.nifi.device.registry.resource.c2.dao.impl.C2PayloadMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
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

@RegisterMapper(C2PayloadMapper.class)
public abstract class C2PayloadDAO {

    @Transaction
    public void registerHeartbeat(C2Payload heartbeatPayload) {
        //long deviceId = registerHeartbeat();

    }

    @SqlUpdate("INSERT INTO " + DBConstants.MINIFI_HEARTBEATS_TABLE + "(MINIFI_DEVICE_ID, OPERATION, STATE, HEARTBEAT_TIMESTAMP) " +
            "VALUES (:minifiDeviceId, :operation, :state, :heartbeatTimestamp)")
    public abstract void registerHeartbeat(@Bind("minifiDeviceID") long minifiDeviceId, @Bind("operation") String operation,
            @Bind("state") String state, @Bind("heartbeatTimestamp") Timestamp heartbeatTimestamp);
}
