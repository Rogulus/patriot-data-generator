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

package io.patriot_framework.generator.device.consumer.http;

import io.patriot_framework.generator.device.consumer.ConsumerData;
import io.patriot_framework.generator.device.consumer.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class StorageTest {
    Storage storage;
    ConsumerData invalidConsumerData = new HttpData(new HttpMetaImpl(UUID.randomUUID(), "host", 0,
            "requestMethod", "endpoint", "protocolVersion",
            new HashMap<>(), new HashMap<>()),
            "payload".getBytes());

    @BeforeEach
    void initVariables() {
        storage = new Storage();
    }

    @Test
    void writeNull() {
        assertDoesNotThrow(() -> storage.write(null));
    }

    @Test
    void writeOne() {
        assertDoesNotThrow(() -> storage.write(invalidConsumerData));
    }

    @Test
    void getEmpty() {
        assertNull(storage.get());
    }

    @Test
    void getOne() {
        storage.write(invalidConsumerData);
        assertEquals(invalidConsumerData, storage.get());
    }

    @Test
    void getTwo() {
        storage.write(invalidConsumerData);
        storage.write(invalidConsumerData);
        assertEquals(Arrays.asList(invalidConsumerData, invalidConsumerData), storage.get(2));
    }

    @Test
    void emptyTheStorage() {
        storage.write(invalidConsumerData);
        assertEquals(Collections.singletonList(invalidConsumerData), storage.get(2));
    }

    @Test
    void checkSize() {
        for (int expected = 1; expected <= 10; expected++) {
            storage.write(invalidConsumerData);
            assertEquals(expected, storage.size());
        }
    }

    @Test
    void checkSizeAfterEmptying(){
        storage.write(invalidConsumerData);
        storage.write(invalidConsumerData);
        storage.get(7);
        assertEquals(0, storage.size());
    }
}
