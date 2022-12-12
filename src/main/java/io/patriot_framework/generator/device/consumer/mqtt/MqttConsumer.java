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

package io.patriot_framework.generator.device.consumer.mqtt;

import io.patriot_framework.generator.Data;
import io.patriot_framework.generator.device.AbstractDevice;
import io.patriot_framework.generator.device.consumer.Consumer;
import io.patriot_framework.generator.device.consumer.ConsumerData;
import io.patriot_framework.generator.device.consumer.Storage;
import io.patriot_framework.generator.device.consumer.exceptions.ConsumerException;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.NotImplementedException;

import java.util.List;

public final class MqttConsumer extends AbstractDevice implements Consumer {

    private MqttAsyncClient mqttClient;
    private String broker;
    private String clientId;
    private String topic;
    private int qos;
    private Storage storage;

    public static final Logger LOGGER = LoggerFactory.getLogger(MqttConsumer.class);

    public MqttConsumer(String broker, String topic, String clientId, int qos, Storage storage) {
        this.broker = broker;
        this.clientId = clientId;
        this.qos = qos;
        this.storage = storage;
        this.topic = topic;
    }

    public void start() throws ConsumerException {
        try {
            LOGGER.info("Trying to connect MQTT client to " + broker);
            mqttClient = new MqttAsyncClient(broker, clientId);

            IMqttToken token = mqttClient.connect();
            token.waitForCompletion();

            LOGGER.info("MQTT client connected to " + broker);

            mqttClient.setCallback(new Callback(getUUID(), storage));
            mqttClient.subscribe(topic, qos);

            LOGGER.info("MQTT client subscribed to " + topic + " with QoS " + qos);
        } catch (MqttException e) {
            LOGGER.error("Initialization of MQTT client failed: " + e);
            LOGGER.debug("Cause of MQTT error: " + e.getCause() + "\nBroker URI: {} \nTopic: {} \nClient ID: {} \nQoS: {}", broker, topic, clientId, qos);
            throw new ConsumerException("MqttException", e);
        }
    }

    @Override
    public void stop() {
        if (mqttClient.isConnected()) {
            try {
                mqttClient.disconnect();
                LOGGER.info("MQTT client disconnected from " + broker);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<ConsumerData> getContents() {
        return storage.getAll();
    }

    @Override
    public List<Data> requestData(Object... params) {
        throw new NotImplementedException("");
    }
}
