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

package org.apache.nifi.api.client.service.impl;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.UriInfo;

import org.apache.nifi.api.client.impl.NiFiAPIClient;
import org.apache.nifi.api.client.service.AbstractBaseService;
import org.apache.nifi.api.client.service.ProcessGroups;
import org.apache.nifi.web.api.ProcessGroupResource;
import org.apache.nifi.web.api.dto.ConnectionDTO;
import org.apache.nifi.web.api.dto.EntityFactory;
import org.apache.nifi.web.api.dto.PermissionsDTO;
import org.apache.nifi.web.api.dto.PositionDTO;
import org.apache.nifi.web.api.dto.ProcessGroupDTO;
import org.apache.nifi.web.api.dto.RevisionDTO;
import org.apache.nifi.web.api.dto.TemplateDTO;
import org.apache.nifi.web.api.dto.status.ConnectionStatusDTO;
import org.apache.nifi.web.api.dto.status.ProcessGroupStatusDTO;
import org.apache.nifi.web.api.entity.ConnectionEntity;
import org.apache.nifi.web.api.entity.ConnectionsEntity;
import org.apache.nifi.web.api.entity.FlowEntity;
import org.apache.nifi.web.api.entity.InstantiateTemplateRequestEntity;
import org.apache.nifi.web.api.entity.ProcessGroupEntity;
import org.apache.nifi.web.api.entity.TemplateEntity;

/**
 * Created by jdyer on 4/8/16.
 */
public class ProcessGroupsImplementation
    extends AbstractBaseService
    implements ProcessGroups {

    private EntityFactory entityFactory;

    public ProcessGroupsImplementation(String hostname, String port) {
        client = new NiFiAPIClient(hostname, port);
        this.entityFactory = new EntityFactory();
    }

    public ProcessGroupEntity getProcessGroup(String clientId, boolean recursive, boolean verbose, String processGroupId) {

        try {
            Method updatePGMethod = ProcessGroupResource.class.getMethod("getProcessGroup", String.class);
            Map<String, String> pathParams = new HashMap<String, String>();
            pathParams.put("id", processGroupId);
            return (ProcessGroupEntity) client.get(ProcessGroupResource.class, updatePGMethod, null, pathParams);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public ProcessGroupEntity updateProcessGroup(String clientID,
            double positionX,
            double positionY,
            String processGroupName) {

        PositionDTO positionDTO = new PositionDTO(positionX, positionY);
        ProcessGroupDTO processGroupDTO = new ProcessGroupDTO();
        processGroupDTO.setName(processGroupName);
        processGroupDTO.setPosition(positionDTO);

        RevisionDTO revisionDTO = new RevisionDTO();
        revisionDTO.setClientId(clientID);
        revisionDTO.setVersion(0l);
        revisionDTO.setLastModifier(clientID);

        PermissionsDTO permissionsDTO = new PermissionsDTO();
        permissionsDTO.setCanRead(true);
        permissionsDTO.setCanWrite(true);

        ProcessGroupStatusDTO processGroupStatusDTO = new ProcessGroupStatusDTO();

        ProcessGroupEntity entity = entityFactory.createProcessGroupEntity(processGroupDTO,
                revisionDTO, permissionsDTO, processGroupStatusDTO, null);

        try {
            Method updatePGMethod = ProcessGroupResource.class.getMethod("updateProcessGroup");
            return (ProcessGroupEntity) client.put(ProcessGroupResource.class, updatePGMethod, entity);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public ProcessGroupEntity createProcessGroup(String clientId,
            double positionX,
            double positionY,
            String processGroupdId,
            String newProcessGroupName) {

        PositionDTO positionDTO = new PositionDTO(positionX, positionY);
        ProcessGroupDTO processGroupDTO = new ProcessGroupDTO();
        processGroupDTO.setName(newProcessGroupName);
        processGroupDTO.setPosition(positionDTO);

        RevisionDTO revisionDTO = new RevisionDTO();
        revisionDTO.setClientId(clientId);
        revisionDTO.setVersion(0l);
        revisionDTO.setLastModifier(clientId);

        PermissionsDTO permissionsDTO = new PermissionsDTO();
        permissionsDTO.setCanRead(true);
        permissionsDTO.setCanWrite(true);

        ProcessGroupStatusDTO processGroupStatusDTO = new ProcessGroupStatusDTO();

        ProcessGroupEntity entity = entityFactory.createProcessGroupEntity(processGroupDTO,
                revisionDTO, permissionsDTO, processGroupStatusDTO, null);

        try {
            Method createPGMethod = ProcessGroupResource.class.getMethod("createProcessGroup",
                    HttpServletRequest.class,
                    String.class,
                    ProcessGroupEntity.class);

            Map<String, String> pathParams = new HashMap<String, String>();
            pathParams.put("id", processGroupdId);

            return (ProcessGroupEntity) client.post(ProcessGroupResource.class, createPGMethod, entity, pathParams, null);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public ConnectionsEntity getConnections(String clientID, String processGroupId) {
        try {
            Method getConnectionsMethod = null;
            try {
                getConnectionsMethod = ProcessGroupResource.class.getMethod("getConnections",
                        String.class);
            } catch (Exception ex) {
                System.out.println("mnf exception ....");
            }

            Map<String, String> pathParams = new HashMap<String, String>();
            pathParams.put("id", processGroupId);

            return (ConnectionsEntity) client.get(ProcessGroupResource.class, getConnectionsMethod, null, pathParams);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public ConnectionEntity createConnection(String clientID, String processGroupId) {

        try {
            Method createConnectionMethod = ProcessGroupResource.class.getMethod("createConnection",
                    HttpServletRequest.class,
                    String.class,
                    ConnectionEntity.class);

            //ConnectionDTO dto, RevisionDTO revision, PermissionsDTO permissions, ConnectionStatusDTO status
            ConnectionDTO connectionDTO = new ConnectionDTO();

            RevisionDTO revisionDTO = new RevisionDTO();
            revisionDTO.setClientId(clientID);
            revisionDTO.setVersion(0l);
            revisionDTO.setLastModifier(clientID);

            PermissionsDTO permissionsDTO = new PermissionsDTO();
            permissionsDTO.setCanRead(true);
            permissionsDTO.setCanWrite(true);

            ConnectionStatusDTO connectionStatusDTO = new ConnectionStatusDTO();


            return (ConnectionEntity) client.post(ProcessGroupResource.class, createConnectionMethod, entityFactory.createConnectionEntity(connectionDTO,
                    revisionDTO, permissionsDTO, connectionStatusDTO), null, null);

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public FlowEntity instantiateTemplate(String processGroupId) {

        try {
            Method createConnectionMethod = ProcessGroupResource.class.getMethod("instantiateTemplate",
                    HttpServletRequest.class,
                    String.class,
                    InstantiateTemplateRequestEntity.class);

            InstantiateTemplateRequestEntity templateRequestEntity = new InstantiateTemplateRequestEntity();
            templateRequestEntity.setOriginX(50.0);
            templateRequestEntity.setOriginY(50.0);
            templateRequestEntity.setTemplateId("JeremyUploadedTemplateID");

            return (FlowEntity) client.post(ProcessGroupResource.class, createConnectionMethod, templateRequestEntity, null, null);

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public TemplateEntity uploadTemplate(String processGroupId, TemplateDTO templateDTO) {
//        String xml = new String(TemplateSerializer.serialize(templateDTO));
//        InputStream in = null;
//        try {
//            in = IOUtils.toInputStream(xml, "UTF-8");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return uploadTemplate(processGroupId, in);
        return null;
    }

    public TemplateEntity uploadTemplate(String processGroupId, InputStream xmlTemplateInputStream) {
        try {
            Method uploadTemplateMethod = ProcessGroupResource.class.getMethod("uploadTemplate",
                    HttpServletRequest.class,
                    UriInfo.class,
                    String.class,
                    InputStream.class);

            Map<String, String> pathParams = new HashMap<String, String>();
            pathParams.put("id", processGroupId);

            return (TemplateEntity) client.post(ProcessGroupResource.class, uploadTemplateMethod, null, pathParams, xmlTemplateInputStream);

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
