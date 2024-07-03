/*
 * Copyright 2024 Patriot project
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.patriot_framework.generator.controll;

import io.patriot_framework.generator.controll.client.CoapControlClient;
import io.patriot_framework.generator.controll.client.CoapDataFeedHandler;
import io.patriot_framework.generator.controll.client.CoapSensorHandler;
import io.patriot_framework.generator.dataFeed.ConstantDataFeed;
import io.patriot_framework.generator.dataFeed.DataFeed;
import io.patriot_framework.generator.dataFeed.NormalDistVariateDataFeed;
import io.patriot_framework.generator.device.impl.basicSensors.Thermometer;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.elements.exception.ConnectorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DataFeedHandlerTest {

    String ip = "127.0.0.1";
    String port = "5683";
    String rootResource = "/";
    // Define the CoAP server URI

    String t1Resource = "/sensor/t1";
    String coapUri = "coap://" + ip +":" + port ;
    String t1uri = coapUri + t1Resource;

    Thermometer t1;
    Thermometer t2;

    CoapClient client;

    @BeforeEach
    public void serverSetup() {
        DataFeed df = new ConstantDataFeed(25);
        df.setLabel("df1");
        t1 = new Thermometer("t1", df);
        t1.registerToCoapServer();

        df = new NormalDistVariateDataFeed(35,1);
        df.setLabel("df2");
        t2 = new Thermometer("t2", df);
        t2.registerToCoapServer();

        client = new CoapClient(coapUri + rootResource);
    }


    @Test
    public void changeDataFeed() throws ConnectorException, IOException {
        CoapControlClient coapClient = new CoapControlClient(coapUri);
        CoapSensorHandler sensorHandler = coapClient.getSensor("t1");
        CoapDataFeedHandler dataFeedHandler = sensorHandler.getDataFeedHandler("df1");
        DataFeed df = new ConstantDataFeed(14.0);
        df.setLabel("df1");
        dataFeedHandler.updateDataFeed(df);

        assertEquals(14.0, t1.requestData().get(0).get(Double.class));
    }
}
