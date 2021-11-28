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

import io.patriot_framework.generator.device.consumer.AbstractConsumerMeta;

import java.util.UUID;

final class MqttMetaImpl extends AbstractConsumerMeta implements MqttMeta {

    private final String topic;
    private final int id;
    private final int qos;
    private final boolean isDuplicate;
    private final boolean isRetained;

    public MqttMetaImpl(UUID uuid, String topic, int id, int qos, boolean isDuplicate, boolean isRetained) {
        super(uuid);
        this.topic = topic;
        this.id = id;
        this.qos = qos;
        this.isDuplicate = isDuplicate;
        this.isRetained = isRetained;
    }

    @Override
    public String getTopic() {
        return topic;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getQos() {
        return qos;
    }

    @Override
    public boolean isDuplicate() {
        return isDuplicate;
    }

    @Override
    public boolean isRetained() {
        return isRetained;
    }
}
