package org.apache.nifi.device.registry.resource.c2.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.nifi.device.registry.resource.c2.core.ops.C2Operation;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

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
 * Created on 7/11/17.
 */


public class C2OperationMapper
        implements ResultSetMapper<C2Operation>
{
    public C2Operation map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException
    {
        C2Operation o = new C2Operation();
        o.setOperationId(resultSet.getLong("OPERATION_ID"));
        o.setName(resultSet.getString("NAME"));
        o.setOperation(resultSet.getString("OPERATION"));
        String content = resultSet.getString("CONTENT");
        Map<String,String> contentMap = Maps.newHashMap();
        if (content != null && !content.isEmpty()) {
         String [] contentSplit=   StringUtils.split(content, ",");
         for(String contentStr : contentSplit){
             int firstDelimiter = contentStr.indexOf(":");
             if (firstDelimiter > 0) {
                 String key = content.substring(0, firstDelimiter);

                if (firstDelimiter+1 < content.length()) {
                    String value = content.substring(firstDelimiter + 1, content.length() );
                    contentMap.put(key, value);
                }
             }
         }
        }
        o.setContent(contentMap);
        o.setAcked(resultSet.getBoolean("ACKED"));
        //o.setLastResponseTimestamp(resultSet.getTimestamp("LAST_RESPONSE_TIMESTAMP"));
//        o.setAckedTimestamp(resultSet.getTimestamp("ACK_TIMESTAMP"));
        return o;
    }

}