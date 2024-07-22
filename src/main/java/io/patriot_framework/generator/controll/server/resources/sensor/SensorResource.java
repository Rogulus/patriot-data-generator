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

package io.patriot_framework.generator.controll.server.resources.sensor;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.patriot_framework.generator.device.passive.sensors.Sensor;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Resource handling basic functionality for specific {@link Sensor}.
 * Its name is created from label of particular Sensor.
 */
public class SensorResource extends CoapResource {

    /**
     * Pattern to match usb-uri. "%s" should be mapped to the label of {@link Sensor}
     */
    public static final String PATTERN = "/sensor/%s$";

    public static final Logger LOGGER = LoggerFactory.getLogger(DataFeedRootResource.class);

    /**
     * Instance of an Sensor for which is resource created for
     */
    private Sensor sensor;


    /**
     * Constructor passes label from sensor to the parent {@link CoapResource}, which creates
     * root resource for sensor and then it creates all children resource nodes with usage
     * of {@link DataFeedResource}. This tree should grow in the future with growing
     * functionality.
     *
     * @param sensor instance of Sensor
     */
    public SensorResource(Sensor sensor) {
        super(sensor.getLabel());
        this.sensor = sensor;
        getAttributes().setTitle("Sensor resources");
        add(new DataFeedRootResource(
                sensor,
                new ObjectMapper(new JsonFactory())));
        LOGGER.info("created new coap resource on path: " + getURI());
    }


    /**
     * Simple Get method which can be used for vitality check of resource.
     * <p>
     * Note: this should be changed in future to return basic information about sensor.
     *
     * @param exchange the CoapExchange for the simple API
     */
    @Override
    public void handleGET(CoapExchange exchange) {
        exchange.respond(sensor.toString());
    }


    /**
     * Method for enabling and disabling of sensor functionality.
     *
     * @param exchange the CoapExchange for the simple API
     */
    @Override
    public void handlePOST(CoapExchange exchange) {
        if (sensor.isEnabled()) {
            sensor.setEnabled(false);
        } else {
            sensor.setEnabled(true);
        }

        exchange.respond(CoAP.ResponseCode.CHANGED);
    }
}
