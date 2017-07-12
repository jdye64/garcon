package org.apache.nifi.device.registry.resource.c2.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.nifi.device.registry.resource.c2.core.metrics.C2QueueMetrics;
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


public class C2QueueMetricsMapper
        implements ResultSetMapper<C2QueueMetrics> {

    public C2QueueMetrics map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException
    {
        C2QueueMetrics qm = new C2QueueMetrics();
        //qm.setName(resultSet.getString("QUEUE_METRICS_ID"));
        qm.setDataSize(resultSet.getLong("DATA_SIZE"));
        qm.setDataSizeMax(resultSet.getLong("DATA_SIZE_MAX"));
        qm.setQueued(resultSet.getLong("QUEUED"));
        qm.setQueueMax(resultSet.getLong("QUEUED_MAX"));
        return qm;
    }
}
