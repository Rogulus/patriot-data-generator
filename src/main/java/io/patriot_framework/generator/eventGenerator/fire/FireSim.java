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

package io.patriot_framework.generator.eventGenerator.fire;

import io.patriot_framework.generator.eventGenerator.DiscreteTime;
import io.patriot_framework.generator.eventGenerator.EventBus;
import io.patriot_framework.generator.eventGenerator.SimulationBase;
import io.patriot_framework.generator.eventGenerator.Time;


public class FireSim extends SimulationBase {


    public void init() {
//        registerAwake(new DiscreteTime(10));
        publishOnTime(10, "tempDiff", new DiscreteTime(5));
        publishOnTime(20, "tempDiff", new DiscreteTime(6));
        publishOnTime(20, "tempDiff", new DiscreteTime(7));
        publishOnTime(33, "tempDiff", new DiscreteTime(8));
        publishOnTime(35, "tempDiff", new DiscreteTime(9));
        publishOnTime(36, "tempDiff", new DiscreteTime(10));
        publishOnTime(57, "tempDiff", new DiscreteTime(11));
        publishOnTime(58, "tempDiff", new DiscreteTime(12));
        publishOnTime(109, "tempDiff", new DiscreteTime(13));
        publishOnTime(100, "tempDiff", new DiscreteTime(14));
        publishOnTime(121, "tempDiff", new DiscreteTime(15));
    }

    @Override
    public void awake() {
    }

    @Override
    public void receive(Object message, String topic) {
    }
}
