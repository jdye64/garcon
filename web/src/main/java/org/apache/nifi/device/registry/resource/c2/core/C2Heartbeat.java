package org.apache.nifi.device.registry.resource.c2.core;

import java.sql.Timestamp;

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


public class C2Heartbeat {

    private long heartBeatId;
    private String deviceId;
    private String operation;
    private boolean running;
    private Timestamp heartbeatTimestamp;

    public C2Heartbeat() {}

    public long getHeartBeatId() {
        return heartBeatId;
    }

    public void setHeartBeatId(long heartBeatId) {
        this.heartBeatId = heartBeatId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public Timestamp getHeartbeatTimestamp() {
        return heartbeatTimestamp;
    }

    public void setHeartbeatTimestamp(Timestamp heartbeatTimestamp) {
        this.heartbeatTimestamp = heartbeatTimestamp;
    }
}
