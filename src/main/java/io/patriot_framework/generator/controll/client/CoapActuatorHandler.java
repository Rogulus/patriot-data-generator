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

package io.patriot_framework.generator.controll.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.elements.exception.ConnectorException;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

public class CoapActuatorHandler extends CoapDeviceHandler {
    public CoapActuatorHandler(CoapControlClient ccc, Set<String> deviceEndpoints, String deviceLabel) {
        super(ccc, deviceEndpoints, deviceLabel);
    }

    public ArrayDeque<String> getStateHistory() throws ConnectorException, IOException {
        Pattern pattern = Pattern.compile(String.format("/%s(/|$)", "stateHistory"));
        Optional<String> stateHistoryEndpoint = deviceEndpoints.stream()
                .filter(pattern.asPredicate())
                .findFirst();

        if(stateHistoryEndpoint.isEmpty()) {
            return null;
        }

        CoapResponse coapResponse = ccc.get(stateHistoryEndpoint.get()); //todo get by mel vracet jen jednu custom exception pokud se posere spojeni, pokud je endpoint prazdny tak null pokud uspech, tak JSON nebo DTO
        String jsonResponse = coapResponse.getResponseText();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });
    }
}
