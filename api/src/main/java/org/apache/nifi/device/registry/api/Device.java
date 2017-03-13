package org.apache.nifi.device.registry.api;

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
 * Created on 3/10/17.
 */


public abstract class Device {

    private String internalIPAddress;
    private String externalIPAddress;
    private String primaryNICMac;

    // Common Hardware based monitoring
    private String cpuTime;
    private String usedMem;
    private String freeMem;
    private String totalMem;
    private String processMemUsage;
    private String totalDiskSpace;
    private String freeDiskSpace;

    public String getInternalIPAddress() {
        return internalIPAddress;
    }

    public void setInternalIPAddress(String internalIPAddress) {
        this.internalIPAddress = internalIPAddress;
    }

    public String getExternalIPAddress() {
        return externalIPAddress;
    }

    public void setExternalIPAddress(String externalIPAddress) {
        this.externalIPAddress = externalIPAddress;
    }

    public String getPrimaryNICMac() {
        return primaryNICMac;
    }

    public void setPrimaryNICMac(String primaryNICMac) {
        this.primaryNICMac = primaryNICMac;
    }

    public String getCpuTime() {
        return cpuTime;
    }

    public void setCpuTime(String cpuTime) {
        this.cpuTime = cpuTime;
    }

    public String getUsedMem() {
        return usedMem;
    }

    public void setUsedMem(String usedMem) {
        this.usedMem = usedMem;
    }

    public String getFreeMem() {
        return freeMem;
    }

    public void setFreeMem(String freeMem) {
        this.freeMem = freeMem;
    }

    public String getTotalMem() {
        return totalMem;
    }

    public void setTotalMem(String totalMem) {
        this.totalMem = totalMem;
    }

    public String getProcessMemUsage() {
        return processMemUsage;
    }

    public void setProcessMemUsage(String processMemUsage) {
        this.processMemUsage = processMemUsage;
    }

    public String getTotalDiskSpace() {
        return totalDiskSpace;
    }

    public void setTotalDiskSpace(String totalDiskSpace) {
        this.totalDiskSpace = totalDiskSpace;
    }

    public String getFreeDiskSpace() {
        return freeDiskSpace;
    }

    public void setFreeDiskSpace(String freeDiskSpace) {
        this.freeDiskSpace = freeDiskSpace;
    }
}
