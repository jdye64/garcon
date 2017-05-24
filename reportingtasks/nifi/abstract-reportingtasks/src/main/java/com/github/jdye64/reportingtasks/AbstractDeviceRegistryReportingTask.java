package com.github.jdye64.reportingtasks;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.processor.util.StandardValidators;
import org.apache.nifi.reporting.AbstractReportingTask;
import org.apache.nifi.reporting.ReportingContext;

import com.fasterxml.jackson.databind.ObjectMapper;

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
 * Created on 4/5/17.
 */


public abstract class AbstractDeviceRegistryReportingTask
    extends AbstractReportingTask {

    protected final ObjectMapper mapper = new ObjectMapper();

    protected static final PropertyDescriptor REST_POSTING_ENABLED = new PropertyDescriptor.Builder()
            .name("POST pressured connections to NiFi Device Registry")
            .description("If true the JSON payload for the pressured connections will be POSTed to the NiFi Device Registry UI")
            .required(true)
            .defaultValue("false")
            .allowableValues("true", "false")
            .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
            .build();

    protected static final PropertyDescriptor DEVICE_REGISTRY_HOST = new PropertyDescriptor.Builder()
            .name("Host")
            .description("NiFi Device Registry service that the metrics will be transported to")
            .required(true)
            .expressionLanguageSupported(true)
            .defaultValue("localhost")
            .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
            .build();

    protected static final PropertyDescriptor DEVICE_REGISTRY_PORT = new PropertyDescriptor.Builder()
            .name("Port")
            .description("Port the target NiFi Device Registry is running on")
            .required(true)
            .expressionLanguageSupported(true)
            .defaultValue("8888")
            .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
            .build();

    protected List<PropertyDescriptor> getSupportedPropertyDescriptors() {
        final List<PropertyDescriptor> properties = new ArrayList<PropertyDescriptor>();
        properties.add(REST_POSTING_ENABLED);
        properties.add(DEVICE_REGISTRY_HOST);
        properties.add(DEVICE_REGISTRY_PORT);
        return properties;
    }

    protected void reportToDeviceRegistry(ReportingContext reportingContext, String uri, String jsonString) {

        String host = reportingContext.getProperty(DEVICE_REGISTRY_HOST).evaluateAttributeExpressions().getValue();
        String port = reportingContext.getProperty(DEVICE_REGISTRY_PORT).evaluateAttributeExpressions().getValue();

        try {

            String url = "http://" + host + ":" + port + uri;

            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost postRequest = new HttpPost(url);

            StringEntity input = new StringEntity(jsonString);
            input.setContentType("application/json");
            postRequest.setEntity(input);

            HttpResponse response = httpClient.execute(postRequest);

            BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));

            String output;
            while ((output = br.readLine()) != null) {
                if (getLogger().isDebugEnabled()){
                    getLogger().debug("NiFi Device Registry Response: {}", new Object[]{output});
                }
            }

            //Closes the BufferedReader
            br.close();

        } catch (Exception ex) {
            getLogger().error("Error POSTing Workflow pressured connections to NiFi Device Registry {}:{}", new Object[]{host, port}, ex);
        }
    }
}
