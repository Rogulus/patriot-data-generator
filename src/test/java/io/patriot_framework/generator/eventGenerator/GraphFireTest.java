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
import io.patriot_framework.generator.coordinates.UndirectedGraphSpace;
import io.patriot_framework.generator.device.impl.basicSensors.Default;
import io.patriot_framework.generator.device.passive.sensors.Sensor;
import io.patriot_framework.generator.eventGenerator.graphFire.ChildWithMatches;
import io.patriot_framework.generator.eventGenerator.graphFire.Fire;
import io.patriot_framework.generator.eventGenerator.graphFire.RoomTempDataFeed;
import io.patriot_framework.generator.eventGenerator.graphFire.TemperatureDiffuser;
import org.junit.jupiter.api.Test;

import java.io.IOException;

// todo snad by to slo udelat templatovane na typ coordinatu
// nebo udelat vuci interface nejakeho coordinatu

public class GraphFireTest {
    @Test
    public void test() throws InterruptedException, IOException {

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

        System.out.println("THREADID:");
        System.out.println(Thread.currentThread().getId());
        conductor.runUntil(new DiscreteTime(5));
        conductor.runRealTimeFor(new DiscreteTime(5));
        Thread.sleep(3000);
        conductor.pause();
        System.out.println("hsdjsd");



        System.out.println("11111");
        conductor.runUntil(new DiscreteTime(20));
        System.out.println("Sd");
        conductor.runRealTimeFor(new DiscreteTime(5));
        System.out.println("dsd");
        Thread.sleep(5010);
        conductor.runRealTime();
        Thread.sleep(5000);
        conductor.pause();


        // todo
        // po spustenem runu by nemelo jit znova spustit run
        // nice to have cli wrapper
        //todo doresit jestli running promena j k necemu
        // otazka jestli prommene pause davaji smysl v event bus ne aot byt stop?

        System.out.println("hello");
        Thread.sleep(50000);
        System.out.println("end");
    }
}
