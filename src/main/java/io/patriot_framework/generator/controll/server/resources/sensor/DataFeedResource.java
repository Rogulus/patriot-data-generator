/*
 * Copyright 2019 Patriot project
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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.patriot_framework.generator.dataFeed.DataFeed;
import io.patriot_framework.generator.device.passive.sensors.Sensor;
import io.patriot_framework.generator.utils.JSONSerializer;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * Children resource of {@link SensorRootResource}. It handles all requests
 * aimed for {@link DataFeed} control.
 */
public class DataFeedResource extends CoapResource {

    private Sensor sensor;

    private ObjectMapper mapper;

    /**
     * Creates new resource with name: dataFeed
     *
     * @param sensor instance of Sensor
     */
    public DataFeedResource(Sensor sensor) {
        super("dataFeed");
        this.sensor = sensor;

        JsonFactory factory = new JsonFactory();
        mapper = new ObjectMapper(factory);
    }


    public void handleGETWithLabel(CoapExchange exchange, String label) {
        List<DataFeed> dataFeeds = sensor.getDataFeeds();

        for (DataFeed dataFeed : dataFeeds) {
            if (dataFeed.getLabel().equals(label)) {
                String responseBody =  JSONSerializer.serializeDataFeed(dataFeed);
                exchange.respond(CoAP.ResponseCode.CONTENT, responseBody, MediaTypeRegistry.APPLICATION_JSON);
                return;
            }
        }

        exchange.respond(CoAP.ResponseCode.NOT_FOUND, "Data feed with given label not found");
    }


    /**
     * Method returns information about DataFeed
     *
     * @param exchange the CoapExchange for the simple API
     */
    @Override
    public void handleGET(CoapExchange exchange) {
        String label = exchange.getQueryParameter("label");

        if (label != null) {
            handleGETWithLabel(exchange, label);
            return;
        }

        List<DataFeed> dataFeeds = sensor.getDataFeeds();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            exchange.respond(CoAP.ResponseCode.CONTENT, objectMapper.writeValueAsString(dataFeeds));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Method should get new DataFeed in JSON format and add it to the list of DataFeeds
     * for particular sensor.
     *
     * @param exchange the CoapExchange for the simple API
     */
    @Override
    public void handlePOST(CoapExchange exchange) {
        exchange.accept();
        String body = exchange.getRequestText();

        DataFeed newDataFeed = null;
        try {
            newDataFeed = mapper.readValue(body, DataFeed.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<DataFeed> dataFeeds = sensor.getDataFeeds();
        for (DataFeed dataFeed : dataFeeds) {
            if (dataFeed.getLabel().equals(newDataFeed.getLabel())) {
                exchange.respond(CoAP.ResponseCode.CONFLICT, "Data feed with given label already exists");
                return;
            }
        }

        try {
            sensor.addDataFeed(newDataFeed);
        } catch (UnsupportedOperationException exception) {
            exchange.respond(CoAP.ResponseCode.CONFLICT, "Simple sensor already contains data feed");
        }

        exchange.respond(CoAP.ResponseCode.CREATED);
    }


    /**
     * Method should replace data feed with new provided DataFeed in JSON format
     * for particular sensor.
     *
     * @param exchange the CoapExchange for the simple API
     */
    @Override
    public void handlePUT(CoapExchange exchange) {
        exchange.accept();
        String label = exchange.getQueryParameter("label");
        String body = exchange.getRequestText();

        if (label == null || label.isEmpty()) {
            exchange.respond(CoAP.ResponseCode.BAD_REQUEST, "label is missing");
            return;
        }

        DataFeed newDataFeed;
        try {
            newDataFeed = mapper.readValue(body, DataFeed.class);
        } catch (IOException e) {
            e.printStackTrace();
            exchange.respond(CoAP.ResponseCode.BAD_REQUEST, "Invalid data feed format");
            return;
        }

        if(!Objects.equals(newDataFeed.getLabel(), label)) {
            exchange.respond(CoAP.ResponseCode.BAD_REQUEST, "Label of the data feed does not match label in the query");
            return;
        }

        List<DataFeed> dataFeeds = sensor.getDataFeeds();
        boolean replaced = false;

        for (DataFeed dataFeed : dataFeeds) {
            if (dataFeed.getLabel().equals(label)) {
                sensor.removeDataFeed(dataFeed);
                sensor.addDataFeed(newDataFeed);
                exchange.respond(CoAP.ResponseCode.CHANGED);
                return;
            }
        }

        try {
            sensor.addDataFeed(newDataFeed);
            exchange.respond(CoAP.ResponseCode.CREATED);
        } catch (UnsupportedOperationException exception) {
            exchange.respond(CoAP.ResponseCode.CONFLICT, exception.getMessage());
        }

    }

    @Override
    public void handleDELETE(CoapExchange exchange) {
        exchange.accept();
        String label = exchange.getQueryParameter("label");

        if (label == null || label.isEmpty()) {
            exchange.respond(CoAP.ResponseCode.BAD_REQUEST, "Label is missing");
            return;
        }

        List<DataFeed> dataFeeds = sensor.getDataFeeds();

        for (DataFeed dataFeed : dataFeeds) {
            if (dataFeed.getLabel().equals(label)) {
                sensor.removeDataFeed(dataFeed);
                break;
            }
        }

        exchange.respond(CoAP.ResponseCode.DELETED);
    }
}
