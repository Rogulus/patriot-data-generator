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

import io.patriot_framework.generator.device.consumer.Storage;
import io.patriot_framework.generator.device.consumer.exceptions.ConsumerException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.AfterEach;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public abstract class HttpTestBase {

    Server server;
    static int port = 8080;
    Storage storage = new Storage();
    HttpClient httpClient;
    HttpPost httpPost;

    void createHttpClientWithPayload(String payload, int port, String contentType) throws UnsupportedEncodingException {
        httpClient = HttpClients.createDefault();

        httpPost = new HttpPost("http://localhost:" + port + "/endpoint?key1=value1&key1=value2&key2=value3");
        httpPost.setHeader("Content-type", contentType);
        httpPost.setEntity(new StringEntity(payload));
    }

    // TODO ? @mijaros -> @BeforeAll
    void runServer() throws ConsumerException, IOException {
        storage = new Storage();
        server = new Server("localhost", port, storage);
        server.start();
    }

    void runServer(SSLInit sslInit) throws ConsumerException, IOException {
        storage = new Storage();
        server = new Server("localhost", port, storage, sslInit);
        server.start();
    }

    // TODO ? @mijaros -> @AfterAll
    @AfterEach
    void closeServer() {
        server.stop();
    }
}
