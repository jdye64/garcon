package org.apache.nifi.device.registry.resource.c2.core;

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
 * Created on 7/7/17.
 */

import org.apache.nifi.device.registry.resource.c2.core.device.DeviceInfo;
import org.apache.nifi.device.registry.resource.c2.core.metrics.C2Metrics;
import org.apache.nifi.device.registry.resource.c2.core.state.C2State;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Payload received from the MiNiFi-CPP C2Agent
 */
public class C2Payload {

    @JsonProperty("DeviceInfo")
    private DeviceInfo deviceInfo;

    @JsonProperty("metrics")
    private C2Metrics metrics;

    @JsonProperty("operation")
    private String operation;

    @JsonProperty("state")
    private C2State state;

    public C2Payload() {
    }

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public C2Metrics getMetrics() {
        return metrics;
    }

    public void setMetrics(C2Metrics metrics) {
        this.metrics = metrics;
    }

    public C2State getState() {
        return state;
    }

    public void setState(C2State state) {
        this.state = state;
    }
}
