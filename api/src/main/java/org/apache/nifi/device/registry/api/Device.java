package org.apache.nifi.device.registry.api;

import java.util.Map;

import org.apache.nifi.util.NiFiProperties;

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
    private String hostname;

    // Common Hardware based monitoring
    private int availableProcessors;

    private Map<String, DiskReport> provRepoDiskReport;
    private Map<String, DiskReport> contentRepoDiskReport;
    private DiskReport flowfileRepoDiskReport;
    private DiskReport dbDiskReport;
    private DiskReport rootDiskReport;

    private NiFiProperties niFiProperties;

    public NiFiProperties getNiFiProperties() {
        return niFiProperties;
    }

    public void setNiFiProperties(NiFiProperties niFiProperties) {
        this.niFiProperties = niFiProperties;
    }

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

    public Map<String, DiskReport> getContentRepoDiskReport() {
        return contentRepoDiskReport;
    }

    public void setContentRepoDiskReport(Map<String, DiskReport> contentRepoDiskReport) {
        this.contentRepoDiskReport = contentRepoDiskReport;
    }

    public DiskReport getFlowfileRepoDiskReport() {
        return flowfileRepoDiskReport;
    }

    public void setFlowfileRepoDiskReport(DiskReport flowfileRepoDiskReport) {
        this.flowfileRepoDiskReport = flowfileRepoDiskReport;
    }

    public DiskReport getDbDiskReport() {
        return dbDiskReport;
    }

    public void setDbDiskReport(DiskReport dbDiskReport) {
        this.dbDiskReport = dbDiskReport;
    }

    public DiskReport getRootDiskReport() {
        return rootDiskReport;
    }

    public void setRootDiskReport(DiskReport rootDiskReport) {
        this.rootDiskReport = rootDiskReport;
    }

    public Map<String, DiskReport> getProvRepoDiskReport() {
        return provRepoDiskReport;
    }

    public void setProvRepoDiskReport(Map<String, DiskReport> provRepoDiskReport) {
        this.provRepoDiskReport = provRepoDiskReport;
    }
}
