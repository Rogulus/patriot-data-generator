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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static io.restassured.RestAssured.given;

public class HttpsTest extends HttpTestBase {

    @BeforeEach
    void runServer() throws ConsumerException, IOException {
        String passphrase = "Patriot";
        SSLInit sslInit = new BasicSSLInit("src/test/resources/sslcerts/server_keystore.jks", passphrase,
                "src/test/resources/sslcerts/server_truststore.jks", passphrase);
        super.runServer(sslInit);
    }

    @Test
    void statusCode200(){
        given().relaxedHTTPSValidation("TLS").when().post("https://localhost:" + port).then().statusCode(200);
    }
}
