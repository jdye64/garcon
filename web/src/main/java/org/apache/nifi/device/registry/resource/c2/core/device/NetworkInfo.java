package org.apache.nifi.device.registry.resource.c2.core.device;


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


public class NetworkInfo {

    @JsonProperty("deviceid")
    private String deviceid;

    @JsonProperty("ip")
    private String ip;

    @JsonProperty("hostname")
    private String hostname;

    public NetworkInfo() {}

    @JsonProperty("deviceid")
    public String getDeviceid() {
        return deviceid;
    }

    @JsonProperty("deviceid")
    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    @JsonProperty("ip")
    public String getIp() {
        return ip;
    }

    @JsonProperty("ip")
    public void setIp(String ip) {
        this.ip = ip;
    }

    @JsonProperty("hostname")
    public String getHostname() {
        return hostname;
    }

    @JsonProperty("hostname")
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
}
