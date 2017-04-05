package org.apache.nifi.device.registry.resource.operations;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.nifi.controller.status.ProcessorStatus;
import org.apache.nifi.device.registry.NiFiDeviceRegistryConfiguration;
import org.apache.nifi.device.registry.dao.ProcessorsDAO;
import org.apache.nifi.device.registry.dao.impl.ProcessorsDAOImpl;
import org.apache.nifi.device.registry.service.ProcessorsService;
import org.apache.nifi.device.registry.service.impl.ProcessorsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.Timed;

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

@Path("/processors")
@Produces(MediaType.APPLICATION_JSON)
public class ProcessorsResource {

    private static final Logger logger = LoggerFactory.getLogger(ProcessorsResource.class);

    private NiFiDeviceRegistryConfiguration configuration;
    private ProcessorsService processorsService = null;
    private ProcessorsDAO processorsDAO = null;

    public ProcessorsResource(NiFiDeviceRegistryConfiguration conf) {
        this.configuration = conf;
        this.processorsService = new ProcessorsServiceImpl();
        this.processorsDAO = new ProcessorsDAOImpl();
    }

    @GET
    @Timed
    @Path("/status")
    public Response getProcessorsStatus() {
        logger.info("Retrieving processors status");
        return Response.ok(processorsDAO.getProcessorStatusesForDevice(1l)).build();
    }

    @POST
    @Timed
    @Path("/status")
    public Response addProcessorsStatus(List<ProcessorStatus> stoppedProcessors) {
        logger.info("Processors status: " + stoppedProcessors);
        processorsDAO.insertDProcessorStatusesForDevice(1l,stoppedProcessors);
        return Response.ok().build();
    }

    @GET
    @Timed
    @Path("/invalid")
    public Response getInvalidProcessors() {
        logger.info("Retrieving invalid processors");
        return Response.ok(processorsDAO.getInvalidProcessorsForDevice(1l)).build();
    }

    @POST
    @Timed
    @Path("/invalid")
    public Response addInvalidProcessors(List<ProcessorStatus> invalidProcessors) {
        logger.info("Invalid Processors: " + invalidProcessors);
        processorsDAO.insertInvalidProcessorsForDevice(1l, invalidProcessors);
        return Response.ok().build();
    }

    @GET
    @Timed
    @Path("/stopped")
    public Response getStoppedProcessors() {
        logger.info("Retrieving stopped processors");
        return Response.ok(processorsDAO.getStoppedProcessorsForDevice(1l)).build();
    }

    @POST
    @Timed
    @Path("/stopped")
    public Response addStoppedProcessors(List<ProcessorStatus> stoppedProcessors) {
        logger.info("Stopped Processors: " + stoppedProcessors);
        processorsDAO.insertStoppedProcessorsForDevice(1l,stoppedProcessors);
        return Response.ok().build();
    }

    @GET
    @Timed
    @Path("/running")
    public Response getRunningProcessors() {
        logger.info("Retrieving running processors");
        return Response.ok(processorsDAO.getRunningProcessorsForDevice(1l)).build();
    }

    @POST
    @Timed
    @Path("/running")
    public Response addRunningProcessors(List<ProcessorStatus> stoppedProcessors) {
        logger.info("Running Processors: " + stoppedProcessors);
        processorsDAO.insertRunningProcessorsForDevice(1l,stoppedProcessors);
        return Response.ok().build();
    }

    @GET
    @Timed
    @Path("/disabled")
    public Response getDisabledProcessors() {
        logger.info("Retrieving disabled processors");
        return Response.ok(processorsDAO.getDisabledProcessorsForDevice(1l)).build();
    }

    @POST
    @Timed
    @Path("/disabled")
    public Response addDisabledProcessors(List<ProcessorStatus> stoppedProcessors) {
        logger.info("Disabled Processors: " + stoppedProcessors);
        processorsDAO.insertDisabledProcessorsForDevice(1l,stoppedProcessors);
        return Response.ok().build();
    }
}
