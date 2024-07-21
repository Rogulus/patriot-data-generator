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

package io.patriot_framework.generator.controll.server.resources.actuator;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.patriot_framework.generator.device.passive.actuators.Actuator;
import io.patriot_framework.generator.device.passive.actuators.stateMachine.StateMachine;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.server.resources.CoapExchange;

import java.util.Deque;


/**
 * Children resource of {@link ActuatorRootResource}. It handles all requests
 * aimed for history states of {@link StateMachine}.
 */
public class ActuatorHistoryResource extends CoapResource {

    Actuator actuator;
    private ObjectMapper mapper;

    public ActuatorHistoryResource(Actuator actuator) {
        super("stateHistory");
        this.actuator = actuator;

        JsonFactory factory = new JsonFactory();
        mapper = new ObjectMapper(factory);
    }


    /**
     * Method returns information state history of state machine and resets it
     *
     * @param exchange the CoapExchange for the simple API
     */
    @Override
    public void handleGET(CoapExchange exchange) {
        Deque<String> stateHistory = actuator.getStateMachine().getStateHistory();
        actuator.getStateMachine().clearStateHistory();
        try {
            exchange.respond(
                    CoAP.ResponseCode.CONTENT,
                    mapper.writeValueAsString(stateHistory)

            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
