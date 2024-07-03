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

package io.patriot_framework.generator.controll.client;

import io.patriot_framework.generator.controll.server.resources.sensor.SensorResource;
import org.eclipse.californium.elements.exception.ConnectorException;

import java.io.IOException;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Class responsible for main client functionality. With help of CoaP client user should
 * have easy access to the main functionality of Devices. Standalone, this class
 * has no way of knowing what are available Devices with running CoaP endpoints.
 * This class matches functionality with right Device endpoint
 * (e.g. toggleDevice() with POST method and /deviceLabel resource).
 * Communication and Device discovery is handled solely by {@link CoapControlClient}.
 *
 * For example see: {@link CoapControlClient#getDevice(String)}
 */
public class CoapDeviceHandler {

    /**
     * Label of Device which endpoints are discovered
     */
    private String deviceLabel;

    /**
     * Set of visible sensor endpoints
     */
    protected Set<String> deviceEndpoints;

    /**
     * Coap Control Client
     */
    protected CoapControlClient ccc;

    public CoapDeviceHandler(CoapControlClient ccc, Set<String> deviceEndpoints, String deviceLabel) {
        this.deviceEndpoints = deviceEndpoints;
        this.ccc = ccc;
        this.deviceLabel = deviceLabel;
    }

    /**
     * With help of {@link SensorResource#PATTERN} choose right deviceEndpoints and make Post request
     * to toggle Sensor on/off.
     *
     * @throws ConnectorException if an issue specific to the connector occurred
     * @throws IOException if any other issue (not specific to the connector) occurred
     */
    public void toggleDevice() throws ConnectorException, IOException {
        Pattern pattern = Pattern.compile(String.format(SensorResource.PATTERN, deviceLabel));

        String toggleEndpoint = deviceEndpoints
                .stream()
                .filter(pattern.asPredicate())
                .findFirst()
                .orElse(null);

        ccc.post(toggleEndpoint, null);
    }
}
