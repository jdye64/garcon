package org.apache.nifi.device.registry.resource.c2;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.nifi.device.registry.GarconConfiguration;
import org.apache.nifi.device.registry.dao.device.DeviceDAO;
import org.apache.nifi.device.registry.resource.c2.core.C2Payload;
import org.apache.nifi.device.registry.resource.c2.core.C2Response;
import org.apache.nifi.device.registry.resource.c2.core.device.DeviceInfo;
import org.apache.nifi.device.registry.resource.c2.core.device.NetworkInfo;
import org.apache.nifi.device.registry.resource.c2.core.device.SystemInfo;
import org.apache.nifi.device.registry.resource.c2.core.metrics.C2Metrics;
import org.apache.nifi.device.registry.resource.c2.core.metrics.C2QueueMetrics;
import org.apache.nifi.device.registry.resource.c2.core.state.C2State;
import org.apache.nifi.device.registry.resource.c2.dao.C2PayloadDAO;
import org.apache.nifi.device.registry.resource.c2.service.C2Service;
import org.apache.nifi.device.registry.resource.c2.service.impl.C2ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.core.JsonProcessingException;
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
 * Created on 7/7/17.
 */

@Path("/api/v1/c2")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class C2Resource {

    private static final Logger logger = LoggerFactory.getLogger(C2Resource.class);

    private GarconConfiguration configuration;
    private static ObjectMapper mapper = new ObjectMapper();
    private C2Service c2Service;

    public C2Resource(GarconConfiguration conf, DeviceDAO deviceDAO, C2PayloadDAO c2PayloadDAO) {
        this.configuration = conf;
        this.c2Service = new C2ServiceImpl(c2PayloadDAO, deviceDAO);
    }

    @POST
    @Timed
    public Response minifiHeartbeat(C2Payload payload) {
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("MiNiFi CPP Heartbeat received: " + mapper.writeValueAsString(payload));
            }
            C2Response response = c2Service.registerHeartBeat(payload);
            if (logger.isDebugEnabled()) {
                logger.debug("C2Response: " + mapper.writeValueAsString(response));
            }
            return Response.ok(response).build();
        } catch (Exception ex) {
            ex.printStackTrace();
            return Response.serverError().build();
        }
    }


    public static void main(String[] args) throws JsonProcessingException {
        C2Payload p = new C2Payload();

        // Create DeviceInfo
        DeviceInfo di = new DeviceInfo();

        // create NetworkInfo
        NetworkInfo ni = new NetworkInfo();
        ni.setHostname("littledev");
        ni.setDeviceid("6842576138429604238");
        ni.setIp("127.0.1.1.");

        // Create SystemInformation
        SystemInfo si = new SystemInfo();
        si.setVcores(4);
        si.setPhysicalMemory(0l);

        List<NetworkInfo> nil = new ArrayList<NetworkInfo>();
        nil.add(ni);
        List<SystemInfo> sil = new ArrayList<SystemInfo>();
        sil.add(si);

        di.setNetworkInfo(nil);
        di.setSystemInfo(sil);

        p.setDeviceInfo(di);

        // Create C2Metrics
        C2Metrics m = new C2Metrics();
        C2QueueMetrics qm = new C2QueueMetrics();
        qm.setQueueMax(0l);
        qm.setQueued(0l);
        qm.setDataSizeMax(0l);
        qm.setDataSize(0l);
        qm.setName("TransferFilesToRPG");
        List<C2QueueMetrics> ml = new ArrayList<C2QueueMetrics>();
        ml.add(qm);
        m.setQueueMetrics(ml);
        p.setMetrics(m);

        p.setOperation("heartbeat");

        C2State state = new C2State();
        state.setRunning("running");
        state.setUptimeMilliseconds("12351234");
        p.setState(state);

        System.out.println("Payload: " + mapper.writeValueAsString(p));

    }

}
