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

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.WebLink;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.elements.exception.ConnectorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * CoaP Control Client for Data Generator Tool, using {@link CoapClient} from Californium CoaP library.
 * This class should be used with cooperation with Resource Handler classes
 * as described in {@link #getDevice(String label)}.
 *
 * Note: Consider extending CoapClient instead of utilization of it.
 */
public class CoapControlClient {

    public static Logger LOGGER = LoggerFactory.getLogger(CoapControlClient.class);
    /**
     * The destination URI
     */
    private String uri;

    /**
     * Class build on top of {@link CoapClient} functionality
     */
    private CoapClient client = new CoapClient();


    public CoapControlClient(String uri) {
        this.uri = uri;
        client.setURI(uri);
    }

    public CoapControlClient(String ip, int port) {
        this.uri = "coap://" + ip + ":" + port;
        client.setURI(uri);
    }

    /**
     * Get request using {@link CoapClient#get()}. This method provides
     * additional URI management.
     *
     * @param resource name of the resource
     * @return the CoAP response
     * @throws ConnectorException if an issue specific to the connector occurred
     * @throws IOException if any other issue (not specific to the connector) occurred
     */
    public CoapResponse get(String resource) throws ConnectorException, IOException {
        client.setURI(getUri() + resource);
        return client.get();
    }

    /**
     * Post request using {@link CoapClient#post(String payload, int format)}.
     * This method provides additional URI management.
     *
     * @param resource name of the resource
     * @param payload the payload
     * @return the CoAP response
     * @throws ConnectorException if an issue specific to the connector occurred
     * @throws IOException if any other issue (not specific to the connector) occurred
     */
    public CoapResponse post(String resource, String payload) throws ConnectorException, IOException {
        client.setURI(getUri() + resource);
        return client.post(payload, MediaTypeRegistry.TEXT_PLAIN);
    }

    public CoapResponse put(String resource, String payload) throws ConnectorException, IOException {
        client.setURI(getUri() + resource);

        return client.put(payload, MediaTypeRegistry.TEXT_PLAIN);
    }

    /**
     * Gets all resources/endpoints for specified label of an Device.
     * To achieve user friendly workflow, we implemented client-server communication with
     * Chain of Responsibility Design Pattern. Therefore this method returns {@link CoapDeviceHandler},
     * which is responsible for more accurate functionality.
     *
     * Example:
     * CoapControlClient ccc = new CoapControlClient("url");
     * ccc.getDevice("deviceLabel").toggleDevice();
     *
     * This example will get all sensor resources with label: "deviceLabel" and toggleDevice()
     * will choose right endpoint to turn the sensor on/off.
     *
     *
     * @param label the label of Device
     * @return an instance of ClientResourceHandler
     * @throws ConnectorException if an issue specific to the connector occurred
     * @throws IOException if any other issue (not specific to the connector) occurred
     */
    public CoapDeviceHandler getDevice(String label) throws ConnectorException, IOException {
        Pattern pattern = Pattern.compile(String.format("/%s(/|$)", label));
        return new CoapDeviceHandler(this, getEndpoints(pattern), label);
    }


    public CoapSensorHandler getSensor(String label) {
        Pattern pattern = Pattern.compile(String.format("/sensor/%s(/|$)", label));
        return new CoapSensorHandler(this, getEndpoints(pattern), label);
    }


    public CoapActuatorHandler getActuator(String label) {
        Pattern pattern = Pattern.compile(String.format("/actuator/%s(/|$)", label));
        return new CoapActuatorHandler(this, getEndpoints(pattern), label);
    }


    public String getUri() {
        return uri;
    }


    public void setUri(String uri) {
        this.uri = uri;
    }


    private Set<String> getEndpoints(Pattern pattern) {
        Set<String> deviceEndpoints;
        try {
            deviceEndpoints = client.discover()
                    .stream()
                    .map(WebLink::getURI)
                    .filter(pattern.asPredicate())
                    .collect(Collectors.toSet());
        } catch ( ConnectorException | IOException e) {
            LOGGER.warn(e.getMessage());
            return null;
        }
        return deviceEndpoints;
    }
}
