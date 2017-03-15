package org.apache.nifi.device.registry.resource;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.nifi.device.registry.NiFiDeviceRegistryConfiguration;
import org.apache.nifi.device.registry.api.NiFiDevice;
import org.apache.nifi.device.registry.service.DeviceService;
import org.apache.nifi.device.registry.service.impl.DeviceServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.Timed;
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
 * Created on 3/10/17.
 */

@Path("/device")
@Produces(MediaType.APPLICATION_JSON)
public class DeviceResource {

    private static final Logger logger = LoggerFactory.getLogger(DeviceResource.class);

    private NiFiDeviceRegistryConfiguration configuration;
    private DeviceService deviceService = null;

    private javax.jms.Session jmsSession = null;
    private Destination destination = null;
    private MessageProducer producer = null;
    private ObjectMapper mapper = new ObjectMapper();

    public DeviceResource(NiFiDeviceRegistryConfiguration conf) {
        this.configuration = conf;
        this.deviceService = new DeviceServiceImpl();
    }

    @POST
    @Timed
    public Response announceAvailability(NiFiDevice device) {
        logger.info("Message: " + device.toString());

        if (this.producer == null) {
            try {
                ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

                Connection connection = connectionFactory.createConnection();
                this.jmsSession = connection.createSession(false,
                        javax.jms.Session.AUTO_ACKNOWLEDGE);

                destination = new ActiveMQQueue("DeviceRegistryEvents");
                this.producer = jmsSession.createProducer(destination);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        try {
            Message msg = jmsSession.createTextMessage(mapper.writeValueAsString(device));
            producer.send(msg);
        } catch (Exception ex) {
            ex.printStackTrace();
        }


//        if (this.session == null) {
//            URI uri = URI.create("ws://localhost:8888/registry/ws");
//
//            WebSocketClient client = new WebSocketClient();
//            try
//            {
//                client.start();
//                // The socket that receives events
//                EventSocket socket = new EventSocket();
//                // Attempt Connect
//                Future<Session> fut = client.connect(socket,uri);
//                // Wait for Connect
//                this.session = fut.get();
//            }
//            catch (Throwable t)
//            {
//                t.printStackTrace(System.err);
//            }
//        }

//        // Send a message
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//           this.session.getRemote().sendString(mapper.writeValueAsString(device));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return Response.ok().build();
    }
}
