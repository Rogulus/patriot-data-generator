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

package io.patriot_framework.generator.controll.server.resources.sensor;

import io.patriot_framework.generator.dataFeed.ConstantDataFeed;
import io.patriot_framework.generator.dataFeed.DataFeed;
import io.patriot_framework.generator.device.impl.basicSensors.DHT11;
import io.patriot_framework.generator.device.impl.basicSensors.Thermometer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataFeedResourceTest {

    @Test
    public void test() throws InterruptedException {
        DataFeed ahoj = new ConstantDataFeed(5.0);
        ahoj.setLabel("df1");

        Thermometer t = new Thermometer("t1", ahoj);
        t.registerToCoapServer();

        DataFeed  humidity = new ConstantDataFeed(5.0);
        humidity.setLabel("df2");
        DHT11 dht = new DHT11("dht", ahoj, humidity);
        dht.registerToCoapServer();

        System.out.println("spinkam");
        Thread.sleep(1000000);
    }
}