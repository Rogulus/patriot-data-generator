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

import io.patriot_framework.generator.Data;
import io.patriot_framework.generator.eventGenerator.DiscreteTime;
import io.patriot_framework.generator.eventGenerator.SimulationBase;


public class FireSim extends SimulationBase {


    public void init() {
//        registerAwake(new DiscreteTime(10));
        publishOnTime(new Data(Integer.class, 10), "tempDiff", new DiscreteTime(5));
        publishOnTime(new Data(Integer.class, 20), "tempDiff", new DiscreteTime(6));
        publishOnTime(new Data(Integer.class, 30), "tempDiff", new DiscreteTime(7));
        publishOnTime(new Data(Integer.class, 33), "tempDiff", new DiscreteTime(8));
        publishOnTime(new Data(Integer.class, 35), "tempDiff", new DiscreteTime(9));
        publishOnTime(new Data(Integer.class, 36), "tempDiff", new DiscreteTime(10));
        publishOnTime(new Data(Integer.class, 57), "tempDiff", new DiscreteTime(11));
        publishOnTime(new Data(Integer.class, 70), "tempDiff", new DiscreteTime(12));
        publishOnTime(new Data(Integer.class, 109), "tempDiff", new DiscreteTime(13));
        publishOnTime(new Data(Integer.class, 100), "tempDiff", new DiscreteTime(14));
        publishOnTime(new Data(Integer.class, 121), "tempDiff", new DiscreteTime(15));
    }

    @Override
    public void awake() {
    }

    @Override
    public void receive(Data message, String topic) {
    }
}
