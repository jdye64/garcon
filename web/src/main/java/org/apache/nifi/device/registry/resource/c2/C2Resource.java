package org.apache.nifi.device.registry.resource.c2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.nifi.device.registry.GarconConfiguration;
import org.apache.nifi.device.registry.resource.c2.core.C2Payload;
import org.apache.nifi.device.registry.resource.c2.core.C2Response;
import org.apache.nifi.device.registry.resource.c2.core.device.DeviceInfo;
import org.apache.nifi.device.registry.resource.c2.core.device.NetworkInfo;
import org.apache.nifi.device.registry.resource.c2.core.device.SystemInfo;
import org.apache.nifi.device.registry.resource.c2.core.metrics.C2Metrics;
import org.apache.nifi.device.registry.resource.c2.core.metrics.C2ProcessMetrics;
import org.apache.nifi.device.registry.resource.c2.core.metrics.C2QueueMetrics;
import org.apache.nifi.device.registry.resource.c2.core.metrics.pm.C2CPUMetrics;
import org.apache.nifi.device.registry.resource.c2.core.metrics.pm.C2MemoryMetrics;
import org.apache.nifi.device.registry.resource.c2.core.state.C2State;
import org.apache.nifi.device.registry.resource.c2.dao.C2DeviceDAO;
import org.apache.nifi.device.registry.resource.c2.dao.C2HeartbeatDAO;
import org.apache.nifi.device.registry.resource.c2.dao.C2OperationDAO;
import org.apache.nifi.device.registry.resource.c2.dao.C2QueueMetricsDAO;
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

    public C2Resource(GarconConfiguration conf, C2DeviceDAO c2DeviceDAO, C2QueueMetricsDAO c2QueueMetricsDAO, C2HeartbeatDAO c2HeartbeatDAO,
            C2OperationDAO c2OperationDAO) {
        this.configuration = conf;
        this.c2Service = new C2ServiceImpl(c2DeviceDAO, c2QueueMetricsDAO, c2HeartbeatDAO, c2OperationDAO);
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

        // Create the C2State object.
        C2State s = new C2State();
        s.setRunning("running");
        s.setUptimeMilliseconds(100000l);

        // Create the Metrics object.
        C2Metrics m = new C2Metrics();

        // -- nested create queue metrics.
        C2QueueMetrics qm = new C2QueueMetrics();
        qm.setQueueMax(0l);
        qm.setQueued(0l);
        qm.setDataSizeMax(0l);
        qm.setDataSize(0l);
        List<C2QueueMetrics> qml = new ArrayList<C2QueueMetrics>();
        qml.add(qm);

        // -- nested create process metrics.
        C2ProcessMetrics pm = new C2ProcessMetrics();

        C2CPUMetrics cpm = new C2CPUMetrics();
        cpm.setInvolcs(1000);
        List<C2CPUMetrics> cpuml = new ArrayList<C2CPUMetrics>();
        cpuml.add(cpm);

        C2MemoryMetrics cmm = new C2MemoryMetrics();
        cmm.setMaxrss(1000);
        List<C2MemoryMetrics> cmml = new ArrayList<C2MemoryMetrics>();
        cmml.add(cmm);

        //pm.setCpuMetrics(cpuml);
        //pm.setMemoryMetrics(cmml);
        List<C2ProcessMetrics> pml = new ArrayList<C2ProcessMetrics>();
        pml.add(pm);

        Map<String, C2QueueMetrics> qmm = new HashMap<String, C2QueueMetrics>();
        qmm.put("GetFile/success/LogAttribute", qm);

        m.setQueueMetrics(qmm);
        //m.setProcessMetricss(pml);

        // Create the DeviceInfo object
        DeviceInfo di = new DeviceInfo();

        // nested --- create device info
        NetworkInfo ni = new NetworkInfo();
        ni.setHostname("localhost");
        ni.setDeviceid("13412341234");
        ni.setIp("127.0.1.1");
        List<NetworkInfo> nil = new ArrayList<NetworkInfo>();
        nil.add(ni);

        // nested --- create system info
        SystemInfo si = new SystemInfo();
        si.setVcores(8);
        si.setPhysicalMemory(100231l);
        si.setMachineArchitecture("x86_64");
        List<SystemInfo> sil = new ArrayList<SystemInfo>();
        sil.add(si);

        di.setSystemInfo(si);
        di.setNetworkInfo(ni);

        p.setState(s);
        p.setOperation("heartbeat");
        p.setMetrics(m);
        p.setDeviceInfo(di);

        System.out.println(mapper.writeValueAsString(p));
    }
}
