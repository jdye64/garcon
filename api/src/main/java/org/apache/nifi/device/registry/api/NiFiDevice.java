package org.apache.nifi.device.registry.api;

import java.util.Map;

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


public class NiFiDevice
        extends Device {

    private String templateMD5;
    private Map<String, String> nifiProperties;

    public String getTemplateMD5() {
        return templateMD5;
    }

    public void setTemplateMD5(String templateMD5) {
        this.templateMD5 = templateMD5;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("Template MD5: ");
        buf.append(getTemplateMD5());
        buf.append("\nInternal IP Address: ");
        buf.append(getInternalIPAddress());
        buf.append("\nExternal IP Address: ");
        buf.append(getExternalIPAddress());
        buf.append("\nMAC Address: ");
        buf.append(getPrimaryNICMac());
        buf.append("\nCPU Time: ");
        buf.append(getCpuTime());
        buf.append("\nFree Disk Space: ");
        buf.append(getFreeDiskSpace());
        buf.append("\nFree Memory: ");
        buf.append(getFreeMem());
        buf.append("\nNiFi Memory Usage: ");
        buf.append(getProcessMemUsage());
        buf.append("\nTotal Disk Space: ");
        buf.append(getTotalDiskSpace());
        buf.append("\nTotal Memory: ");
        buf.append(getTotalMem());
        return buf.toString();
    }
}
