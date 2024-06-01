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

package io.patriot_framework.generator.eventGenerator;

import io.patriot_framework.generator.Data;
import io.patriot_framework.generator.device.impl.basicSensors.Default;
import io.patriot_framework.generator.device.passive.sensors.Sensor;
import io.patriot_framework.generator.eventGenerator.fire.FireSim;
import io.patriot_framework.generator.eventGenerator.fire.RoomProbe;
import io.patriot_framework.generator.eventGenerator.fire.RoomTempSim;
import org.junit.jupiter.api.Test;

import java.util.List;

public class EventBusTest {

    @Test
    public void test() {
        //todo udelat komentare
        Conductor conductor = new Conductor();

        RoomProbe probe = new RoomProbe();
        Sensor roomThermometer = new Default("roomThermometer", probe);

        conductor.addSimulation(new RoomTempSim());
        conductor.addSimulation(new FireSim());
        conductor.addSimulation(probe);

        Thread conductorThread = new Thread(conductor);
        conductorThread.start();


        for(int i = 0; i < 20; i++) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
            List<Data> temp = roomThermometer.requestData();
        }
    }
}
