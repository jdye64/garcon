package org.apache.nifi.device.registry.resource.c2.core.config;


import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

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


public class C2DeviceFlowFileConfig {

    @JsonProperty("deviceConfigId")
    private long deviceConfigId;

    @JsonProperty("configName")
    private String configName;

    @JsonProperty("configDescription")
    private String configDescription;

    @JsonProperty("configFile")
    private byte[] configFile;

    @JsonProperty("createdTimestamp")
    private Timestamp createdTimestamp;

    @JsonProperty("updatedTimestamp")
    private Timestamp updatedTimestamp;

    public C2DeviceFlowFileConfig() {}

    public long getDeviceConfigId() {
        return deviceConfigId;
    }

    public void setDeviceConfigId(long deviceConfigId) {
        this.deviceConfigId = deviceConfigId;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getConfigDescription() {
        return configDescription;
    }

    public void setConfigDescription(String configDescription) {
        this.configDescription = configDescription;
    }

    public byte[] getConfigFile() {
        return configFile;
    }

    public void setConfigFile(byte[] configFile) {
        this.configFile = configFile;
    }

    public Timestamp getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Timestamp createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public Timestamp getUpdatedTimestamp() {
        return updatedTimestamp;
    }

    public void setUpdatedTimestamp(Timestamp updatedTimestamp) {
        this.updatedTimestamp = updatedTimestamp;
    }
}
