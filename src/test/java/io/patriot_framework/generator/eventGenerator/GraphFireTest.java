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

import io.patriot_framework.generator.coordinates.UndirectedGraphCoordinate;
import io.patriot_framework.generator.device.impl.basicSensors.Default;
import io.patriot_framework.generator.device.passive.sensors.Sensor;
import io.patriot_framework.generator.eventGenerator.graphFire.Room;
import io.patriot_framework.generator.eventGenerator.graphFire.RoomTempDataFeed;
import org.jgrapht.Graph;
import org.junit.jupiter.api.Test;
import org.jgrapht.graph.*;

// todo snad by to slo udelat templatovane na typ coordinatu

public class GraphFireTest {
    @Test
    public void test() {

        UndirectedGraphCoordinate livingRoomCoordinate = new UndirectedGraphCoordinate.UndirectedGraphBuilder()
                .addEdge("garage", "entrance")
                .addEdge("garage", "corridor")
                .addEdge("garage", "livingRoom")
                .addEdge("entrance", "corridor")
                .addEdge("corridor", "livingRoom")
                .addEdge("corridor", "workroom")
                .addEdge("livingRoom", "bedroom")
                .addEdge("workroom", "bedroom")
                .build("livingRoom");

        RoomTempDataFeed livingRoomDF = new RoomTempDataFeed(livingRoomCoordinate); // todo asi to chce reprezentaci grafu
        RoomTempDataFeed garageDF = new RoomTempDataFeed(livingRoomCoordinate.getCoordinate("garage"));
        RoomTempDataFeed corridorDF = new RoomTempDataFeed(livingRoomCoordinate.getCoordinate("corridor"));

        Sensor livingRoomThermometer = new Default("livingRoomThermometer", livingRoomDF);
        Sensor garageThermometer = new Default("garageThermometer", garageDF);
        Sensor corridorThermometer = new Default("corridorThermometer", corridorDF);

        Conductor conductor = new Conductor();
        conductor.addSimulation(livingRoomDF);
        conductor.addSimulation(garageDF);
        conductor.addSimulation(corridorDF);


        // todo zobecnit fire na cellular automat s nastavitelnou sirkou okoli?







    }
}


 */

package io.patriot_framework.generator.eventGenerat
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


//        Graph<Room, DefaultEdge> houseSpace = new DefaultUndirectedGraph<>(DefaultEdge.class);
//        //todo definovat v package interface zprav ktere se predavaji
//        //todo topic by mela byt classa
//        Room garage = new Room("garage", 20);
//        Room entrance = new Room("entrance", 20);
//        Room corridor = new Room("corridor", 20);
//        Room livingRoom = new Room("livingRoom", 20);
//        Room workroom = new Room("workroom", 20);
//        Room bedroom = new Room("bedroom", 20);
//
//        houseSpace.addEdge(garage, entrance);
//        houseSpace.addEdge(garage, corridor);
//        houseSpace.addEdge(garage, livingRoom);
//        houseSpace.addEdge(entrance, corridor);
//        houseSpace.addEdge(corridor, livingRoom);
//        houseSpace.addEdge(corridor, workroom);
//        houseSpace.addEdge(livingRoom, bedroom);
//        houseSpace.addEdge(workroom, bedroom);