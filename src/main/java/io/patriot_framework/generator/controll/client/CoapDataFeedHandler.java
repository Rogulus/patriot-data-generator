/*
 * Copyright 2024 Patriot project
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

package io.patriot_framework.generator.controll.client;

import io.patriot_framework.generator.dataFeed.DataFeed;
import io.patriot_framework.generator.utils.JSONSerializer;
import org.eclipse.californium.elements.exception.ConnectorException;

import java.io.IOException;

public class CoapDataFeedHandler {
    CoapControlClient ccc;
    String dataFeedEndpoint;
    String dataFeedLabel;


    public CoapDataFeedHandler(CoapControlClient ccc, String dataFeedEndpoint, String dataFeedLabel) {
        this.ccc = ccc;
        this.dataFeedEndpoint = dataFeedEndpoint + "/" + dataFeedLabel;
        this.dataFeedLabel = dataFeedLabel;
    }

    public void updateDataFeed(DataFeed dataFeed) throws ConnectorException, IOException {
        ccc.put(dataFeedEndpoint, JSONSerializer.serializeDataFeed(dataFeed));
    }
}
