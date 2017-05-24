/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.jdye64.processors.backpressure;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.nifi.annotation.documentation.CapabilityDescription;
import org.apache.nifi.annotation.documentation.Tags;
import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.controller.status.ConnectionStatus;
import org.apache.nifi.controller.status.ProcessGroupStatus;
import org.apache.nifi.processor.util.StandardValidators;
import org.apache.nifi.reporting.ReportingContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.jdye64.reportingtasks.AbstractDeviceRegistryReportingTask;


@Tags({"backpressure", "reportingtask"})
@CapabilityDescription("Provide a description")
public class BackpressureReportingTask
        extends AbstractDeviceRegistryReportingTask {

    private static final PropertyDescriptor BACKPRESSURE_OBJECT_SIZE_THRESHOLD = new PropertyDescriptor.Builder()
            .name("Backpressure Object Size Threshold")
            .description("Number of objects that if the connection contains more objects than this will be reported")
            .required(true)
            .expressionLanguageSupported(true)
            .defaultValue("1000")
            .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
            .build();

    @Override
    protected List<PropertyDescriptor> getSupportedPropertyDescriptors() {
        final List<PropertyDescriptor> properties = super.getSupportedPropertyDescriptors();
        properties.add(BACKPRESSURE_OBJECT_SIZE_THRESHOLD);
        return properties;
    }

    private List<ConnectionStatus> recursiveConnectionStatusAdd(ProcessGroupStatus status, Integer bpCount) {

        List<ConnectionStatus> pressuredConnections = new ArrayList<>();

        Iterator<ConnectionStatus> itr = status.getConnectionStatus().iterator();
        while (itr.hasNext()) {
            ConnectionStatus cs = itr.next();
            if (cs.getBackPressureObjectThreshold() > bpCount.intValue()) {
                pressuredConnections.add(cs);
            }
        }

        //Loop through all of the process groups and add their connection status
        Iterator<ProcessGroupStatus> pgitr = status.getProcessGroupStatus().iterator();
        while (pgitr.hasNext()) {
            ProcessGroupStatus pgs = pgitr.next();

            //recursive call
            pressuredConnections.addAll(recursiveConnectionStatusAdd(pgs, bpCount));
        }

        return pressuredConnections;
    }

    public void onTrigger(ReportingContext reportingContext) {

        List<ConnectionStatus> pressuredConnections = new ArrayList<>();

        Integer bpCount = new Integer(reportingContext.getProperty(BACKPRESSURE_OBJECT_SIZE_THRESHOLD).evaluateAttributeExpressions().getValue());

        //Recursively adds all process group pressured nested connections
        pressuredConnections.addAll(recursiveConnectionStatusAdd(reportingContext.getEventAccess().getControllerStatus(), bpCount));

        try {
            getLogger().info("{}", new Object[]{mapper.writeValueAsString(pressuredConnections)});

            if (reportingContext.getProperty(REST_POSTING_ENABLED).asBoolean()) {
                reportToDeviceRegistry(reportingContext, "/api/v1/connection/pressured", mapper.writeValueAsString(pressuredConnections));
            }

        } catch (JsonProcessingException e) {
            getLogger().error("Error Processing pressured connections JSON: {}", new Object[]{e.getMessage()}, e);
        }
    }

}
