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

package io.patriot_framework.generator.serialization;

import io.patriot_framework.generator.dataFeed.ConstantDataFeed;
import io.patriot_framework.generator.dataFeed.DataFeed;
import io.patriot_framework.generator.dataFeed.DayTemperatureDataFeed;
import io.patriot_framework.generator.dataFeed.ExponentialDistDataFeed;
import io.patriot_framework.generator.dataFeed.LinearDataFeed;
import io.patriot_framework.generator.dataFeed.NormalDistVariateDataFeed;
import io.patriot_framework.generator.device.Device;
import io.patriot_framework.generator.device.impl.basicSensors.DHT11;
import io.patriot_framework.generator.device.impl.basicSensors.Default;
import io.patriot_framework.generator.device.impl.basicSensors.Hygrometer;
import io.patriot_framework.generator.device.impl.basicSensors.Thermometer;
import io.patriot_framework.generator.network.NetworkAdapter;
import io.patriot_framework.generator.network.Rest;
import io.patriot_framework.generator.network.wrappers.JSONWrapper;
import io.patriot_framework.generator.utils.JSONSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * @author <a href="mailto:jakub.smadis@gmail.com">Jakub Smadis</a>
 */
public class SensorSerializationTest {
    private File file;

    @BeforeEach
    void setup() throws IOException {
        file = File.createTempFile("thermometer", ".json");
        file.deleteOnExit();
    }

    @Test
    void serializeThermometer() throws IOException {
        DataFeed df = new NormalDistVariateDataFeed(18, 2);
        df.setLabel("kokino");
        NetworkAdapter na = new Rest("https://httpbin.org/post", new JSONWrapper());
        Device sensor = new Thermometer("simpleThermometer", df);
        sensor.setNetworkAdapter(na);

        JSONSerializer.serialize(sensor, file);

        String content = new String(Files.readAllBytes(file.toPath()));

        System.out.println("EHM");
        System.out.println(content);
        System.out.println("EHM");

        Device another = JSONSerializer.deserializeDevice(file);
        assert (another.equals(sensor));
    }

    @Test
    void serializeDHT11() {
        DataFeed temperature = new ExponentialDistDataFeed(0.02);
        DataFeed humidity = new NormalDistVariateDataFeed(30, 7);
        Device sensor = new DHT11("dht11", temperature, humidity);
        NetworkAdapter na = new Rest("https://httpbin.org/post", new JSONWrapper());
        sensor.setNetworkAdapter(na);

        JSONSerializer.serialize(sensor, file);
        Device another = JSONSerializer.deserializeDevice(file);
        assert (another.equals(sensor));
    }

    @Test
    void serializeDefaultBasicSensor() {
        DataFeed df = new ConstantDataFeed(0.42);
        Device sensor = new Default("default", df);

        JSONSerializer.serialize(sensor, file);
        Device another = JSONSerializer.deserializeDevice(file);
        assert (another.equals(sensor));
    }

    @Test
    void serializeHygrometer() {
        DataFeed df = new DayTemperatureDataFeed(0.48f, 42.42f);
        Device sensor = new Hygrometer("hygro", df);

        JSONSerializer.serialize(sensor, file);
        Device another = JSONSerializer.deserializeDevice(file);
        assert (another.equals(sensor));
    }

    @Test
    void serializeHygrometerLineDataFeed() {
        DataFeed df = new LinearDataFeed(42);
        Device sensor = new Hygrometer("hygro", df);

        JSONSerializer.serialize(sensor, file);
        Device another = JSONSerializer.deserializeDevice(file);
        assert (another.equals(sensor));
    }


}
