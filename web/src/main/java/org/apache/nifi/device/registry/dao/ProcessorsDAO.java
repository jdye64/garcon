package org.apache.nifi.device.registry.dao;

import java.util.List;

import org.apache.nifi.controller.status.ProcessorStatus;

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


public interface ProcessorsDAO {

    List<ProcessorStatus> getStoppedProcessorsForDevice(long deviceId);

    void insertStoppedProcessorsForDevice(long deviceId, List<ProcessorStatus> stoppedProcessors);

    List<ProcessorStatus> getInvalidProcessorsForDevice(long deviceId);

    void insertInvalidProcessorsForDevice(long deviceId, List<ProcessorStatus> invalidProcessors);

    List<ProcessorStatus> getRunningProcessorsForDevice(long deviceId);

    void insertRunningProcessorsForDevice(long deviceId, List<ProcessorStatus> invalidProcessors);

    List<ProcessorStatus> getDisabledProcessorsForDevice(long deviceId);

    void insertDisabledProcessorsForDevice(long deviceId, List<ProcessorStatus> invalidProcessors);

    List<ProcessorStatus> getProcessorStatusesForDevice(long deviceId);

    void insertDProcessorStatusesForDevice(long deviceId, List<ProcessorStatus> invalidProcessors);

    int getTotalNumberOfProcessors(Long deviceId);

    int getNumberOfRunningProcessors(Long deviceId);

    int getNumberOfDisabledProcessors(Long deviceId);

    int getNumberOfStoppedProcessors(Long deviceId);

    int getNumberOfInvalidProcessors(Long deviceId);
}
