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

import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.UUID;

final class MqttBuilder {

    private UUID uuid;
    private final String topic;
    private final int id;
    private int qos;
    private boolean isDuplicate;
    private boolean isRetained;
    private byte[] payload;

    public MqttBuilder(String topic, MqttMessage message) {
        this.topic = topic;
        this.id = message.getId();
        this.qos = message.getQos();
        this.isDuplicate = message.isDuplicate();
        this.isRetained = message.isRetained();
        this.payload = message.getPayload();
    }

    public MqttBuilder setUUID(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public MqttData build() {
        MqttMetaImpl meta = new MqttMetaImpl(uuid, topic, id, qos, isDuplicate, isRetained);
        return new MqttData(meta, payload);
    }
}
