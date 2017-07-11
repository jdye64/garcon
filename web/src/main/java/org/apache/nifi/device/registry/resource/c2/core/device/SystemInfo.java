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


public class SystemInfo {

    @JsonProperty("machine_arch")
    private String machine_arch;

    @JsonProperty("physicalmem")
    private long physicalMemory;

    @JsonProperty("vcores")
    private int vcores;

    public SystemInfo() {}

    @JsonProperty("machine_arch")
    public String getMachineArchitecture() {
        return machine_arch;
    }

    @JsonProperty("machine_arch")
    public void setMachineArchitecture(String machineArchitecture) {
        this.machine_arch = machineArchitecture;
    }

    @JsonProperty("physicalmem")
    public long getPhysicalMemory() {
        return physicalMemory;
    }

    @JsonProperty("physicalmem")
    public void setPhysicalMemory(long physicalMemory) {
        this.physicalMemory = physicalMemory;
    }

    @JsonProperty("vcores")
    public int getVcores() {
        return vcores;
    }

    @JsonProperty("vcores")
    public void setVcores(int vcores) {
        this.vcores = vcores;
    }
}
