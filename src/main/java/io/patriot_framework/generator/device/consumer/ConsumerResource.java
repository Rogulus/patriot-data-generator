/*
 * Copyright 2021 Patriot project
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

package io.patriot_framework.generator.device.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.patriot_framework.generator.device.consumer.exceptions.ConsumerException;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.server.resources.CoapExchange;

public class ConsumerResource extends CoapResource {

    private Consumer consumer;

    public ConsumerResource(Consumer consumer) {
        super(consumer.getLabel());
        this.consumer = consumer;
    }

    @Override
    public void handleGET(CoapExchange exchange) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            exchange.respond(mapper.writeValueAsString(consumer.getContents()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handlePOST(CoapExchange exchange) {
        if (consumer.isEnabled()) {
            consumer.stop();
            consumer.setEnabled(false);
        } else {
            try {
                consumer.start();
                consumer.setEnabled(true);
            } catch (ConsumerException e) {
                e.printStackTrace();
            }
        }

        exchange.respond(CoAP.ResponseCode.CHANGED);
    }
}
