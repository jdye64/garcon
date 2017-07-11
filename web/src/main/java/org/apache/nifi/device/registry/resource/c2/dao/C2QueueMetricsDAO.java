package org.apache.nifi.device.registry.resource.c2.dao;

import org.apache.nifi.device.registry.dao.DBConstants;
import org.apache.nifi.device.registry.resource.c2.dao.impl.C2QueueMetricsMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
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

@RegisterMapper(C2QueueMetricsMapper.class)
public abstract class C2QueueMetricsDAO {

    @SqlUpdate("INSERT INTO " + DBConstants.C2_QUEUE_METRICS + "(DEVICE_ID, QUEUE_METRICS_ID, DATA_SIZE, DATA_SIZE_MAX, QUEUED, QUEUED_MAX) " +
            "VALUES (:deviceId, :queueMetricsId, :dataSize, :dataSizeMax, :queued, :queuedMax)")
    public abstract void insertQueueMetrics(@Bind("deviceId") String deviceId, @Bind("queueMetricsId") String queueMetricsId,
            @Bind("dataSize") long dataSize, @Bind("dataSizeMax") long dataSizeMax, @Bind("queued") long queued,
            @Bind("queuedMax") long queuedMax);

    @SqlUpdate("UPDATE " + DBConstants.C2_QUEUE_METRICS + " SET DATA_SIZE = :dataSize, DATA_SIZE_MAX = :dataSizeMax, " +
            "QUEUED = :queued, QUEUED_MAX = :queuedMax WHERE DEVICE_ID = :deviceId AND QUEUE_METRICS_ID = :queueMetricsId")
    public abstract void updateQueueMetrics(@Bind("deviceId") String deviceId, @Bind("queueMetricsId") String queueMetricsId,
            @Bind("dataSize") long dataSize, @Bind("dataSizeMax") long dataSizeMax, @Bind("queued") long queued,
            @Bind("queuedMax") long queuedMax);
}
