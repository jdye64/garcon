package org.apache.nifi.device.registry.resource.hud;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.nifi.device.registry.NiFiDeviceRegistryConfiguration;
import org.apache.nifi.device.registry.dao.DeviceDAO;
import org.apache.nifi.device.registry.dao.ProcessorsDAO;
import org.apache.nifi.device.registry.dao.impl.DeviceDAOImpl;
import org.apache.nifi.device.registry.dao.impl.ProcessorsDAOImpl;
import org.apache.nifi.device.registry.dto.HUD;
import org.apache.nifi.device.registry.service.ConnectionService;
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
 * Created on 4/6/17.
 */

@Path("/api/v1/hud")
@Produces(MediaType.APPLICATION_JSON)
public class HUDResource {

    private static final Logger logger = LoggerFactory.getLogger(HUDResource.class);

    private NiFiDeviceRegistryConfiguration configuration;
    private ProcessorsDAO processorsDAO = null;
    private DeviceDAO deviceDAO = null;
    private ConnectionService connectionService = null;

    public HUDResource(NiFiDeviceRegistryConfiguration conf) {
        this.configuration = conf;
        this.processorsDAO = ProcessorsDAOImpl.getInstance();
        this.deviceDAO = DeviceDAOImpl.getInstance();
    }

    @GET
    @Timed
    @Path("/metrics/lite")
    public Response getLiteHeadsUpDisplayMetrics() {
        logger.info("Retrieving lite weight heads up display metrics for UI");
        HUD hud = new HUD();
        hud.setTotalNumDevices(deviceDAO.getTotalNumDevice());
        hud.setTotalNumProcessors(processorsDAO.getTotalNumberOfProcessors(null));
        return Response.ok(hud).build();
    }
}
