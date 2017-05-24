package org.apache.nifi.device.registry.service.impl;

import org.apache.nifi.device.registry.dao.ConnectionDAO;
import org.apache.nifi.device.registry.dao.impl.ConnectionDAOImpl;
import org.apache.nifi.device.registry.dto.ConnectionsHUD;
import org.apache.nifi.device.registry.service.ConnectionService;

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


public class ConnectionServiceImpl
    implements ConnectionService {

    private ConnectionDAO connectionDAO = null;

    private static ConnectionServiceImpl instance = null;
    protected ConnectionServiceImpl() {
        // Exists only to defeat instantiation.
        connectionDAO = ConnectionDAOImpl.getInstance();
    }
    public static ConnectionServiceImpl getInstance() {
        if(instance == null) {
            instance = new ConnectionServiceImpl();
        }
        return instance;
    }

    public ConnectionsHUD generateConnectionsHUD(String deviceId) {
        ConnectionsHUD hud = new ConnectionsHUD();
        hud.setBackPressuredConnections(connectionDAO.getPressuredConnectionsCountForDevice(deviceId));
        hud.setTotalConnections(connectionDAO.getTotalConnectionsCountForDevice(deviceId));
        hud.setBackPressuredBytes(connectionDAO.getPressuredConnectionBytesForDevice(deviceId));
        hud.setBackPressuredObjects(connectionDAO.getPressuedConenctionsObjectCountForDevice(deviceId));
        return hud;
    }
}
