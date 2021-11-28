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

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.util.List;
import java.util.Map;
import java.util.UUID;

final class HttpBuilder {

    private UUID uuid;
    private String host;
    private int port;
    private final String requestMethod;
    private final String endpoint;
    private final String protocolVersion;
    private final Map<String, String> headers;
    private final Map<String, List<String>> queryParams;
    private final byte[] payload;

    public HttpBuilder(FullHttpRequest request) {
        requestMethod = request.method().toString();
        endpoint = request.uri().split("\\?", 2)[0];
        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.uri());
        queryParams = queryStringDecoder.parameters();
        protocolVersion = request.protocolVersion().toString();
        headers = RequestUtils.parseHttpHeaders(request.headers());
        payload = RequestUtils.payloadReader(request);
    }

    public HttpBuilder setUUID(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public HttpBuilder setHost(String host) {
        this.host = host;
        return this;
    }

    public HttpBuilder setPort(int port) {
        this.port = port;
        return this;
    }

    public HttpData build() {
        HttpMetaImpl meta = new HttpMetaImpl(uuid, host, port, requestMethod, endpoint, protocolVersion, headers, queryParams);
        return new HttpData(meta, payload);
    }
}
