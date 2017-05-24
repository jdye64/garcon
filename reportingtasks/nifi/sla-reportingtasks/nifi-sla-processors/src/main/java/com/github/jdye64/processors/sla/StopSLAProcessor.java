package com.github.jdye64.processors.sla;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.nifi.annotation.behavior.ReadsAttribute;
import org.apache.nifi.annotation.behavior.ReadsAttributes;
import org.apache.nifi.annotation.documentation.CapabilityDescription;
import org.apache.nifi.annotation.documentation.SeeAlso;
import org.apache.nifi.annotation.documentation.Tags;
import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.flowfile.FlowFile;
import org.apache.nifi.processor.AbstractProcessor;
import org.apache.nifi.processor.ProcessContext;
import org.apache.nifi.processor.ProcessSession;
import org.apache.nifi.processor.ProcessorInitializationContext;
import org.apache.nifi.processor.Relationship;
import org.apache.nifi.processor.exception.ProcessException;
import org.apache.nifi.processor.util.StandardValidators;

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
 * Created on 4/7/17.
 */

@Tags({"stop", "SLA"})
@CapabilityDescription("When invoked will trigger the start of an SLA measurement. That measurement will be persisted in the SLAControllerService" +
        " and remain in the 'RUNNING' state until the StopSLAProcessor is called")
@SeeAlso({StopSLAProcessor.class})
@ReadsAttributes({@ReadsAttribute(attribute="sla.start.timestamp", description="System current Linux epoch timestamp when this processor was invoked.")})
public class StopSLAProcessor
    extends AbstractProcessor {

    static final PropertyDescriptor SLA = new PropertyDescriptor.Builder()
            .name("SLA in milliseconds")
            .description("SLA to be enforced by check the value of 'sla.start.timestamp' and comparing it with the current timestamp when this processor is " +
                    " invoked. If the result is greater than the designated value then the flowfile with be cloned allowing the original flowfile to continue" +
                    " its path but subsequently allowing a sub flow to trigger an alert or audit than an SLA was missed.")
            .expressionLanguageSupported(true)
            .defaultValue("1000")
            .required(true)
            .addValidator(StandardValidators.POSITIVE_LONG_VALIDATOR)
            .build();

    public static final Relationship REL_ORIGINAL = new Relationship.Builder()
            .name("original")
            .description("passthrough relationship so that the flowfile can continue its normal path without interuption")
            .build();

    public static final Relationship REL_SLA_MISSED = new Relationship.Builder()
            .name("missed sla")
            .description("relationship that is invoked if it is determined than an sla has been missed")
            .build();

    public static final Relationship REL_FAILURE = new Relationship.Builder()
            .name("failure")
            .description("failed to start SLA window")
            .build();

    private List<PropertyDescriptor> descriptors;

    private Set<Relationship> relationships;

    @Override
    protected void init(final ProcessorInitializationContext context) {
        final List<PropertyDescriptor> descriptors = new ArrayList<>();
        descriptors.add(SLA);
        this.descriptors = Collections.unmodifiableList(descriptors);

        final Set<Relationship> relationships = new HashSet<>();
        relationships.add(REL_ORIGINAL);
        relationships.add(REL_SLA_MISSED);
        relationships.add(REL_FAILURE);
        this.relationships = Collections.unmodifiableSet(relationships);
    }

    @Override
    public Set<Relationship> getRelationships() {
        return this.relationships;
    }

    @Override
    public final List<PropertyDescriptor> getSupportedPropertyDescriptors() {
        return descriptors;
    }

    @Override
    public void onTrigger(final ProcessContext context, final ProcessSession session) throws ProcessException {
        final FlowFile flowFile = session.get();
        if ( flowFile == null ) {
            return;
        }

        try {

            String timestamp = String.valueOf(System.currentTimeMillis());
            String startTimestamp = flowFile.getAttribute("sla.start.timestamp");
            long diff = (Long.valueOf(timestamp) - Long.valueOf(startTimestamp));

            if (getLogger().isDebugEnabled()) {
                getLogger().debug("Pulling sla.start.timestamp value of {} comparing with current timestamp of {}. Result: {}", new Object[]{startTimestamp, timestamp, diff});
            }

            if (diff > context.getProperty(SLA).evaluateAttributeExpressions().asLong()) {
                FlowFile missedSLAFF = session.clone(flowFile);
                missedSLAFF = session.putAttribute(missedSLAFF, "sla.stop.timestamp", timestamp);
                session.transfer(missedSLAFF, REL_SLA_MISSED);
            }

        } catch (Exception e) {
            e.printStackTrace();
            session.transfer(flowFile, REL_FAILURE);
        } finally {
            session.transfer(flowFile, REL_ORIGINAL);
        }
    }

}
