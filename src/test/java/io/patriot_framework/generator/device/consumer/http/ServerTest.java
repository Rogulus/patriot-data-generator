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
import io.patriot_framework.generator.device.consumer.exceptions.ConsumerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ServerTest extends HttpTestBase {

    @BeforeEach
    void runServer() throws ConsumerException, IOException {
        super.runServer();
    }

    @Test
    void statusCode200(){
        given().when().get("http://localhost:8080").then().statusCode(200);
    }

    @Test
    public void responseTime() {
        given().when().get("http://localhost:8080").then().time(lessThan(1000L));
    }

    @Test
    void FIFO() throws IOException {
        for (int i = 1; i <= 3; i++) {
            createHttpClientWithPayload("payload-" + i, port, "text/plain");
            httpClient.execute(httpPost);
        }

        assertArrayEquals("payload-1".getBytes(), storage.get().getPayload());
    }

    @Test
    void storageContents() throws IOException {
        for (int i = 1; i <= 5; i++) {
            createHttpClientWithPayload("payload-" + i, port, "text/plain");
            httpClient.execute(httpPost);
        }

        List<Byte> expected = Stream.of("1", "2", "3", "4", "5")
                .map(s -> s.getBytes()[0])
                .collect(Collectors.toList());

        List<Byte> result = storage.get(5).stream()
                .map(ConsumerData::getPayload)
                .map(payload -> payload[payload.length - 1])
                .collect(Collectors.toList());

        assertEquals(expected, result);
    }
}
