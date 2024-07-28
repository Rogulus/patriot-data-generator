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

package io.patriot_framework.generator.eventSimulator.eventGenerator.simulationAdapter.coapMessengers;

import io.patriot_framework.generator.controll.client.CoapActuatorHandler;
import io.patriot_framework.generator.eventSimulator.eventGenerator.simulationAdapter.ActuatorMessenger;
import org.eclipse.californium.elements.exception.ConnectorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Deque;

public class CoapActuatorMessenger implements ActuatorMessenger {
    public static Logger LOGGER = LoggerFactory.getLogger(CoapDataFeedMessenger.class);
    private CoapActuatorHandler handler;

    public CoapActuatorMessenger(CoapActuatorHandler handler) {
        this.handler = handler;
    }


    @Override
    public Deque<String> getStateHistory() {
        Deque<String> result;
        try {
            result = handler.getStateHistory();
        } catch (ConnectorException | IOException e) {
            LOGGER.warn(e.getMessage());
            return null;
        }
        return result;
    }
}
