package org.apache.nifi.device.registry.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.nifi.controller.status.ConnectionStatus;
import org.apache.nifi.device.registry.dao.ConnectionDAO;

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


public class ConnectionDAOImpl
    implements ConnectionDAO {

    private static ConnectionDAOImpl instance = null;
    protected ConnectionDAOImpl() {
        // Exists only to defeat instantiation.
    }
    public static ConnectionDAOImpl getInstance() {
        if(instance == null) {
            instance = new ConnectionDAOImpl();
        }
        return instance;
    }

    Map<String, List<List<ConnectionStatus>>> db = new HashMap<String, List<List<ConnectionStatus>>>();

    public List<ConnectionStatus> getLatestPressuredConnectionForDevice(String deviceId) {
        List<List<ConnectionStatus>> pressuredHistory = db.get(deviceId);
        if (pressuredHistory != null) {
            //Return the latest history.
            return pressuredHistory.get(pressuredHistory.size() - 1);
        } else {
            return null;
        }
    }

    public void insertPressuredConnectionForDevice(List<ConnectionStatus> pressuredConnections, String deviceId) {
        List<List<ConnectionStatus>> pressuredHistory = db.get(deviceId);
        if (pressuredHistory == null) {
            pressuredHistory = new ArrayList<List<ConnectionStatus>>();
        } else {
            //Get the current connections for this device and reset them
        }
        pressuredHistory.add(pressuredConnections);
        db.put(deviceId, pressuredHistory);
    }

    public ConnectionStatus getPressuredConnectionDetails(String deviceId, String connectionId) {
        List<List<ConnectionStatus>> pressuredHistory = db.get(deviceId);
        if (pressuredHistory != null) {
            //Find the pressured connection and returned the ConnectionStatus
            for (List<ConnectionStatus> csl : pressuredHistory) {
                for (ConnectionStatus cs : csl) {
                    if (cs.getId().equalsIgnoreCase(connectionId)) {
                        return cs;
                    }
                }
            }
        } else {
            return null;
        }
        return null;
    }

    public long getPressuredConnectionsCountForDevice(String deviceId) {

        long pressuredConnectionCount = 0l;

        if (!StringUtils.isEmpty(deviceId)) {
            List<List<ConnectionStatus>> deviceConnections = db.get(deviceId);  //Basically at the processor level. could have multiples of course
            for (List<ConnectionStatus> con : deviceConnections) {
                pressuredConnectionCount =+ con.size();
            }

        } else {
            Iterator<String> itr = db.keySet().iterator();
            while (itr.hasNext()) {
                String devId = itr.next();
                List<List<ConnectionStatus>> deviceConnections =  db.get(devId);
                for (List<ConnectionStatus> con : deviceConnections) {
                    pressuredConnectionCount =+ con.size();
                }
            }
        }

        return pressuredConnectionCount;
    }

    public long getTotalConnectionsCountForDevice(String deviceId) {

        long totalConnections = 0l;

        if (!StringUtils.isEmpty(deviceId)) {
            List<List<ConnectionStatus>> deviceConnections = db.get(deviceId);  //Basically at the processor level. could have multiples of course
            for (List<ConnectionStatus> con : deviceConnections) {
                totalConnections =+ con.size();
            }

        } else {
            Iterator<String> itr = db.keySet().iterator();
            while (itr.hasNext()) {
                String devId = itr.next();
                List<List<ConnectionStatus>> deviceConnections =  db.get(devId);
                for (List<ConnectionStatus> con : deviceConnections) {
                    totalConnections =+ con.size();
                }
            }
        }

        return totalConnections;

    }

    public long getPressuredConnectionBytesForDevice(String deviceId) {
        long pressuredConnectionBytes = 0l;

        if (!StringUtils.isEmpty(deviceId)) {
            List<List<ConnectionStatus>> deviceConnections = db.get(deviceId);  //Basically at the processor level. could have multiples of course
            for (List<ConnectionStatus> con : deviceConnections) {
                for (ConnectionStatus cs : con) {
                    pressuredConnectionBytes = pressuredConnectionBytes + cs.getQueuedBytes();
                }
            }

        } else {
            Iterator<String> itr = db.keySet().iterator();
            while (itr.hasNext()) {
                String devId = itr.next();
                List<List<ConnectionStatus>> deviceConnections =  db.get(devId);
                for (List<ConnectionStatus> con : deviceConnections) {
                    for (ConnectionStatus cs : con) {
                        pressuredConnectionBytes = pressuredConnectionBytes + cs.getQueuedBytes();
                    }
                }
            }
        }

        return pressuredConnectionBytes;
    }

    public long getPressuedConenctionsObjectCountForDevice(String deviceId) {
        long pressuredConnectionObjects = 0l;

        if (!StringUtils.isEmpty(deviceId)) {
            List<List<ConnectionStatus>> deviceConnections = db.get(deviceId);  //Basically at the processor level. could have multiples of course
            for (List<ConnectionStatus> con : deviceConnections) {
                for (ConnectionStatus cs : con) {
                    pressuredConnectionObjects = pressuredConnectionObjects +  cs.getQueuedCount();
                }
            }

        } else {
            Iterator<String> itr = db.keySet().iterator();
            while (itr.hasNext()) {
                String devId = itr.next();
                List<List<ConnectionStatus>> deviceConnections =  db.get(devId);
                for (List<ConnectionStatus> con : deviceConnections) {
                    for (ConnectionStatus cs : con) {
                        pressuredConnectionObjects = pressuredConnectionObjects +  cs.getQueuedCount();
                    }
                }
            }
        }

        return pressuredConnectionObjects;
    }
}
