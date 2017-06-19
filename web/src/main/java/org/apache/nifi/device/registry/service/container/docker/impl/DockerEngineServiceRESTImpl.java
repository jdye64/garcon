package org.apache.nifi.device.registry.service.container.docker.impl;

import org.apache.nifi.device.registry.service.container.docker.DockerEngineService;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;

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
 * Created on 6/7/17.
 */


public class DockerEngineServiceRESTImpl
    implements DockerEngineService {

    DockerClient dockerClient = null;

    public DockerEngineServiceRESTImpl() {
        DockerClientConfig dockerClientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost("tcp://jdyer-garcon0.field.hortonworks.com:4243")  //Currently setup just for WhiskeyCellar on my home network with open Docker REST API behind firewall
                .build();

        dockerClient = DockerClientBuilder.getInstance(dockerClientConfig).build();
    }

    public String startNiFiContainer(String image, int count) {
        System.out.println("Starting docker container .... " + image);

        CreateContainerResponse container = dockerClient.createContainerCmd(image)
                .exec();

        dockerClient.startContainerCmd(container.getId()).exec();
        return container.getId();
    }

    public void stopNiFiContainer(String containerId) {
        System.out.println("Stopping docker container with ID: " + containerId);
        dockerClient.stopContainerCmd(containerId).exec();
        System.out.println("stopped");
    }
}
