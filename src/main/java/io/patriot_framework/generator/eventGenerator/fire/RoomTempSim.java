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
import io.patriot_framework.generator.eventGenerator.Time;

public class RoomTempSim extends SimulationBase {

    Time time = new DiscreteTime();
    Integer temperature = 0;


    @Override
    public void init() { // todo pridat do interface spojeneho s conductorem
        subscribe( "temperature");
        subscribe("tempDiff");
        time.setValue(3);
        registerAwake(time);
        publishOnTime(new Data(Integer.class, temperature), "temperature", time);
    }

    @Override
    public void awake() {
        time = eventBus.getTime();
        time.setValue(time.getValue() + 3);
        registerAwake(time);
        publish(new Data(Integer.class, temperature), "temperature");
    }

    @Override
    public void receive(Data message, String topic) {
        switch (topic) {
            case "temperature":
//                 temperature = (Integer) message;
//                 DiscreteTime newTime = (DiscreteTime)time.clone();
//                 newTime.setValue(time.getValue() + 3);
//                 publishOnTime(temperature + 10, "temperature", newTime);
//                 System.out.println("temperature: " + temperature);
                 break;
            case "tempDiff":
                Integer tempDif = message.get(Integer.class);
                temperature += tempDif;
        }
    }
}
