package org.apache.nifi.device.registry.managed;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Created on 3/10/17.
 */

import org.apache.nifi.device.registry.NiFiDeviceRegistryConfiguration;
import org.apache.nifi.remote.client.SiteToSiteClient;
import org.apache.nifi.remote.client.SiteToSiteClientConfig;

import io.dropwizard.lifecycle.Managed;

public class Site2SiteManagedProxy
    implements Managed{

    private NiFiDeviceRegistryConfiguration configuration = null;

    public Site2SiteManagedProxy(NiFiDeviceRegistryConfiguration conf) {
        this.configuration = conf;
    }

    public void start() throws Exception {



        SiteToSiteClientConfig config = new SiteToSiteClient.Builder()
                .url("http://nifi.dev:9191/nifi")
                .portName("Data For Spark")
                .buildConfig();
    }

    public void stop() throws Exception {
        //Stop your managed instance. Called when Jetty is shut down.
    }
}
