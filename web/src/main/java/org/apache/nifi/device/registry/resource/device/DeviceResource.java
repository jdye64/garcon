package org.apache.nifi.device.registry.resource.device;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.nifi.device.registry.GarconConfiguration;
import org.apache.nifi.device.registry.api.device.MiNiFiCPPDevice;
import org.apache.nifi.device.registry.api.device.NiFiDevice;
import org.apache.nifi.device.registry.service.device.DeviceService;
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
 * Created on 3/10/17.
 */

@Path("/api/v1/device")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DeviceResource {

    private static final Logger logger = LoggerFactory.getLogger(DeviceResource.class);

    private GarconConfiguration configuration;
    private DeviceService deviceService = null;

    public DeviceResource(GarconConfiguration conf, DeviceService deviceService) {
        this.configuration = conf;
        this.deviceService = deviceService;
    }

    // ---------- BEGIN GET OPERATIONS FOR RETRIEVING DEVICE OBJECTS ------------------

    @GET
    @Timed
    public Response getDevices() {
        if (logger.isDebugEnabled()) {
            logger.debug("Retrieving devices registered to Garcon");
        }
        return Response.ok(this.deviceService.getDevices()).build();
    }

    @GET
    @Timed
    @Path("/{deviceId}")
    public Response getDeviceById(@PathParam("deviceId") String deviceId) {
        if (logger.isDebugEnabled()) {
            logger.debug("Attempting to retrieve device with id {}", deviceId);
        }
        return Response.ok(this.deviceService.getDeviceById(deviceId)).build();
    }

    // ---------- END GET OPERATIONS FOR RETRIEVING DEVICE OBJECTS ------------------

    @POST
    @Timed
    @Path("/minifi")
    public Response testingMiNiFiCPP(MiNiFiCPPDevice miNiFiDevice) {
        if (logger.isDebugEnabled()) {
            logger.debug("Apache NiFi MiNiFi C++ device heartbeat received for hostname {}", miNiFiDevice.getHostname());
        }
        return Response.ok("OK").build();
    }

    @POST
    @Timed
    public Response announceAvailability(NiFiDevice device) {
        //deviceDAO.insert(device);
        return Response.ok().build();
    }
}
