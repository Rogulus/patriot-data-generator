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
import io.patriot_framework.generator.coordinates.UndirectedGraphCoordinate;
import io.patriot_framework.generator.coordinates.UndirectedGraphSpace;
import io.patriot_framework.generator.device.impl.basicSensors.Default;
import io.patriot_framework.generator.device.passive.sensors.Sensor;
import io.patriot_framework.generator.eventGenerator.graphFire.ChildWithMatches;
import io.patriot_framework.generator.eventGenerator.graphFire.Fire;
import io.patriot_framework.generator.eventGenerator.graphFire.Room;
import io.patriot_framework.generator.eventGenerator.graphFire.RoomTempDataFeed;
import io.patriot_framework.generator.eventGenerator.graphFire.TemperatureDiffuser;
import org.apache.lucene.analysis.da.DanishAnalyzer;
import org.jgrapht.Graph;
import org.junit.jupiter.api.Test;
import org.jgrapht.graph.*;

import javax.sound.midi.Soundbank;
import java.util.HashMap;
import java.util.List;

// todo snad by to slo udelat templatovane na typ coordinatu
// nebo udelat vuci interface nejakeho coordinatu

public class GraphFireTest {
    @Test
    public void test() {

        UndirectedGraphSpace houseSpace = new UndirectedGraphSpace.UndirectedGraphSpaceBuilder()
                .addEdge("garage", "entrance")
                .addEdge("garage", "corridor")
                .addEdge("garage", "livingRoom")
                .addEdge("entrance", "corridor")
                .addEdge("corridor", "livingRoom")
                .addEdge("corridor", "workroom")
                .addEdge("livingRoom", "bedroom")
                .addEdge("workroom", "bedroom")
                .build();

        houseSpace.getAll().forEach(x -> x.setData("temperature", new Data(Integer.class, 20)));

        RoomTempDataFeed livingRoomDF = new RoomTempDataFeed(houseSpace.getCoordinate("livingRoom"));
        RoomTempDataFeed garageDF = new RoomTempDataFeed(houseSpace.getCoordinate("garage"));
        RoomTempDataFeed corridorDF = new RoomTempDataFeed(houseSpace.getCoordinate("corridor"));

        Sensor livingRoomThermometer = new Default("livingRoomThermometer", livingRoomDF);
        Sensor garageThermometer = new Default("garageThermometer", garageDF);
        Sensor corridorThermometer = new Default("corridorThermometer", corridorDF);

        TemperatureDiffuser diffuser = new TemperatureDiffuser(houseSpace);
        Fire fire = new Fire(houseSpace, 300);
        ChildWithMatches toby = new ChildWithMatches(houseSpace.getCoordinate("livingRoom"));
        ChildWithMatches sandra = new ChildWithMatches(houseSpace.getCoordinate("garage"));

        Conductor conductor = new Conductor();
        conductor.addSimulation(diffuser);
        conductor.addSimulation(fire);
        conductor.addSimulation(toby);
        conductor.addSimulation(sandra);

        conductor.addSimulation(livingRoomDF);
        conductor.addSimulation(garageDF);
        conductor.addSimulation(corridorDF);
        // todo zobecnit fire na cellular automat s nastavitelnou sirkou okoli?

        Thread conductorThread = new Thread(conductor);
        conductorThread.start();

        for(int i = 0; i < 120; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
            System.out.println("Time: " + i);
            System.out.println();

            houseSpace.getAll().forEach(x -> System.out.println(x));

            List<Data> temp = livingRoomThermometer.requestData();
            System.out.println(temp.get(0));

            List<Data> temp1 = corridorThermometer.requestData();
            System.out.println(temp1.get(0));

            List<Data> temp2 = garageThermometer.requestData();
            System.out.println(temp2.get(0));
        }
    }
}
