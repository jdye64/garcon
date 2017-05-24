package com.github.jdye64.processors.provenance;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.io.IOUtils;
import org.apache.nifi.annotation.documentation.CapabilityDescription;
import org.apache.nifi.annotation.documentation.Tags;
import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.flowfile.FlowFile;
import org.apache.nifi.flowfile.attributes.CoreAttributes;
import org.apache.nifi.processor.AbstractProcessor;
import org.apache.nifi.processor.ProcessContext;
import org.apache.nifi.processor.ProcessSession;
import org.apache.nifi.processor.ProcessorInitializationContext;
import org.apache.nifi.processor.Relationship;
import org.apache.nifi.processor.exception.ProcessException;
import org.apache.nifi.processor.io.InputStreamCallback;
import org.apache.nifi.processor.io.OutputStreamCallback;
import org.apache.nifi.processor.util.StandardValidators;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.common.collect.Maps;

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
 * Created on 4/12/17.
 */


@Tags({"provenance", "events", "phoenix", "hbase"})
@CapabilityDescription("Preps incoming provenance event data from another NiFi clusters Site2SiteReportingTask output. That input is split into individual records," +
        " attributes are extracted into a more relational friendly model, and then prepared sql statement is written to the outgoing flowfile content body." +
        " Flowfiles exiting this processor are prepared to be written directly into Apache Phoenix. Please note the output from this processor will only" +
        " work with Apache Phoenix and no other relational data store because of the nested Array types it supports.")
public class ProvenanceEventsToPhoenix
    extends AbstractProcessor {

    public static final PropertyDescriptor PHOENIX_TABLE_NAME = new PropertyDescriptor.Builder()
            .name("Phoenix Table Name")
            .description("Name of the Phoenix table that the provenance events should be inserted into.")
            .expressionLanguageSupported(true)
            .required(true)
            .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
            .build();

    public static final Relationship REL_SUCCESS = new Relationship.Builder()
            .name("success")
            .description("successfully to convert provenance event into Phoenix SQL prepared statement")
            .build();

    public static final Relationship REL_FAILURE = new Relationship.Builder()
            .name("failure")
            .description("failed to convert provenance event into Phoenix SQL prepared statement")
            .build();

    private List<PropertyDescriptor> descriptors;

    private Set<Relationship> relationships;

    @Override
    protected void init(final ProcessorInitializationContext context) {
        final List<PropertyDescriptor> descriptors = new ArrayList<>();
        descriptors.add(PHOENIX_TABLE_NAME);
        this.descriptors = Collections.unmodifiableList(descriptors);

        final Set<Relationship> relationships = new HashSet<>();
        relationships.add(REL_SUCCESS);
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

    private static final String PREPEND_UPSERT = "UPSERT INTO ";

    //Since the provenance events are all the same we can safely do this since this processor doesn't handle generic SQL but rather JSON of an expected format.
    private static final String PROV_UPSERT_PREDICATES = " (EVENTID, EVENTORDINAL, EVENTTYPE, TIMESTAMPMILLIS, TIMESTAMP, DURATIONMILLIS, LINEAGESTART, DETAILS, " +
            "COMPONENTID, COMPONENTTYPE, COMPONENTNAME, ENTITYID, ENTITYTYPE, ENTITYSIZE, UPDATEDATTRIBUTES, PREVIOUSATTRIBUTES, ACTORHOSTNAME, CONTENTURI, " +
            "PREVIOUSCONTENTURI, PARENTIDS, CHILDIDS, PLATFORM, APPLICATION, UUID)";
    private static final String PROV_PREP_VALUES = " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    @Override
    public void onTrigger(ProcessContext context, ProcessSession session) throws ProcessException {

        final FlowFile flowFile = session.get();
        if ( flowFile == null ) {
            return;
        }

        String tableName = context.getProperty(PHOENIX_TABLE_NAME).evaluateAttributeExpressions(flowFile).getValue();

        StringBuffer buffer = new StringBuffer();
        buffer.append(PREPEND_UPSERT);
        buffer.append(tableName);
        buffer.append(PROV_UPSERT_PREDICATES);
        buffer.append(PROV_PREP_VALUES);

        try {

            AtomicReference<JSONArray> provEvents = new AtomicReference<>();

            session.read(flowFile, new InputStreamCallback() {
                @Override
                public void process(InputStream in) throws IOException {
                    StringWriter writer = new StringWriter();
                    IOUtils.copy(in, writer, "UTF-8");
                    provEvents.set(new JSONArray(writer.toString()));
                }
            });

            JSONArray events = provEvents.get();

            for (int i = 0; i < events.length(); i++) {
                JSONObject obj = events.getJSONObject(i);
                Map<String, String> attributes = generateAttributes(obj, tableName);

                FlowFile ff = session.write(session.clone(flowFile), new OutputStreamCallback(){
                    @Override
                    public void process(OutputStream out) throws IOException {
                        out.write(buffer.toString().getBytes());
                    }
                });

                ff = session.putAllAttributes(ff, attributes);
                session.transfer(ff, REL_SUCCESS);
            }

            session.remove(flowFile);

        } catch (Exception ex) {
            getLogger().error("Error converting provenance event into Phoenix prepared statement {}", new Object[]{ex.getMessage()}, ex);
            session.transfer(flowFile, REL_FAILURE);
        }
    }


    private final  Map<String, String> generateAttributes(JSONObject obj, String tableName) {
        //Generate all of the update values for the prepared statement
        Map<String, String> attributes = Maps.newHashMapWithExpectedSize(48);

        attributes.put(CoreAttributes.MIME_TYPE.key(), "text/plain");
        attributes.put("sql.table", tableName);
        attributes.put("sql.args.1.type", "12");
        attributes.put("sql.args.1.value", obj.getString("eventId"));
        attributes.put("sql.args.2.type", "12");
        attributes.put("sql.args.2.value", String.valueOf(obj.getLong("eventOrdinal")));
        attributes.put("sql.args.3.type", "12");
        attributes.put("sql.args.3.value", obj.getString("eventType"));
        attributes.put("sql.args.4.type", "12");
        attributes.put("sql.args.4.value", String.valueOf(obj.getLong("timestampMillis")));
        attributes.put("sql.args.5.type", "12");
        attributes.put("sql.args.5.value", obj.getString("timestamp"));
        attributes.put("sql.args.6.type", "12");
        attributes.put("sql.args.6.value", String.valueOf(obj.getLong("durationMillis")));
        attributes.put("sql.args.7.type", "12");
        attributes.put("sql.args.7.value", String.valueOf(obj.getLong("lineageStart")));
        attributes.put("sql.args.8.type", "12");
        String details = "";
        if (obj.has("details")) {
            details = obj.getString("details");
        }
        attributes.put("sql.args.8.value", details);
        attributes.put("sql.args.9.type", "12");
        attributes.put("sql.args.9.value", obj.getString("componentId"));
        attributes.put("sql.args.10.type", "12");
        attributes.put("sql.args.10.value", obj.getString("componentType"));
        attributes.put("sql.args.11.type", "12");
        attributes.put("sql.args.11.value", obj.getString("componentName"));
        attributes.put("sql.args.12.type", "12");
        attributes.put("sql.args.12.value", obj.getString("entityId"));
        attributes.put("sql.args.13.type", "12");
        attributes.put("sql.args.13.value", obj.getString("entityType"));
        attributes.put("sql.args.14.type", "12");
        attributes.put("sql.args.14.value", String.valueOf(obj.getLong("entitySize")));
        attributes.put("sql.args.15.type", "12");
        attributes.put("sql.args.15.value", obj.getJSONObject("updatedAttributes").toString());
        attributes.put("sql.args.16.type", "12");
        attributes.put("sql.args.16.value", obj.getJSONObject("previousAttributes").toString());
        attributes.put("sql.args.17.type", "12");
        attributes.put("sql.args.17.value", obj.getString("actorHostname"));
        attributes.put("sql.args.18.type", "12");
        attributes.put("sql.args.18.value", obj.getString("contentURI"));
        attributes.put("sql.args.19.type", "12");
        attributes.put("sql.args.19.value", obj.getString("previousContentURI"));
        attributes.put("sql.args.20.type", "12");
        attributes.put("sql.args.20.value", obj.getJSONArray("parentIds").toString());
        attributes.put("sql.args.21.type", "12");
        attributes.put("sql.args.21.value", obj.getJSONArray("childIds").toString());
        attributes.put("sql.args.22.type", "12");
        attributes.put("sql.args.22.value", obj.getString("platform"));
        attributes.put("sql.args.23.type", "12");
        attributes.put("sql.args.23.value", obj.getString("application"));
        attributes.put("sql.args.24.type", "12");

        //Gets the UUID of the flowfile
        String uuid = null;
        if (obj.getJSONObject("updatedAttributes").has("uuid")) {
            uuid = obj.getJSONObject("updatedAttributes").getString("uuid");
        } else {
            uuid = obj.getJSONObject("previousAttributes").getString("uuid");
        }

        attributes.put("sql.args.24.value", uuid);

        return attributes;
    }
}
