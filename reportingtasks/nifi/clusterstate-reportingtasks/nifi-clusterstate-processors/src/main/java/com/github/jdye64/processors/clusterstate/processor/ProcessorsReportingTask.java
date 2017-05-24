package com.github.jdye64.processors.clusterstate.processor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.controller.status.ProcessorStatus;
import org.apache.nifi.reporting.ReportingContext;

import com.fasterxml.jackson.core.JsonProcessingException;

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


public class ProcessorsReportingTask
    extends AbstractProcessorStateReportingTask {

    public final List<PropertyDescriptor> getSupportedPropertyDescriptors() {
        final List<PropertyDescriptor> descriptors = super.getSupportedPropertyDescriptors();
        return Collections.unmodifiableList(descriptors);
    }

    @Override
    public void onTrigger(ReportingContext reportingContext) {

        List<ProcessorStatus> processors = new ArrayList<>();

        //Recursively adds all process group pressured nested connections
        processors.addAll(recursiveProcessorLocate(reportingContext.getEventAccess().getControllerStatus(), null));

        try {
            getLogger().info("{}", new Object[]{mapper.writeValueAsString(processors)});

            if (reportingContext.getProperty(REST_POSTING_ENABLED).asBoolean()) {
                reportToDeviceRegistry(reportingContext, "/api/v1/processors/status", mapper.writeValueAsString(processors));
            }

        } catch (JsonProcessingException e) {
            getLogger().error("Error Processing processors status JSON: {}", new Object[]{e.getMessage()}, e);
        }

    }
}
