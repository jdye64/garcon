/*
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

package org.apache.nifi.api.client.service;

import java.io.InputStream;

import org.apache.nifi.web.api.dto.TemplateDTO;
import org.apache.nifi.web.api.entity.ConnectionEntity;
import org.apache.nifi.web.api.entity.ConnectionsEntity;
import org.apache.nifi.web.api.entity.FlowEntity;
import org.apache.nifi.web.api.entity.ProcessGroupEntity;
import org.apache.nifi.web.api.entity.TemplateEntity;

/**
 * Created by jdyer on 4/8/16.
 */
public interface ProcessGroups {

    /**
     * Gets a process group
     *
     * @param clientId
     *  If the client id is not specified, new one will be generated. This value (whether specified or generated) is included in the response
     *
     * @param recursive
     *  Whether the response should contain all encapsulated components or just the immediate children.
     *
     * @param verbose
     *  Whether to include any encapulated components or just details about the process group.
     *
     * @param processGroupId
     *  The id of the process group that is the parent of the requested resource(s). If the desired process group is the root group an alias 'root' may be used as the process-group-id.
     *
     * @return
     */
    ProcessGroupEntity getProcessGroup(String clientId, boolean recursive, boolean verbose, String processGroupId);

    ProcessGroupEntity updateProcessGroup(String clientID, double positionX, double positionY, String processGroupdId);

    ProcessGroupEntity createProcessGroup(String cliendId, double positionX, double positionY, String processGroupdId, String newProcessGroupName);

    ConnectionsEntity getConnections(String clientId, String processGroupId);

    ConnectionEntity createConnection(String cliendID, String processGroupId);

    FlowEntity instantiateTemplate(String processGroupId);

    TemplateEntity uploadTemplate(String processGroupId, TemplateDTO templateDTO);

    TemplateEntity uploadTemplate(String processGroupId, InputStream xmlTemplateInputStream);
}
