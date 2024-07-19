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

import io.patriot_framework.generator.controll.client.CoapActuatorHandler;
import io.patriot_framework.generator.controll.client.CoapControlClient;
import io.patriot_framework.generator.device.impl.basicActuators.BinaryActuator;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.elements.exception.ConnectorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ActuatorHandlerTest {
    String ip = "127.0.0.1";
    String port = "5683";
    String rootResource = "/";
    // Define the CoAP server URI

    String act1Resource = "/actuator/act1";
    String coapUri = "coap://" + ip +":" + port ;
    String act1Uri = coapUri + act1Resource;

    BinaryActuator act1;

    CoapClient client;

    @BeforeEach
    public void serverSetup() {
        act1 = new BinaryActuator("act1");
        act1.registerToCoapServer();

        client = new CoapClient(coapUri + rootResource);
    }

    @Test
    public void getStateHistoryTest() throws ConnectorException, IOException {
        CoapControlClient coapClient = new CoapControlClient(coapUri);

        CoapActuatorHandler actuatorHandler = coapClient.getActuator("act1");
        assertEquals("[Off]", actuatorHandler.getStateHistory().toString());

        act1.controlSignal("On");
        act1.controlSignal("Off");
        act1.controlSignal("On");

        assertEquals("[Off, On, Off, On]", actuatorHandler.getStateHistory().toString());
    }
}
