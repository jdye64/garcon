package org.apache.nifi.processors.rt;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.nifi.annotation.documentation.CapabilityDescription;
import org.apache.nifi.annotation.documentation.Tags;
import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.device.registry.api.DiskReport;
import org.apache.nifi.device.registry.api.NiFiDevice;
import org.apache.nifi.processor.util.StandardValidators;
import org.apache.nifi.properties.NiFiPropertiesLoader;
import org.apache.nifi.reporting.AbstractReportingTask;
import org.apache.nifi.reporting.ReportingContext;
import org.apache.nifi.util.NiFiProperties;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * Created on 3/13/17.
 */

@Tags({"reporting", "device registry", "IoT"})
@CapabilityDescription("In large IoT based deployments reports back information about this instance to give a NiFi operator a single view of all instances running")
public class DeviceRegistryReportingTask
        extends AbstractReportingTask {

    private static final Logger logger = LoggerFactory.getLogger(DeviceRegistryReportingTask.class);
    private final ObjectMapper mapper = new ObjectMapper();

    private static final PropertyDescriptor DEVICE_REGISTRY_HOST = new PropertyDescriptor.Builder()
            .name("Host")
            .description("NiFi Device Registry service that the metrics will be transported to")
            .required(true)
            .expressionLanguageSupported(true)
            .defaultValue("localhost")
            .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
            .build();

    private static final PropertyDescriptor DEVICE_REGISTRY_PORT = new PropertyDescriptor.Builder()
            .name("Port")
            .description("Port the target NiFi Device Registry is running on")
            .required(true)
            .expressionLanguageSupported(true)
            .defaultValue("8888")
            .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
            .build();

    @Override
    protected List<PropertyDescriptor> getSupportedPropertyDescriptors() {
        final List<PropertyDescriptor> properties = new ArrayList<>();
        properties.add(DEVICE_REGISTRY_HOST);
        properties.add(DEVICE_REGISTRY_PORT);
        return properties;
    }

    public void onTrigger(ReportingContext reportingContext) {
        logger.info("Running DeviceRegistryReportingTask");

        String host = reportingContext.getProperty(DEVICE_REGISTRY_HOST).evaluateAttributeExpressions().getValue();
        String port = reportingContext.getProperty(DEVICE_REGISTRY_PORT).evaluateAttributeExpressions().getValue();

        //Build NiFiDevice object for payload.
        NiFiDevice device = new NiFiDevice();
        device = populateNetworkingInfo(device);
        device = populateMemoryInfo(device);
        device = populateDiskSpaceInfo(reportingContext, device);

        device.setTemplateMD5("MD5 String ....");

        report(host, port, device);
    }

    private boolean report(String host, String port, NiFiDevice device) {

        try {
            String url = "http://" + host + ":" + port + "/device";

            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost postRequest = new HttpPost(url);

            StringEntity input = new StringEntity(this.mapper.writeValueAsString(device));
            input.setContentType("application/json");
            postRequest.setEntity(input);

            HttpResponse response = httpClient.execute(postRequest);

            if (response.getStatusLine().getStatusCode() != 201) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }

            BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

        return true;
    }


    private NiFiDevice populateNetworkingInfo(NiFiDevice device) {

        InetAddress ip;
        try {

            ip = InetAddress.getLocalHost();

            NetworkInterface network = NetworkInterface.getByInetAddress(ip);

            byte[] mac = network.getHardwareAddress();

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? ":" : ""));
            }

            //Set the values to the device object.
            device.setPrimaryNICMac(sb.toString());
            device.setInternalIPAddress(ip.getHostAddress());
            device.setExternalIPAddress(ip.getHostAddress());   //TODO: This should not be this way

            String hostname = InetAddress.getLocalHost().getHostName();
            if (!StringUtils.isEmpty(hostname)) {
                device.setHostname(hostname);
            } else {
                //Try the linux approach ... could fail if hostname(1) system command is not available.
                try {
                    Process process = Runtime.getRuntime().exec("hostname");
                    InputStream is = process.getInputStream();

                    StringWriter writer = new StringWriter();
                    IOUtils.copy(is, writer, "UTF-8");
                    hostname = writer.toString();
                    if (StringUtils.isEmpty(hostname)) {
                        device.setHostname("UNKNOWN");
                    } else {
                        device.setHostname(hostname);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e){
            e.printStackTrace();
        }

        return device;
    }

    private NiFiDevice populateMemoryInfo(NiFiDevice device) {
        device.setAvailableProcessors(Runtime.getRuntime().availableProcessors());
        device.setJvmTotalMemory(Runtime.getRuntime().totalMemory());
        device.setJvmFreeMemory(Runtime.getRuntime().freeMemory());
        device.setJvmMaxMemory(Runtime.getRuntime().maxMemory());
        return device;
    }

    private NiFiDevice populateDiskSpaceInfo(ReportingContext reportingContext, NiFiDevice device) {

        try {
            NiFiProperties properties = NiFiPropertiesLoader.loadDefaultWithKeyFromBootstrap();
            device.setNiFiProperties(properties);

            //Set the disk report
            device.setRootDiskReport(createDiskReportForPath(Paths.get("/")));
            device.setDbDiskReport(createDiskReportForPath(properties.getDatabaseRepositoryPath()));
            device.setFlowfileRepoDiskReport(createDiskReportForPath(properties.getFlowFileRepositoryPath()));

            Map<String, Path> contentPaths = properties.getContentRepositoryPaths();
            Map<String, DiskReport> contentDiskReports = new HashMap<>();
            Iterator<String> itr = contentPaths.keySet().iterator();

            while (itr.hasNext()) {
                String key = itr.next();
                Path path = contentPaths.get(key);
                contentDiskReports.put(key, createDiskReportForPath(path));
            }
            device.setContentRepoDiskReport(contentDiskReports);

            Map<String, Path> provPaths = properties.getProvenanceRepositoryPaths();
            Map<String, DiskReport> provDiskReports = new HashMap<>();
            itr = provDiskReports.keySet().iterator();

            while (itr.hasNext()) {
                String key = itr.next();
                Path path = provPaths.get(key);
                provDiskReports.put(key, createDiskReportForPath(path));
            }
            device.setProvRepoDiskReport(provDiskReports);


        } catch (IOException e) {
            e.printStackTrace();
        }

        return device;
    }

    private DiskReport createDiskReportForPath(Path path) {
        DiskReport report = new DiskReport();

        File f = path.toFile();
        if (f.exists()) {
            report.setAvailableBytes(f.getFreeSpace());
            report.setPath(path.toString());
            report.setTotalBytes(f.getTotalSpace());
            report.setUsedBytes(f.getUsableSpace());
        } else {
            //File doesn't exist so null out all information and flag that.
        }

        return report;
    }
}
