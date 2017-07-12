package org.apache.nifi.device.registry.resource.c2.dto;

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


public class C2HUD {

    private long totalDevices;      //Could probably just add running and stopped on JS side ...
    private long runningDevices;
    private long stoppedDevices;

    public C2HUD() {}

    public long getTotalDevices() {
        return totalDevices;
    }

    public void setTotalDevices(long totalDevices) {
        this.totalDevices = totalDevices;
    }

    public long getRunningDevices() {
        return runningDevices;
    }

    public void setRunningDevices(long runningDevices) {
        this.runningDevices = runningDevices;
    }

    public long getStoppedDevices() {
        return stoppedDevices;
    }

    public void setStoppedDevices(long stoppedDevices) {
        this.stoppedDevices = stoppedDevices;
    }
}
