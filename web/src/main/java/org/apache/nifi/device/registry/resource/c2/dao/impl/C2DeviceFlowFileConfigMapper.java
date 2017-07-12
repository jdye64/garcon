package org.apache.nifi.device.registry.resource.c2.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.nifi.device.registry.resource.c2.core.config.C2DeviceFlowFileConfig;
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


public class C2DeviceFlowFileConfigMapper
        implements ResultSetMapper<C2DeviceFlowFileConfig>
{
    public C2DeviceFlowFileConfig map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException
    {
        C2DeviceFlowFileConfig ffc = new C2DeviceFlowFileConfig();
        ffc.setDeviceConfigId(resultSet.getLong("DEVICE_CONFIG_ID"));
        ffc.setConfigName(resultSet.getString("NAME"));
        ffc.setConfigDescription(resultSet.getString("DESCRIPTION"));
        ffc.setConfigFile(resultSet.getBytes("CONFIG_FILE"));
        ffc.setCreatedTimestamp(resultSet.getTimestamp("CREATED_TS"));
        ffc.setUpdatedTimestamp(resultSet.getTimestamp("UPDATED_TS"));
        return ffc;
    }
}