package org.apache.nifi.device.registry.dao.device.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.nifi.device.registry.api.device.NiFiDevice;
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
 * Created on 6/7/17.
 */


public class NiFiDeviceMapper
        implements ResultSetMapper<NiFiDevice>
{
    public NiFiDevice map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException
    {
        NiFiDevice nn = new NiFiDevice();
//        fr.setAirTemp(resultSet.getDouble("AIR_TEMP"));
//        fr.setCatchTime(resultSet.getString("CATCH_TIME"));
//        fr.setCatcherName(resultSet.getString("CATCHER_NAME"));
//        fr.setFishRecordId(resultSet.getLong("FISH_RECORD_ID"));
//        fr.setFishType(resultSet.getString("FISH_TYPE"));
//        fr.setFishWeight(resultSet.getDouble("FISH_WEIGHT"));
//        fr.setLatitude(resultSet.getDouble("LATITUDE"));
//        fr.setLongitude(resultSet.getDouble("LONGITUDE"));
//        fr.setWaterTemp(resultSet.getDouble("WATER_TEMP"));
        return nn;
    }
}

