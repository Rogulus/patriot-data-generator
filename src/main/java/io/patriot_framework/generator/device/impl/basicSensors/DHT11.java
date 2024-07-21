/*
 * Copyright 2019 Patriot project
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

package io.patriot_framework.generator.device.impl.basicSensors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.patriot_framework.generator.dataFeed.DataFeed;
import io.patriot_framework.generator.device.passive.sensors.AbstractSensor;

/**
 * This class represents temperature and humidity sensor as Device Composition.
 * Constructor requires two DataFeeds, one for temperature, one for humidity.
 */
public class DHT11 extends AbstractSensor {

    @JsonCreator
    public DHT11(@JsonProperty("label") String label,
                 @JsonProperty("temperature") DataFeed temperature,
                 @JsonProperty("humidity") DataFeed humidity) {
        super(label, temperature, humidity);
    }
}
