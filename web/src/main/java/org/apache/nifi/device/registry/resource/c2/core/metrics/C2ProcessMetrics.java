package org.apache.nifi.device.registry.resource.c2.core.metrics;

import java.util.List;

import org.apache.nifi.device.registry.resource.c2.core.metrics.pm.C2CPUMetrics;
import org.apache.nifi.device.registry.resource.c2.core.metrics.pm.C2MemoryMetrics;
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
 * Created on 7/11/17.
 */


public class C2ProcessMetrics {

    @JsonProperty("MemoryMetrics")
    private List<C2MemoryMetrics> memoryMetrics;

    @JsonProperty("CpuMetrics")
    private List<C2CPUMetrics> cpuMetrics;

    public C2ProcessMetrics() {}

    public List<C2MemoryMetrics> getMemoryMetrics() {
        return memoryMetrics;
    }

    public void setMemoryMetrics(List<C2MemoryMetrics> memoryMetrics) {
        this.memoryMetrics = memoryMetrics;
    }

    public List<C2CPUMetrics> getCpuMetrics() {
        return cpuMetrics;
    }

    public void setCpuMetrics(List<C2CPUMetrics> cpuMetrics) {
        this.cpuMetrics = cpuMetrics;
    }
}
