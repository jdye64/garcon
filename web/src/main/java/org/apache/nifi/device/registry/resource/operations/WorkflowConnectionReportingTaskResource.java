package org.apache.nifi.device.registry.resource.operations;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.nifi.controller.status.ConnectionStatus;
import org.apache.nifi.device.registry.NiFiDeviceRegistryConfiguration;
import org.apache.nifi.device.registry.dao.WorkflowConnectionDAO;
import org.apache.nifi.device.registry.dao.impl.WorkflowConnectionDAOImpl;
import org.apache.nifi.device.registry.service.WorkflowConnectionService;
import org.apache.nifi.device.registry.service.impl.WorkflowConnectionServiceImpl;
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
 * Created on 3/30/17.
 */

@Path("/connection")
@Produces(MediaType.APPLICATION_JSON)
public class WorkflowConnectionReportingTaskResource {

    private static final Logger logger = LoggerFactory.getLogger(WorkflowConnectionReportingTaskResource.class);

    private NiFiDeviceRegistryConfiguration configuration;
    private WorkflowConnectionService conService = null;
    private WorkflowConnectionDAO conDao = null;

    public WorkflowConnectionReportingTaskResource(NiFiDeviceRegistryConfiguration conf) {
        this.configuration = conf;
        this.conService = new WorkflowConnectionServiceImpl();
        this.conDao = new WorkflowConnectionDAOImpl();
    }

    @GET
    @Timed
    public Response getRegisteredDevices() {
        logger.info("Retrieving pressured connections");
        return Response.ok(conDao.getLatestPressuredConnectionForDevice(1l)).build();
    }

    @GET
    @Timed
    @Path("/{id}")
    public Response getDetailedConnectionStatusForConnectionById(@PathParam("id") String connectionId) {
        logger.info("Retrieving detailed information for backpressured connection with ID: {}", new Object[]{connectionId});
        return Response.ok(conDao.getPressuredConnectionDetails(1l, connectionId)).build();
    }

    @POST
    @Timed
    @Path("/pressured")
    public Response addPressuredConnections(List<ConnectionStatus> pressuredConnections) {
        logger.info("Pressured Connects: " + pressuredConnections);
        conDao.insertPressuredConnectionForDevice(pressuredConnections, 1l);
        return Response.ok().build();
    }
}
