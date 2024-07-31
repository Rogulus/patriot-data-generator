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

import io.patriot_framework.generator.dataFeed.ConstantDataFeed;
import io.patriot_framework.generator.dataFeed.DataFeed;
import io.patriot_framework.generator.device.impl.basicActuators.BinaryActuator;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.elements.exception.ConnectorException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;

public class ActuatorHistoryResourceTest {
    String ip = "127.0.0.1";
    String port = "5683";
    String rootResource = "/";
    // Define the CoAP server URI

    String act1Resource = "/actuator/act1";
    String coapUrl = "coap://" + ip + ":" + port;
    String act1uri = coapUrl + act1Resource;

    BinaryActuator act1;

    CoapClient client;


    private DataFeed createConstantDataFeed(double value, String label) {
        DataFeed dataFeed = new ConstantDataFeed(value);
        dataFeed.setLabel(label);
        return dataFeed;
    }

    private String buildRequestUri(String path, String label) {
        return coapUrl + path + "?label=" + label;
    }

    private CoapResponse sendPutRequest(CoapClient client, String payload) throws ConnectorException, IOException {
        return client.put(payload, MediaTypeRegistry.APPLICATION_JSON);
    }

    private CoapResponse sendPostRequest(CoapClient client, String payload) throws ConnectorException, IOException {
        return client.post(payload, MediaTypeRegistry.APPLICATION_JSON);
    }


    @BeforeEach
    public void serverSetup() throws InterruptedException {
        act1 = new BinaryActuator("act1");
        act1.registerToCoapServer();

        act1.controlSignal("On");
        Thread.sleep(2000);
        act1.controlSignal("Off");
        Thread.sleep(2000);

        act1.controlSignal("On");
        Thread.sleep(2000);

        client = new CoapClient(coapUrl + rootResource);

    }

    @AfterEach
    public void shutdownClient() {
        // Shutdown the client
        client.shutdown();
    }
}
