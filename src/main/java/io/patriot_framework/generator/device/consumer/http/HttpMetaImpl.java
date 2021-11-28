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

import io.patriot_framework.generator.device.consumer.AbstractConsumerMeta;

import java.util.List;
import java.util.Map;
import java.util.UUID;

final class HttpMetaImpl extends AbstractConsumerMeta implements HttpMeta {

    private String host;
    private int port;
    private String requestMethod;
    private String endpoint;
    private String protocolVersion;
    private Map<String, String> headers;
    private Map<String, List<String>> queryParams;

    public HttpMetaImpl(UUID uuid, String host, int port,
                        String requestMethod, String endpoint, String protocolVersion,
                        Map<String, String> headers, Map<String, List<String>> queryParams) {
        super(uuid);
        this.host = host;
        this.port = port;

        this.requestMethod = requestMethod;
        this.endpoint = endpoint;
        this.protocolVersion = protocolVersion;
        this.headers = headers;
        this.queryParams = queryParams;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public String getRequestMethod() {
        return requestMethod;
    }

    @Override
    public String getEndpoint() {
        return endpoint;
    }

    @Override
    public String getProtocolVersion() {
        return protocolVersion;
    }

    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public Map<String, List<String>> getQueryParams() {
        return queryParams;
    }
}
