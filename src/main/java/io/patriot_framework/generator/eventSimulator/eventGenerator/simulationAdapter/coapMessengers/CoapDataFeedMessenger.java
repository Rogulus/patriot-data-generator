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

package io.patriot_framework.generator.eventSimulator.eventGenerator.simulationAdapter.coapMessengers;

import io.patriot_framework.generator.Data;
import io.patriot_framework.generator.controll.client.CoapDataFeedHandler;
import io.patriot_framework.generator.dataFeed.DataConstantDataFeed;
import io.patriot_framework.generator.dataFeed.DataFeed;
import io.patriot_framework.generator.eventSimulator.eventGenerator.simulationAdapter.DataFeedMessenger;
import org.eclipse.californium.elements.exception.ConnectorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class CoapDataFeedMessenger implements DataFeedMessenger {
    public static Logger LOGGER = LoggerFactory.getLogger(CoapDataFeedMessenger.class);
    private CoapDataFeedHandler handler;
    public CoapDataFeedMessenger(CoapDataFeedHandler handler) {
        this.handler = handler;
    }

    @Override
    public boolean changeDataFeed(DataFeed dataFeed) {
        try {
            dataFeed.setLabel(handler.getDataFeedLabel());
            handler.updateDataFeed(dataFeed);
        } catch (ConnectorException | IOException e) {
            LOGGER.warn(e.toString());
            return false;
        }
        return true;
    }

    @Override
    public boolean updateData(Data data) {
        return changeDataFeed(new DataConstantDataFeed(data));
    }
}
