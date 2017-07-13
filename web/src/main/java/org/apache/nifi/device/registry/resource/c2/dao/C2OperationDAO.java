package org.apache.nifi.device.registry.resource.c2.dao;

import java.util.List;

import org.apache.nifi.device.registry.dao.DBConstants;
import org.apache.nifi.device.registry.resource.c2.core.ops.C2Operation;
import org.apache.nifi.device.registry.resource.c2.dao.impl.C2OperationMapper;
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
 * Created on 7/12/17.
 */

@RegisterMapper(C2OperationMapper.class)
public abstract class C2OperationDAO {

    @SqlQuery("SELECT * FROM " + DBConstants.C2_OPERATIONS + " WHERE DEVICE_ID = :deviceId AND ACKED = 0")
    public abstract List<C2Operation> getPendingOperationsForDevice(@Bind("deviceId") String deviceId);

    @SqlQuery("SELECT * FROM " + DBConstants.C2_OPERATIONS + " WHERE DEVICE_ID = :deviceId")
    public abstract List<C2Operation> getDeviceOperationHistory(@Bind("deviceId") String deviceId);

    @SqlUpdate("UPDATE " + DBConstants.C2_OPERATIONS + " SET ACKED = 1, ACK_TIMESTAMP = NOW() WHERE OPERATION_ID = :operationId")
    public abstract void ackOperation(@Bind("operationId") long operationId);
}
