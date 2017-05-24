package org.apache.nifi.device.registry.dto;

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
 * Created on 4/8/17.
 */


public class ConnectionsHUD {

    private String deviceId;        //If null or empty means the metrics are for the entire registry.
    private long backPressuredConnections;  //Number of connections currently experiencing backpressure.
    private long totalConnections;
    private long backPressuredBytes;        //Number of bytes that are currently present in back pressured connections (note not all connections)
    private long backPressuredObjects;      //Number of objects that are current present in back pressured connections (note not all connections)

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public long getBackPressuredConnections() {
        return backPressuredConnections;
    }

    public void setBackPressuredConnections(long backPressuredConnections) {
        this.backPressuredConnections = backPressuredConnections;
    }

    public long getTotalConnections() {
        return totalConnections;
    }

    public void setTotalConnections(long totalConnections) {
        this.totalConnections = totalConnections;
    }

    public long getBackPressuredBytes() {
        return backPressuredBytes;
    }

    public void setBackPressuredBytes(long backPressuredBytes) {
        this.backPressuredBytes = backPressuredBytes;
    }

    public long getBackPressuredObjects() {
        return backPressuredObjects;
    }

    public void setBackPressuredObjects(long backPressuredObjects) {
        this.backPressuredObjects = backPressuredObjects;
    }
}
