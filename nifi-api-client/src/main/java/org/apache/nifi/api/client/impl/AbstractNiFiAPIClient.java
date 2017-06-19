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

package org.apache.nifi.api.client.impl;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.nifi.web.api.ApplicationResource;
import org.apache.nifi.web.api.entity.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

public abstract class AbstractNiFiAPIClient {

    public static final Logger logger = LoggerFactory.getLogger(AbstractNiFiAPIClient.class);

    private String server;
    private String port;
    private String baseUrl;
    private HttpClient client;
    private ObjectMapper mapper;

    protected void setupClient(String server, String port) {
        this.server = server;
        this.port = port;
        this.baseUrl = "http://" + this.server + ":" + this.port + "/nifi-api";
        this.client = HttpClientBuilder.create().build();
        this.mapper = new ObjectMapper();
        this.mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss Z");
        this.mapper.setDateFormat(dateFormat);
    }

    @Deprecated
    public String get(String nifiCommand) {
        return "Don't use this anymore!!!";
    }

    /**
     * Replaces the javax {X} path variables with the actual values that should be in the URL request.
     *
     * @param currentUri
     *  The current URI with the {x} values
     *
     * @param pathParams
     *  The map of key/value pairs representing the new values that should be replaced
     *
     * @return
     *  The updated URI string with the real invocation values
     */
    private String replaceUriWithPathParams(String currentUri, Map<String, String> pathParams) {
        System.out.println("Current: " + currentUri);

        //Iterates through all of the path params
        if (pathParams != null) {
            Iterator<String> itr = pathParams.keySet().iterator();
            while (itr.hasNext()) {
                String key = itr.next();
                String replaceKey = "{" + key + "}";
                currentUri = currentUri.replace(replaceKey, pathParams.get(key));
            }
        }

        return currentUri;
    }

    public <U extends Entity> Object get(Class<? extends ApplicationResource> resourceClass,
            Method nifiApiMethod,
            U u,
            Map<String, String> pathParams) {

        StringBuilder stringBuilder = new StringBuilder(this.baseUrl);
        stringBuilder.append(resourceClass.getAnnotation(Path.class).value());
        stringBuilder.append("/");
        stringBuilder.append(nifiApiMethod.getAnnotation(Path.class).value());

        String fullRequest = replaceUriWithPathParams(stringBuilder.toString(), pathParams);

        HttpGet request = new HttpGet(fullRequest);

        try {

            // add request header
            String[] consumes = nifiApiMethod.getAnnotation(Consumes.class).value();
            String[] contentType = nifiApiMethod.getAnnotation(Produces.class).value();

            if (consumes != null && consumes.length > 0) {
                request.addHeader("Accept", consumes[0]);
            } else {
                request.addHeader("Accept", "*/*");
            }

            if (contentType != null && contentType.length > 0) {
                request.setHeader("Content-type", contentType[0]);
            } else {
                request.setHeader("Content-type", "*/*");
            }

            HttpResponse response = client.execute(request);

            ApiResponse[] apiResponses = nifiApiMethod.getAnnotation(ApiResponses.class).value();

            for (ApiResponse ar : apiResponses) {
                if (ar.code() == response.getStatusLine().getStatusCode()) {
                    System.out.println(ar.message());
                    //TODO: Probably throw a custom exception here ....
                    return null;
                }
            }

            //Examine the return type and handle that data appropriately.
            Header rCT = response.getHeaders("Content-Type")[0];
            if (rCT.getValue().equalsIgnoreCase("application/xml")) {
//                System.out.println("Processing response XML");
//                TemplateDTO templateDTO = TemplateDeserializer.deserialize(response.getEntity().getContent());
//                return templateDTO;
                return null;
            } else {
                return mapper.readValue(response.getEntity().getContent(), nifiApiMethod.getAnnotation(ApiOperation.class).response());
            }

        } catch (Exception ex) {
            logger.error("Unable to complete HTTP GET due to {}", ex.getMessage());
            return null;
        }
    }

    public <U extends Entity> Object post(Class<? extends ApplicationResource> resourceClass,
            Method nifiApiMethod,
            U u,
            Map<String, String> pathParams,
            InputStream payloadData) {

        StringBuilder stringBuilder = new StringBuilder(this.baseUrl);
        stringBuilder.append(resourceClass.getAnnotation(Path.class).value());
        stringBuilder.append("/");
        stringBuilder.append(nifiApiMethod.getAnnotation(Path.class).value());

        String fullRequest = replaceUriWithPathParams(stringBuilder.toString(), pathParams);

        HttpPost request = new HttpPost(fullRequest);

        StringBuffer result = new StringBuffer();
        try {

            //Set the Accept and Content-Type headers appropriately.
            String produces = nifiApiMethod.getAnnotation(Produces.class).value()[0];
            String consumes = nifiApiMethod.getAnnotation(Consumes.class).value()[0];

            //Set POST request payload. Can only upload either Inputstream OR Object currently.
            if (u != null || payloadData != null) {
                if (u != null) {
                    StringEntity input = new StringEntity(mapper.writeValueAsString(u));
                    request.setEntity(input);
                    request.setHeader("Content-type", consumes);
                } else {
                    InputStreamEntity inputStreamEntity = new InputStreamEntity(payloadData);
                    request.setEntity(inputStreamEntity);

                    MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                    //builder.addTextBody("field1", "yes", ContentType.TEXT_PLAIN);

                    // This attaches the file to the POST:
                    builder.addBinaryBody(
                            "template",
                            payloadData,
                            ContentType.APPLICATION_OCTET_STREAM,
                            "SomethingTest.xml"
                    );

                    HttpEntity multipart = builder.build();
                    request.setEntity(multipart);
                }
            }

            request.addHeader("Accept", produces);

            HttpResponse response = client.execute(request);

            //Examine the return type and handle that data appropriately.
            Header rCT = response.getHeaders("Content-Type")[0];
            if (rCT.getValue().equalsIgnoreCase("application/xml")) {
//                TemplateDTO templateDTO = TemplateDeserializer.deserialize(response.getEntity().getContent());
//                return templateDTO;
                return null;
            } else {
                return mapper.readValue(response.getEntity().getContent(), nifiApiMethod.getAnnotation(ApiOperation.class).response());
            }

        } catch (Exception ex) {
            logger.error("Unable to complete HTTP POST due to {}", ex.getMessage());
            return null;
        }
    }

    /**
     * Invokes the NiFi PUT REST API commands.
     *
     * @param resourceClass
     * @param nifiApiMethod
     * @param u
     * @param <U>
     * @return
     */
    public <U extends Entity> Object put(
            Class<? extends ApplicationResource> resourceClass,
            Method nifiApiMethod,
            U u) {

        StringBuilder stringBuilder = new StringBuilder(this.baseUrl);
        stringBuilder.append(resourceClass.getAnnotation(Path.class).value());
        stringBuilder.append("/");
        stringBuilder.append(nifiApiMethod.getAnnotation(Path.class).value());

        //String fullRequest = replaceUriWithPathParams(stringBuilder.toString(), pathParams);
        String fullRequest = stringBuilder.toString();

        HttpPut request = new HttpPut(fullRequest);

        try {

            StringEntity input = new StringEntity(mapper.writeValueAsString(u));
            request.setEntity(input);

            // add request header
            request.addHeader("Accept", "application/json");
            request.setHeader("Content-type", "application/json");
            HttpResponse response = client.execute(request);

            return mapper.readValue(response.getEntity().getContent(), nifiApiMethod.getAnnotation(ApiOperation.class).response());

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}