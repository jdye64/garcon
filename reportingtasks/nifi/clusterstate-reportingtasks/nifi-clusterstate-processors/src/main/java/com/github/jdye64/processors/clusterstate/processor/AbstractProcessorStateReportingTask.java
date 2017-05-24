package com.github.jdye64.processors.clusterstate.processor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.nifi.controller.status.ProcessGroupStatus;
import org.apache.nifi.controller.status.ProcessorStatus;
import org.apache.nifi.controller.status.RunStatus;

import com.github.jdye64.reportingtasks.AbstractDeviceRegistryReportingTask;

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
 * Created on 4/6/17.
 */


public abstract class AbstractProcessorStateReportingTask
    extends AbstractDeviceRegistryReportingTask {

    protected List<ProcessorStatus> recursiveProcessorLocate(ProcessGroupStatus status, RunStatus runStatus) {

        List<ProcessorStatus> processors = new ArrayList<>();

        Iterator<ProcessorStatus> itr = status.getProcessorStatus().iterator();
        while (itr.hasNext()) {
            ProcessorStatus ps = itr.next();
            if (runStatus == null) {
                //Add all
                processors.add(ps);
            } else {
                if (ps.getRunStatus().compareTo(runStatus) == 0) {
                    processors.add(ps);
                }
            }
        }

        //Loop through all of the process groups and add their connection status
        Iterator<ProcessGroupStatus> pgitr = status.getProcessGroupStatus().iterator();
        while (pgitr.hasNext()) {
            ProcessGroupStatus pgs = pgitr.next();

            //recursive call
            processors.addAll(recursiveProcessorLocate(pgs, runStatus));
        }

        return processors;
    }
}
