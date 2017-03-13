/**
 *
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

package org.apache.nifi.device.registry;

import java.io.IOException;

import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;

import org.apache.nifi.device.registry.cli.DummyCommand;
import org.apache.nifi.device.registry.managed.Site2SiteManagedProxy;
import org.apache.nifi.device.registry.resource.DeviceResource;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.websockets.WebsocketBundle;

public class NiFiDeviceRegistry
        extends Application<NiFiDeviceRegistryConfiguration> {

    @Override
    public void initialize(Bootstrap<NiFiDeviceRegistryConfiguration> bootstrap) {
        bootstrap.addCommand(new DummyCommand());

        //Creates an Asset bundle to serve up static content. Served from http://localhost:8080/assets/
        bootstrap.addBundle(new AssetsBundle());

        websocketBundle = new WebsocketBundle(WebSocket.class);
        bootstrap.addBundle(websocketBundle);
    }

    private WebsocketBundle websocketBundle;

    @Override
    public void run(NiFiDeviceRegistryConfiguration configuration, Environment environment) throws Exception {

//        final DBIFactory factory = new DBIFactory();
//        final DBI jdbi = factory.build(environment, configuration.getDataSourceFactory(), "mysql");
//        final DummyDAO dao = jdbi.onDemand(DummyDAO.class);

        //Add managed instances.
        environment.lifecycle().manage(new Site2SiteManagedProxy());

        //Register your Web Resources like below.
        final DeviceResource dummyResource = new DeviceResource(configuration);
        environment.jersey().register(dummyResource);

        // Using ServerEndpointConfig lets you inject objects to the websocket endpoint:
        final ServerEndpointConfig config = ServerEndpointConfig.Builder.create(EchoServer.class, "/extends-ws").build();
        // config.getUserProperties().put(Environment.class.getName(), environment);
        // Then you can get it from the Session object
        // - obj = session.getUserProperties().get("objectName");

        //websocketBundle.addEndpoint(config);
    }

    public static void main(String[] args) throws Exception {
        new NiFiDeviceRegistry().run(args);
    }

    @Metered
    @Timed
    @ExceptionMetered
    @ServerEndpoint("/registry/ws")
    public static class WebSocket {

        @OnOpen
        public void myOnOpen(final Session session) throws IOException {
            session.getAsyncRemote().sendText("welcome");
        }

        @OnMessage
        public void myOnMsg(final Session session, String message) {
            session.getAsyncRemote().sendText(message.toUpperCase());
        }

        @OnClose
        public void myOnClose(final Session session, CloseReason cr) {
        }
    }

    @Metered
    @Timed
    public static class EchoServer
            extends Endpoint implements MessageHandler.Whole<String> {
        private Session session;

        @Override
        public void onOpen(Session session, EndpointConfig config) {
            session.addMessageHandler(this);
            session.getAsyncRemote().sendText("welcome");
            this.session = session;
        }

        @Override
        public void onMessage(String message) {
            session.getAsyncRemote().sendText(message.toUpperCase());
        }
    }
}
