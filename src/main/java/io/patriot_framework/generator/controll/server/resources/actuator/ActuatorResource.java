/*
 * Copyright 2020 Patriot project
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

import io.patriot_framework.generator.Data;
import io.patriot_framework.generator.device.passive.actuators.Actuator;
import io.patriot_framework.generator.device.passive.actuators.stateMachine.State;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.server.resources.CoapExchange;

import java.util.List;

/**
 * Resource handling basic functionality for specific {@link Actuator}.
 * Its name is created from label of particular Actuator.
 */
public class ActuatorResource extends CoapResource {

    /**
     * Pattern to match usb-uri. "%s" should be mapped to the label of {@link Actuator}
     */
    public static final String PATTERN = "/actuator/%s$";

    /**
     * Instance of an Actuator for which is resource created for
     */
    private Actuator actuator;

    public ActuatorResource(Actuator actuator) {
        super(actuator.getLabel());
        this.actuator = actuator;

        getAttributes().setTitle("Actuator resources");
        add(new StateMachineHistoryResource(actuator));
    }

    /**
     * Method used for turning the Actuator ON/OFF
     *
     * @param exchange the CoapExchange for the simple API
     */
    @Override
    public void handlePOST(CoapExchange exchange) {
        exchange.accept();
        actuator.controlSignal(exchange.getRequestText());

        exchange.respond(CoAP.ResponseCode.CHANGED);
    }

    /**
     * Method used for returning the actual {@link State} of the Actuator
     *
     * @param exchange the CoapExchange for the simple API
     */
    @Override
    public void handleGET(CoapExchange exchange) {
        exchange.accept();
        List<Data> state = actuator.requestData();
        String result = state.get(0).get(String.class);

        exchange.respond(CoAP.ResponseCode.CONTENT, result);
    }
}