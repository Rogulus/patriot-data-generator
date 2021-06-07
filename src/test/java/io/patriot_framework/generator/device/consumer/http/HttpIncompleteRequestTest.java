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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.*;

import static org.junit.jupiter.api.Assertions.*;

public class HttpIncompleteRequestTest extends HttpTestBase {

    public void sendIncompleteRequest() {
        try {
            String hostname = "localhost";
            InetAddress addr = InetAddress.getByName(hostname);
            Socket socket = new Socket(addr, 8080);

            String payload = "Payload";
            String httpMessage = "POST / HTTP/1.0\r\nAccept: */*\r\n"
                    + "Host: " + hostname + ":" + port + "\r\n"
                    + "Content-Type: text/plain\r\n"
                    + "Content-Length: " + payload.length() + "\r\n"
                    + "\r\n\r\n" +payload + "\r\n\r\n";

            if(socket.isConnected()) {
                System.out.println("Socket is connected to: " + socket.getInetAddress() + " on port: " + socket.getPort());

                OutputStream os = socket.getOutputStream();
                os.write(httpMessage.getBytes("ASCII"));
                os.flush();

                os.close();
            }
            socket.close();
            System.out.println("Socket closed");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void timeoutRequest() throws IOException {
        String hostname = "localhost";
        URL url = new URL("http://" + hostname +":8080");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        String payload = "Payload";

        connection.setRequestMethod("POST");
        connection.setReadTimeout(3000);
        connection.setDoOutput(true);

        try (DataOutputStream writer = new DataOutputStream(connection.getOutputStream())) {
            for (byte ch: payload.getBytes("ASCII")) {
                writer.write(ch);
                Thread.sleep(1000);
            }
            writer.flush();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
    }

    @Test
    @Disabled
    public void incompleteRequest() throws ConsumerException, IOException {
        runServer();
        sendIncompleteRequest();

        HttpData data = (HttpData) storage.get();
        HttpMeta meta = data.getMeta();

        assertArrayEquals("Payload".getBytes(), data.getPayload());
    }

    @Test
    public void requestTimeoutTest() throws ConsumerException, IOException {
        runServer();
        timeoutRequest();

        assertNull(storage.get());
    }
}
