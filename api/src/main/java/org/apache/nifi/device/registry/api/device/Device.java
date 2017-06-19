package org.apache.nifi.device.registry.api.device;

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
 * Created on 6/18/17.
 */


public class Device {

    // Device networking info
    private String primaryNICMac;           // Used as the unique "ID" for each device is the sprawling flow graph
    private String privateIPAddress;
    private String publicIPAddress;
    private String hostname;

    // Common Hardware info
    private int availableProcessors;
    private long totalSystemMemory;
    private long availableSystemMemory;
    private long consumedMemory;

    public String getPrimaryNICMac() {
        return primaryNICMac;
    }

    public void setPrimaryNICMac(String primaryNICMac) {
        this.primaryNICMac = primaryNICMac;
    }

    public String getPrivateIPAddress() {
        return privateIPAddress;
    }

    public void setPrivateIPAddress(String privateIPAddress) {
        this.privateIPAddress = privateIPAddress;
    }

    public String getPublicIPAddress() {
        return publicIPAddress;
    }

    public void setPublicIPAddress(String publicIPAddress) {
        this.publicIPAddress = publicIPAddress;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getAvailableProcessors() {
        return availableProcessors;
    }

    public void setAvailableProcessors(int availableProcessors) {
        this.availableProcessors = availableProcessors;
    }

    public long getTotalSystemMemory() {
        return totalSystemMemory;
    }

    public void setTotalSystemMemory(long totalSystemMemory) {
        this.totalSystemMemory = totalSystemMemory;
    }

    public long getAvailableSystemMemory() {
        return availableSystemMemory;
    }

    public void setAvailableSystemMemory(long availableSystemMemory) {
        this.availableSystemMemory = availableSystemMemory;
    }

    public long getConsumedMemory() {
        return consumedMemory;
    }

    public void setConsumedMemory(long consumedMemory) {
        this.consumedMemory = consumedMemory;
    }
}
