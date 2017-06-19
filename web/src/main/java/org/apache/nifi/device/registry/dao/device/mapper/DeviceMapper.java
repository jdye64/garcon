package org.apache.nifi.device.registry.dao.device.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.nifi.device.registry.api.device.Device;
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
 * Created on 6/18/17.
 */


public class DeviceMapper
        implements ResultSetMapper<Device>
{
    public Device map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException
    {
        Device mn = new Device();
//        nc.setId(resultSet.getLong("CLUSTER_ID"));
//        nc.setName(resultSet.getString("CLUSTER_NAME"));
//        nc.setDescription(resultSet.getString("CLUSTER_DESC"));
//        nc.setHostname(resultSet.getString("CLUSTER_HOSTNAME"));
//        nc.setPort(resultSet.getString("CLUSTER_PORT"));
        return mn;
    }
}

