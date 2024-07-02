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

package io.patriot_framework.generator.serialization;

import io.patriot_framework.generator.dataFeed.ConstantDataFeed;
import io.patriot_framework.generator.dataFeed.DataFeed;
import io.patriot_framework.generator.dataFeed.DayTemperatureDataFeed;
import io.patriot_framework.generator.dataFeed.ExponentialDistDataFeed;
import io.patriot_framework.generator.dataFeed.LinearDataFeed;
import io.patriot_framework.generator.dataFeed.NormalDistVariateDataFeed;
import io.patriot_framework.generator.utils.JSONSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class DataFeedSerializationTest {
    private File file;

    @BeforeEach
    void setup() throws IOException {
        file = File.createTempFile("dataFeed", ".json");
        file.deleteOnExit();
    }

    private void serializeDataFeedTest(DataFeed dataFeed) {
        dataFeed.setLabel("label");
        JSONSerializer.serializeDataFeed(dataFeed, file);
        DataFeed another = JSONSerializer.deserializeDataFeed(file);
        assert (another.equals(dataFeed));
    }

    @Test
    void serializeConstantDataFeed() {
        DataFeed dataFeed = new ConstantDataFeed(0.0);
        serializeDataFeedTest(dataFeed);
    }

    @Test
    void serializeDayTemperatureDataFeed() {
        DataFeed dataFeed = new DayTemperatureDataFeed(15.3f, 35.2f);
        serializeDataFeedTest(dataFeed);
    }

    @Test
    void ExponentialDistDataFeed() {
        DataFeed dataFeed = new ExponentialDistDataFeed(1.2);
        serializeDataFeedTest(dataFeed);
    }

    @Test
    void LinearDataFeed() {
        DataFeed dataFeed = new LinearDataFeed(20.0);

    }

    @Test
    void NormalDistVariateDataFeed() {
        DataFeed dataFeed = new NormalDistVariateDataFeed(18, 2);
        serializeDataFeedTest(dataFeed);
    }

}
