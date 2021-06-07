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

import io.patriot_framework.generator.device.consumer.exceptions.ConsumerException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HttpDataTest extends HttpTestBase {

    @Test
    void checkHttpData() throws ConsumerException, IOException {
        super.runServer();

        createHttpClientWithPayload("payload", port, "text/plain");
        httpClient.execute(httpPost);

        HttpData data = (HttpData) storage.get();
        HttpMeta meta = data.getMeta();

        assertEquals(server.getUUID(), meta.getUUID());

        LocalDateTime timestamp = meta.getTimestamp();
        assertTrue(timestamp.compareTo(LocalDateTime.now().minusSeconds(3)) >= 0);

        assertEquals("POST", meta.getRequestMethod());
        assertEquals("/endpoint", meta.getEndpoint());
        assertEquals("HTTP/1.1", meta.getProtocolVersion());

        Map<String, String> headers = meta.getHeaders();
        assertEquals("text/plain", headers.get("Content-type"));

        Map<String, List<String>> queryParams = meta.getQueryParams();
        assertEquals("value1", queryParams.get("key1").get(0));
        assertEquals("value2", queryParams.get("key1").get(1));
        assertEquals("value3", queryParams.get("key2").get(0));

        assertArrayEquals("payload".getBytes(), data.getPayload());
    }
}