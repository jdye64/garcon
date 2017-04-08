package org.apache.nifi.device.registry.resource;

import java.io.IOException;
import java.util.List;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.TextMessage;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.nifi.device.registry.dao.WebsocketClientSessionDAO;
import org.apache.nifi.device.registry.dao.impl.WebsocketClientSessionDAOImpl;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Metered;
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
 * Created on 3/13/17.
 */

@Metered
@Timed
@ExceptionMetered
@ServerEndpoint("/api/v1/registry/ws")
public class NiFiDeviceWebSocketNotifier {

    private final WebsocketClientSessionDAO websocketSessions = new WebsocketClientSessionDAOImpl();

    public NiFiDeviceWebSocketNotifier() {
        Thread t = new Thread(new ActiveMQConsumer());
        t.start();
    }

    @OnOpen
    public void myOnOpen(final Session session) throws IOException {
        //session.getAsyncRemote().sendText("welcome");
        websocketSessions.addWebsocketClientSession(session);
    }

    @OnMessage
    public void myOnMsg(final Session session, String message) {
        //We actually don't want to do anything here. We only send messages when ActiveMQ JMS messages are received.
        //session.getAsyncRemote().sendText(message.toUpperCase());
    }

    @OnClose
    public void myOnClose(final Session session, CloseReason cr) {
        websocketSessions.removeWebsocketClientSession(session);
    }


    public class ActiveMQConsumer
            implements Runnable {

        private javax.jms.Session jmsSession = null;
        private Destination destination = null;
        private Connection connection = null;
        private MessageConsumer consumer = null;

        public ActiveMQConsumer() {
            try {

                ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
                destination = new ActiveMQQueue("DeviceRegistryEvents");

                connection = connectionFactory.createConnection();
                this.jmsSession = connection.createSession(false,
                        javax.jms.Session.AUTO_ACKNOWLEDGE);

                consumer = this.jmsSession.createConsumer(destination);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public void run() {
            try {
                connection.start();
                TextMessage msg = (TextMessage) consumer.receive();

                List<Session> sessionList = websocketSessions.getActiveSessions();
                for (Session session : sessionList) {
                    try {
                        session.getBasicRemote().sendText(msg.getText());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}
