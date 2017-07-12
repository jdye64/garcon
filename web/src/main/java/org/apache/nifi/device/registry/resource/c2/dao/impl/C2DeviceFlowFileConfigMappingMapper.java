package org.apache.nifi.device.registry.resource.c2.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.nifi.device.registry.resource.c2.core.config.C2DeviceFlowFileConfigMapping;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

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


public class C2DeviceFlowFileConfigMappingMapper
        implements ResultSetMapper<C2DeviceFlowFileConfigMapping>
{
    public C2DeviceFlowFileConfigMapping map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException
    {
        C2DeviceFlowFileConfigMapping ffc = new C2DeviceFlowFileConfigMapping();
        ffc.setFfConfigMappingId(resultSet.getLong("DEVICE_CONFIG_MAPPING_ID"));
        ffc.setDeviceId(resultSet.getString("DEVICE_ID"));
        ffc.setFfConfigId(resultSet.getLong("DEVICE_CONFIG_ID"));
        ffc.setConfiguredTimestamp(resultSet.getTimestamp("CONFIGURED_TS"));
        ffc.setDeployedTimestamp(resultSet.getTimestamp("DEPLOYED_TS"));
        return ffc;
    }
}