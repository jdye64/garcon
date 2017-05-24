package com.github.jdye64.processors.provenance;

import java.io.File;

import org.apache.nifi.util.TestRunner;
import org.apache.nifi.util.TestRunners;
import org.junit.Before;
import org.junit.Test;


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
 * Created on 4/12/17.
 */


public class TestProvenanceEventsToPhoenix {

    private TestRunner testRunner;

    @Before
    public void init() {
        testRunner = TestRunners.newTestRunner(ProvenanceEventsToPhoenix.class);
        testRunner.setProperty(ProvenanceEventsToPhoenix.PHOENIX_TABLE_NAME, "NIFI_PROV");
    }

    @Test
    public void testParseProvEvents() throws Exception {
        testRunner.enqueue(new File("src/test/resources/sample_prov_events.json").toPath());
        testRunner.run();

        testRunner.assertTransferCount(ProvenanceEventsToPhoenix.REL_SUCCESS, 17);
//        testRunner.assertTransferCount(PutHTMLElement.REL_INVALID_HTML, 0);
//        testRunner.assertTransferCount(PutHTMLElement.REL_ORIGINAL, 1);
//        testRunner.assertTransferCount(PutHTMLElement.REL_NOT_FOUND, 0);
//
//        List<MockFlowFile> ffs = testRunner.getFlowFilesForRelationship(PutHTMLElement.REL_SUCCESS);
//        assertTrue(ffs.size() == 1);
//        String data = new String(testRunner.getContentAsByteArray(ffs.get(0)));
//
//        //Contents will be the entire HTML doc. So lets use Jsoup again just the grab the element we want.
//        Document doc = Jsoup.parse(data);
//        Elements eles = doc.select("#put");
//        Element ele = eles.get(0);
//
//        assertTrue(StringUtils.equals("<p>modified value</p> \n<a href=\"httpd://localhost\"></a>", ele.html()));
    }
}
