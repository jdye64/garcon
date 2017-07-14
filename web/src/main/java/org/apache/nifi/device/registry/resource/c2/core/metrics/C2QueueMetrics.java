package org.apache.nifi.device.registry.resource.c2.core.metrics;


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
 * Created on 7/7/17.
 */


public class C2QueueMetrics {

    @JsonProperty("queue_metrics_id")
    private String queue_metrics_id;

    @JsonProperty("datasize")
    private long dataSize;

    @JsonProperty("datasizemax")
    private long dataSizeMax;

    @JsonProperty("queued")
    private long queued;

    @JsonProperty("queuedmax")
    private long queueMax;

    public C2QueueMetrics() {
    }

    public String getQueueMetricsId() {
        return queue_metrics_id;
    }

    public void setQueueMetricsId(String queue_metrics_id) {
        this.queue_metrics_id = queue_metrics_id;
    }

    public long getDataSize() {
        return dataSize;
    }

    public void setDataSize(long dataSize) {
        this.dataSize = dataSize;
    }

    public long getDataSizeMax() {
        return dataSizeMax;
    }

    public void setDataSizeMax(long dataSizeMax) {
        this.dataSizeMax = dataSizeMax;
    }

    public long getQueued() {
        return queued;
    }

    public void setQueued(long queued) {
        this.queued = queued;
    }

    public long getQueueMax() {
        return queueMax;
    }

    public void setQueueMax(long queueMax) {
        this.queueMax = queueMax;
    }
}
