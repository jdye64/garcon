package org.apache.nifi.device.registry.resource.c2.core.state;


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
 * Created on 7/7/17.
 */


public class C2State {

    @JsonProperty("running")
    private String running;

    @JsonProperty("uptimeMilliseconds")
    private long uptimeMilliseconds;

    public C2State() {
    }

    @JsonProperty("running")
    public String getRunning() {
        return running;
    }

    @JsonProperty("running")
    public void setRunning(String running) {
        this.running = running;
    }

    @JsonProperty("uptimeMilliseconds")
    public long getUptimeMilliseconds() {
        return uptimeMilliseconds;
    }

    @JsonProperty("uptimeMilliseconds")
    public void setUptimeMilliseconds(long uptimeMilliseconds) {
        this.uptimeMilliseconds = uptimeMilliseconds;
    }
}
