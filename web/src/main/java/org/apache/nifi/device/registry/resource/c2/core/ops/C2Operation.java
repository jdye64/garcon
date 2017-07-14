package org.apache.nifi.device.registry.resource.c2.core.ops;


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
 * Created on 7/10/17.
 */


public class C2Operation {

    @JsonProperty("operationid")
    private long operationId;

    @JsonProperty("operation")
    private String operation;

    @JsonProperty("name")
    private String name;

    @JsonProperty("acked")
    private boolean acked;

    @JsonProperty("lastResponseTimestamp")
    private Timestamp lastResponseTimestamp;

    @JsonProperty("ackedTimestamp")
    private Timestamp ackedTimestamp;

    public C2Operation() {}

    public long getOperationId() {
        return operationId;
    }

    public void setOperationId(long operationId) {
        this.operationId = operationId;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAcked() {
        return acked;
    }

    public void setAcked(boolean acked) {
        this.acked = acked;
    }

    public Timestamp getLastResponseTimestamp() {
        return lastResponseTimestamp;
    }

    public void setLastResponseTimestamp(Timestamp lastResponseTimestamp) {
        this.lastResponseTimestamp = lastResponseTimestamp;
    }

    public Timestamp getAckedTimestamp() {
        return ackedTimestamp;
    }

    public void setAckedTimestamp(Timestamp ackedTimestamp) {
        this.ackedTimestamp = ackedTimestamp;
    }
}
