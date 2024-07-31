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

package io.patriot_framework.generator.controll.server.resources.sensor;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.patriot_framework.generator.dataFeed.DataFeed;
import io.patriot_framework.generator.device.passive.sensors.Sensor;
import io.patriot_framework.generator.utils.JSONSerializer;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.core.server.resources.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Represents a CoAP resource for handling concrete data-feed of the {@link Sensor}.
 * This resource implements methods GET, PUT and DELETE.
 */
public class DataFeedResource extends CoapResource {
    public static final Logger LOGGER = LoggerFactory.getLogger(DataFeedResource.class);
    private Sensor sensor;
    private String myLabel;
    private ObjectMapper mapper;

    private String getResourcePath() {
        var parent = getParent();
        StringBuilder path = new StringBuilder(getPath());
        while (parent != null) {
            path.insert(0, parent.getPath() + "/");
            parent = parent.getParent();
        }
        return path.toString();
    }

    /**
     * Constructs a new instance of NewDataFeedResource.
     *
     * @param sensor The sensor associated with the data feed
     * @param dataFeed The data feed of the sensor which should be associated with this resource
     * @param mapper The object mapper for serialization/deserialization
     */
    public DataFeedResource(Sensor sensor, DataFeed dataFeed, ObjectMapper mapper) {
        super(dataFeed.getLabel());
        myLabel = dataFeed.getLabel();
        this.sensor = sensor;
        this.mapper = mapper;
    }



    private DataFeed getMyDataFeed() {
        for(var dataFeed: sensor.getDataFeeds()) {
            if (dataFeed.getLabel().equals(myLabel)) return dataFeed;
        }
        return null;
    }


    /**
     * Handles a GET request by retrieving a data feed and serializing it to JSON for response.
     *
     * @param exchange The CoapExchange object representing the CoAP request and response
     */
    @Override
    public void handleGET(CoapExchange exchange) {
        var dataFeed = getMyDataFeed();
        if(dataFeed == null) {
            exchange.respond(CoAP.ResponseCode.NOT_FOUND, "Data feed with given label not found");
            return;
        }
        String responseBody = JSONSerializer.serializeDataFeed(dataFeed);
        exchange.respond(CoAP.ResponseCode.CONTENT, responseBody, MediaTypeRegistry.APPLICATION_JSON);
    }


    /**
     * Method should replace data feed with new provided DataFeed in JSON format
     * for corresponding sensor.
     * New data-feed must have same label as original data-feed.
     *
     * @param exchange The CoAP exchange object for handling the request and response
     */
    @Override
    public void handlePUT(CoapExchange exchange) {
        exchange.accept();
        String body = exchange.getRequestText();

        DataFeed newDataFeed;
        try {
            newDataFeed = mapper.readValue(body, DataFeed.class);
        } catch (IOException e) {
            exchange.respond(CoAP.ResponseCode.UNPROCESSABLE_ENTITY, "DataFeed cannot be deserialized");
            LOGGER.warn(e.toString());
            return;
        }

        if(newDataFeed.getLabel() == null || (!newDataFeed.getLabel().equals(myLabel))) {
            exchange.respond(CoAP.ResponseCode.UNPROCESSABLE_ENTITY, "DataFeed must have same label");
            return;
        }

        var myDataFeed = getMyDataFeed();
        if(myDataFeed != null) {
            sensor.removeDataFeed(myDataFeed);
            sensor.addDataFeed(newDataFeed);
            LOGGER.info("Data feed updated");
            exchange.respond(CoAP.ResponseCode.CHANGED);
            return;
        }

        try {
            sensor.addDataFeed(newDataFeed);
            getParent().add(new DataFeedResource(sensor, newDataFeed, mapper));
            exchange.respond(CoAP.ResponseCode.CREATED);
        } catch (UnsupportedOperationException exception) {
            exchange.respond(CoAP.ResponseCode.CONFLICT, exception.getMessage());
        }
    }


    /**
     * Handles a DELETE request for this CoAP resource.
     *
     * This method removes the data feed associated with this resource's sensor,
     * responds with a DELETED status code, and deletes corresponding resource from CoAP server.
     *
     * @param exchange The CoapExchange object representing the CoAP request and response
     */
    @Override
    public void handleDELETE(CoapExchange exchange) {
        var dataFeed = getMyDataFeed();
        sensor.removeDataFeed(dataFeed);
        exchange.respond(CoAP.ResponseCode.DELETED);
        Resource parent = getParent();
        if (parent != null) {
            parent.delete(this);
            LOGGER.info("Coap resource on path: " + getURI() + "deleted");
        }
    }
}
