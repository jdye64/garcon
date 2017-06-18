package org.apache.nifi.device.registry.api.monitor;

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

import java.util.List;

/**
 * Defines a Metric's threshold that should be monitored by Garcon. A Threshold will have ThresholdReactions that are triggered if the threshold is actually
 * reached.
 */
public class MetricThreshold {

    private long id;
    private long clusterId;     //Defines the cluster that this MetricsThreshold belongs to.
    private String name;
    private String description;
    private String componentId; // NiFi GUID for the connection, processor, controller, etc that this monitor action should query the API for
    private String apiMethod;
    private List<ThresholdReaction> thresholdReactions;

    public MetricThreshold() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getClusterId() {
        return clusterId;
    }

    public void setClusterId(long clusterId) {
        this.clusterId = clusterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public String getApiMethod() {
        return apiMethod;
    }

    public void setApiMethod(String apiMethod) {
        this.apiMethod = apiMethod;
    }

    public List<ThresholdReaction> getThresholdReactions() {
        return thresholdReactions;
    }

    public void setThresholdReactions(List<ThresholdReaction> thresholdReactions) {
        this.thresholdReactions = thresholdReactions;
    }
}
