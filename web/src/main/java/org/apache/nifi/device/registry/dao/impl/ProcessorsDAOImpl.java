package org.apache.nifi.device.registry.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.nifi.controller.status.ProcessorStatus;
import org.apache.nifi.device.registry.dao.ProcessorsDAO;

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
 * Created on 4/5/17.
 */


public class ProcessorsDAOImpl
    implements ProcessorsDAO {

    private static ProcessorsDAOImpl instance = null;
    protected ProcessorsDAOImpl() {
        // Exists only to defeat instantiation.
    }
    public static ProcessorsDAOImpl getInstance() {
        if(instance == null) {
            instance = new ProcessorsDAOImpl();
        }
        return instance;
    }

    Map<Long, List<List<ProcessorStatus>>> db_stopped = new HashMap<Long, List<List<ProcessorStatus>>>();
    Map<Long, List<List<ProcessorStatus>>> db_invalid = new HashMap<Long, List<List<ProcessorStatus>>>();
    Map<Long, List<List<ProcessorStatus>>> db_running = new HashMap<Long, List<List<ProcessorStatus>>>();
    Map<Long, List<List<ProcessorStatus>>> db_disabled = new HashMap<Long, List<List<ProcessorStatus>>>();
    Map<Long, List<List<ProcessorStatus>>> db_statuses = new HashMap<Long, List<List<ProcessorStatus>>>();

    public List<ProcessorStatus> getStoppedProcessorsForDevice(long deviceId) {
        List<List<ProcessorStatus>> stoppedHistory = db_stopped.get(deviceId);
        if (stoppedHistory != null) {
            //Return the latest history.
            return stoppedHistory.get(stoppedHistory.size() - 1);
        } else {
            return null;
        }
    }

    public void insertStoppedProcessorsForDevice(long deviceId, List<ProcessorStatus> stoppedProcessors) {
        List<List<ProcessorStatus>> stoppedHistory = db_stopped.get(deviceId);
        if (stoppedHistory == null) {
            stoppedHistory = new ArrayList<List<ProcessorStatus>>();
        }
        stoppedHistory.add(stoppedProcessors);
        db_stopped.put(deviceId, stoppedHistory);
    }

    public List<ProcessorStatus> getInvalidProcessorsForDevice(long deviceId) {
        List<List<ProcessorStatus>> stoppedHistory = db_invalid.get(deviceId);
        if (stoppedHistory != null) {
            //Return the latest history.
            return stoppedHistory.get(stoppedHistory.size() - 1);
        } else {
            return null;
        }
    }

    public void insertInvalidProcessorsForDevice(long deviceId, List<ProcessorStatus> invalidProcessors) {
        List<List<ProcessorStatus>> stoppedHistory = db_invalid.get(deviceId);
        if (stoppedHistory == null) {
            stoppedHistory = new ArrayList<List<ProcessorStatus>>();
        }
        stoppedHistory.add(invalidProcessors);
        db_invalid.put(deviceId, stoppedHistory);
    }

    public List<ProcessorStatus> getRunningProcessorsForDevice(long deviceId) {
        List<List<ProcessorStatus>> stoppedHistory = db_running.get(deviceId);
        if (stoppedHistory != null) {
            //Return the latest history.
            return stoppedHistory.get(stoppedHistory.size() - 1);
        } else {
            return null;
        }
    }

    public void insertRunningProcessorsForDevice(long deviceId, List<ProcessorStatus> invalidProcessors) {
        List<List<ProcessorStatus>> stoppedHistory = db_running.get(deviceId);
        if (stoppedHistory == null) {
            stoppedHistory = new ArrayList<List<ProcessorStatus>>();
        }
        stoppedHistory.add(invalidProcessors);
        db_running.put(deviceId, stoppedHistory);
    }

    public List<ProcessorStatus> getDisabledProcessorsForDevice(long deviceId) {
        List<List<ProcessorStatus>> stoppedHistory = db_disabled.get(deviceId);
        if (stoppedHistory != null) {
            //Return the latest history.
            return stoppedHistory.get(stoppedHistory.size() - 1);
        } else {
            return null;
        }
    }

    public void insertDisabledProcessorsForDevice(long deviceId, List<ProcessorStatus> invalidProcessors) {
        List<List<ProcessorStatus>> stoppedHistory = db_disabled.get(deviceId);
        if (stoppedHistory == null) {
            stoppedHistory = new ArrayList<List<ProcessorStatus>>();
        }
        stoppedHistory.add(invalidProcessors);
        db_disabled.put(deviceId, stoppedHistory);
    }

    public List<ProcessorStatus> getProcessorStatusesForDevice(long deviceId) {
        List<List<ProcessorStatus>> stoppedHistory = db_statuses.get(deviceId);
        if (stoppedHistory != null) {
            //Return the latest history.
            return stoppedHistory.get(stoppedHistory.size() - 1);
        } else {
            return null;
        }
    }

    public void insertDProcessorStatusesForDevice(long deviceId, List<ProcessorStatus> invalidProcessors) {
        List<List<ProcessorStatus>> stoppedHistory = db_statuses.get(deviceId);
        if (stoppedHistory == null) {
            stoppedHistory = new ArrayList<List<ProcessorStatus>>();
        }
        stoppedHistory.add(invalidProcessors);
        db_statuses.put(deviceId, stoppedHistory);
    }

    public int getTotalNumberOfProcessors(Long deviceId) {
        return countDBSizeForDevice(db_statuses, deviceId);
    }

    public int getNumberOfRunningProcessors(Long deviceId) {
        return countDBSizeForDevice(db_running, deviceId);
    }

    public int getNumberOfDisabledProcessors(Long deviceId) {
        return countDBSizeForDevice(db_disabled, deviceId);
    }

    public int getNumberOfStoppedProcessors(Long deviceId) {
        return countDBSizeForDevice(db_stopped, deviceId);
    }

    public int getNumberOfInvalidProcessors(Long deviceId) {
        return countDBSizeForDevice(db_invalid, deviceId);
    }


    private int countDBSizeForDevice(Map<Long, List<List<ProcessorStatus>>> db, Long deviceId) {
        int count = 0;
        if (deviceId != null) {
            List<List<ProcessorStatus>> facts = db.get(deviceId);
            for (List<ProcessorStatus> ps : facts) {
                count += ps.size();
            }
        } else {
            Iterator<Long> itr = db.keySet().iterator();
            while (itr.hasNext()) {
                Long key = itr.next();
                List<List<ProcessorStatus>> facts = db.get(key);
                for (List<ProcessorStatus> ps : facts) {
                    count += ps.size();
                }
            }
        }
        return count;
    }
}
