package org.apache.nifi.device.registry.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.nifi.controller.status.ConnectionStatus;
import org.apache.nifi.device.registry.dao.WorkflowConnectionDAO;

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


public class WorkflowConnectionDAOImpl
    implements WorkflowConnectionDAO {

    Map<Long, List<List<ConnectionStatus>>> db = new HashMap<Long, List<List<ConnectionStatus>>>();

    public List<ConnectionStatus> getLatestPressuredConnectionForDevice(long deviceId) {
        List<List<ConnectionStatus>> pressuredHistory = db.get(deviceId);
        if (pressuredHistory != null) {
            //Return the latest history.
            return pressuredHistory.get(pressuredHistory.size() - 1);
        } else {
            return null;
        }
    }

    public void insertPressuredConnectionForDevice(List<ConnectionStatus> pressuredConnections, long deviceId) {
        List<List<ConnectionStatus>> pressuredHistory = db.get(deviceId);
        if (pressuredHistory == null) {
            pressuredHistory = new ArrayList<List<ConnectionStatus>>();
        }
        pressuredHistory.add(pressuredConnections);
        db.put(deviceId, pressuredHistory);
    }
}
