package org.apache.nifi.device.registry.resource.c2.core.device;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;


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
 * Created on 7/10/17.
 */


public class DeviceInfo {

    @JsonProperty("NetworkInfo")
    private List<NetworkInfo> networkInfo;

    @JsonProperty("SystemInformation")
    private List<SystemInfo> systemInfo;

    public DeviceInfo(){}

    @JsonProperty("NetworkInfo")
    public List<NetworkInfo> getNetworkInfo() {
        return networkInfo;
    }

    @JsonProperty("NetworkInfo")
    public void setNetworkInfo(List<NetworkInfo> networkInfo) {
        this.networkInfo = networkInfo;
    }

    @JsonProperty("SystemInformation")
    public List<SystemInfo> getSystemInfo() {
        return systemInfo;
    }

    @JsonProperty("SystemInformation")
    public void setSystemInfo(List<SystemInfo> systemInfo) {
        this.systemInfo = systemInfo;
    }
}
