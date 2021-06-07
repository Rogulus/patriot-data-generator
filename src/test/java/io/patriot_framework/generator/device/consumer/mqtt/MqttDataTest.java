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

import io.patriot_framework.generator.device.consumer.exceptions.ConsumerException;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class MqttDataTest extends MqttTestBase {

    @Test
    void basicDataTest() throws MqttException, ConsumerException {
        startSubscriber("front-door");
        summonPublisher();
        publish("front-door", "test message");

        MqttData data = (MqttData) storage.get();
        MqttMeta meta = data.getMeta();

        assertEquals(subscriber.getUUID(), meta.getUUID());

        LocalDateTime timestamp = meta.getTimestamp();
        assertTrue(timestamp.compareTo(LocalDateTime.now().minusSeconds(3)) >= 0);

        assertEquals("front-door", meta.getTopic());
        assertEquals(1, meta.getQos());

        assertFalse(meta.isDuplicate());
        assertFalse(meta.isRetained());

        assertArrayEquals("test message".getBytes(), data.getPayload());
    }

    @Test
    @Disabled
    void multiplePayloadsTest() throws MqttException, ConsumerException {
        startSubscriber("temperature-meter");
        summonPublisher();

        for (int i = 1; i <= 10; i++) {
            publish("temperature-meter", i + "°C");
        }

        for (int i = 1; i <= 10; i++) {
            String temperature = i + "°C";
            assertArrayEquals(temperature.getBytes(), storage.get().getPayload());
        }
    }

    @Test
    void bigPayload() throws MqttException, ConsumerException {
        byte[] message = new byte[500000];
        java.util.Arrays.fill(message, (byte) 's');

        startSubscriber("big-messages");
        summonPublisher();
        publish("big-messages", message);

        assertArrayEquals(message, storage.get().getPayload());
    }
}
