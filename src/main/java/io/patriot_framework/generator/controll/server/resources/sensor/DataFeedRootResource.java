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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.patriot_framework.generator.dataFeed.DataFeed;
import io.patriot_framework.generator.device.passive.sensors.Sensor;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.core.server.resources.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;


/**
 * Children resource of {@link SensorResource}. It handles POST and GET requests
 * aimed for adding a new {@link DataFeed} to a parent {@link Sensor} and getting
 * all {@link DataFeed} of a parent {@link Sensor}.
 */
public class DataFeedRootResource extends CoapResource {
    private Sensor mySensor;
    ObjectMapper mapper;

    public static final Logger LOGGER = LoggerFactory.getLogger(DataFeedRootResource.class);

    private void logNewDataFeedResource(Resource resource) {
        LOGGER.info("created new DataFeed resource on path: " + resource.getURI());
    }

    /**
     * Constructs a DataFeedRootResource object with the given sensor and ObjectMapper.
     *
     * @param sensor The sensor associated with the data feed
     * @param mapper The ObjectMapper used for serialization and deserialization
     */
    public DataFeedRootResource(Sensor sensor, ObjectMapper mapper) {
        super("dataFeed");
        mySensor = sensor;
        this.mapper = mapper;
        for(var dataFeed: sensor.getDataFeeds()) {
            var newResource = new DataFeedResource(sensor, dataFeed, mapper);
            add(newResource);
            logNewDataFeedResource(newResource);
        }
    }


    /**
     * GET method which returns list of data-feeds associated with the {@link Sensor}
     *
     * @param exchange The CoAP exchange object for handling the request and response
     */
    @Override
    public void handleGET(CoapExchange exchange) {
        List<DataFeed> dataFeeds = mySensor.getDataFeeds();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            exchange.respond(
                    CoAP.ResponseCode.CONTENT,
                    objectMapper.writeValueAsString(dataFeeds),
                    MediaTypeRegistry.APPLICATION_JSON);
        } catch (JsonProcessingException e) {
            LOGGER.warn(e.toString());
            exchange.respond(
                    CoAP.ResponseCode.CONTENT,
                    "null",
                    MediaTypeRegistry.APPLICATION_JSON);
        }
    }


    /**
     * This POST method should get new DataFeed in JSON format and add it to the list of DataFeeds
     * for corresponding {@link Sensor}.
     *
     * @param exchange The CoAP exchange object for handling the request and response
     */
    @Override
    public void handlePOST(CoapExchange exchange) {
        exchange.accept();
        String body = exchange.getRequestText();

        DataFeed newDataFeed;
        try {
            newDataFeed = mapper.readValue(body, DataFeed.class);
        } catch (IOException e) {
            exchange.respond(CoAP.ResponseCode.UNPROCESSABLE_ENTITY, "DataFeed cannot be deserialized");
            return;
        }

        if(newDataFeed.getLabel() == null) {
            exchange.respond(CoAP.ResponseCode.UNPROCESSABLE_ENTITY, "DataFeed cannot have label null");
            return;
        }

        for (DataFeed dataFeed : mySensor.getDataFeeds()) {
            if (dataFeed.getLabel().equals(newDataFeed.getLabel())) {
                exchange.respond(CoAP.ResponseCode.CONFLICT, "Data feed with given label already exists");
                return;
            }
        }

        try {
            mySensor.addDataFeed(newDataFeed);
        } catch (UnsupportedOperationException exception) {
            exchange.respond(CoAP.ResponseCode.CONFLICT, "Simple sensor already contains data feed");
            return;
        }

        var newResource = new DataFeedResource(mySensor, newDataFeed, mapper);
        add(newResource);
        logNewDataFeedResource(newResource);
        exchange.respond(CoAP.ResponseCode.CREATED);
    }
}
