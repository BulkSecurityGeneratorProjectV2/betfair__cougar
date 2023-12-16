/*
 * Copyright 2013, The Sporting Exchange Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// Originally from UpdatedComponentTests/StandardTesting/RPC/RPC_Get_RequestTypes_String_HeaderParam_EscapedCharacters.xls;
package com.betfair.cougar.tests.updatedcomponenttests.standardtesting.rpc;

import com.betfair.testing.utils.cougar.assertions.AssertionUtils;
import com.betfair.testing.utils.cougar.beans.HttpCallBean;
import com.betfair.testing.utils.cougar.beans.HttpResponseBean;
import com.betfair.testing.utils.cougar.helpers.CougarHelpers;
import com.betfair.testing.utils.cougar.manager.AccessLogRequirement;
import com.betfair.testing.utils.cougar.manager.CougarManager;
import com.betfair.testing.utils.cougar.manager.RequestLogRequirement;

import org.testng.annotations.Test;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * Ensure that Cougar can correctly handle a string Header Param containing various encoded escaped characters (within a Batched JSON request)
 */
public class RPCGetRequestTypesStringHeaderParamEscapedCharactersTest {
    @Test
    public void doTest() throws Exception {
        // Set up the Http Call Bean to make the request
        CougarManager cougarManager1 = CougarManager.getInstance();
        HttpCallBean callBean = cougarManager1.getNewHttpCallBean("87.248.113.14");
        CougarManager cougarManager = cougarManager1;
        // Set the call bean to use JSON batching
        callBean.setJSONRPC(true);
        // Set the list of requests to make a batched call to
        Map[] mapArray2 = new Map[5];
        mapArray2[0] = new HashMap();
        mapArray2[0].put("method","nonMandatoryParamsOperation");
        mapArray2[0].put("params","[\"this & that\",\"qp\",{}]");
        mapArray2[0].put("id","\"And\"");
        mapArray2[1] = new HashMap();
        mapArray2[1].put("method","nonMandatoryParamsOperation");
        mapArray2[1].put("params","[\"hash#\",\"qp\",{}]");
        mapArray2[1].put("id","\"Hash\"");
        mapArray2[2] = new HashMap();
        mapArray2[2].put("method","nonMandatoryParamsOperation");
        mapArray2[2].put("params","[\"this & that is 100%\",\"qp\",{}]");
        mapArray2[2].put("id","\"Combo\"");
        mapArray2[3] = new HashMap();
        mapArray2[3].put("method","nonMandatoryParamsOperation");
        mapArray2[3].put("params","[\"colon:\",\"qp\",{}]");
        mapArray2[3].put("id","\"Colon\"");
        mapArray2[4] = new HashMap();
        mapArray2[4].put("method","nonMandatoryParamsOperation");
        mapArray2[4].put("params","[\"a space\",\"qp\",{}]");
        mapArray2[4].put("id","\"Space\"");
        callBean.setBatchedRequests(mapArray2);
        // Get current time for getting log entries later

        Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
        // Make JSON call to the operation requesting a JSON response
        cougarManager.makeRestCougarHTTPCall(callBean, com.betfair.testing.utils.cougar.enums.CougarMessageProtocolRequestTypeEnum.RESTJSON, com.betfair.testing.utils.cougar.enums.CougarMessageContentTypeEnum.JSON);
        // Get the response to the batched query (store the response for further comparison as order of batched responses cannot be relied on)
        HttpResponseBean response = callBean.getResponseObjectsByEnum(com.betfair.testing.utils.cougar.enums.CougarMessageProtocolResponseTypeEnum.RESTJSONJSON);
        // Convert the returned json object to a map for comparison
        CougarHelpers cougarHelpers4 = new CougarHelpers();
        Map<String, Object> map5 = cougarHelpers4.convertBatchedResponseToMap(response);
        AssertionUtils.multiAssertEquals("{\"id\":\"And\",\"result\":{\"headerParameter\":\"this & that\",\"queryParameter\":\"qp\"},\"jsonrpc\":\"2.0\"}", map5.get("responseAnd"));
        AssertionUtils.multiAssertEquals("{\"id\":\"Colon\",\"result\":{\"headerParameter\":\"colon:\",\"queryParameter\":\"qp\"},\"jsonrpc\":\"2.0\"}", map5.get("responseColon"));
        AssertionUtils.multiAssertEquals("{\"id\":\"Combo\",\"result\":{\"headerParameter\":\"this & that is 100%\",\"queryParameter\":\"qp\"},\"jsonrpc\":\"2.0\"}", map5.get("responseCombo"));
        AssertionUtils.multiAssertEquals("{\"id\":\"Hash\",\"result\":{\"headerParameter\":\"hash#\",\"queryParameter\":\"qp\"},\"jsonrpc\":\"2.0\"}", map5.get("responseHash"));
        AssertionUtils.multiAssertEquals("{\"id\":\"Space\",\"result\":{\"headerParameter\":\"a space\",\"queryParameter\":\"qp\"},\"jsonrpc\":\"2.0\"}", map5.get("responseSpace"));
        AssertionUtils.multiAssertEquals(200, map5.get("httpStatusCode"));
        AssertionUtils.multiAssertEquals("OK", map5.get("httpStatusText"));
        // Pause the test to allow the logs to be filled
        // generalHelpers.pauseTest(500L);
        // Check the log entries are as expected
        
        cougarManager.verifyRequestLogEntriesAfterDate(timeStamp, new RequestLogRequirement("2.8", "nonMandatoryParamsOperation"),new RequestLogRequirement("2.8", "nonMandatoryParamsOperation"),new RequestLogRequirement("2.8", "nonMandatoryParamsOperation"),new RequestLogRequirement("2.8", "nonMandatoryParamsOperation"),new RequestLogRequirement("2.8", "nonMandatoryParamsOperation") );
        
        cougarManager.verifyAccessLogEntriesAfterDate(timeStamp, new AccessLogRequirement("87.248.113.14", "/json-rpc", "Ok") );
    }

}