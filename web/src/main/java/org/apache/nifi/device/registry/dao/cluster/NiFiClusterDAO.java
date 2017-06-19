package org.apache.nifi.device.registry.dao.cluster;

import java.util.List;

import org.apache.nifi.device.registry.api.cluster.NiFiCluster;
import org.apache.nifi.device.registry.dao.cluster.impl.NiFiClusterMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
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
 * Created on 6/7/17.
 */

@RegisterMapper(NiFiClusterMapper.class)
public interface NiFiClusterDAO {

    @SqlQuery("select * from NIFI_CLUSTER")
    List<NiFiCluster> getAllManagedNiFiClusters();

    @SqlQuery("SELECT * FROM NIFI_CLUSTER WHERE CLUSTER_ID = :clusterId")
    NiFiCluster getClusterById(@Bind("clusterId")long clusterId);
}
