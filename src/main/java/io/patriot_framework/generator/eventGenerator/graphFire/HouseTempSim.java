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

package io.patriot_framework.generator.eventGenerator.graphFire;

import io.patriot_framework.generator.Data;
import io.patriot_framework.generator.coordinates.Coordinate;
import io.patriot_framework.generator.eventGenerator.DiscreteTime;
import io.patriot_framework.generator.eventGenerator.SimulationBase;
import io.patriot_framework.generator.eventGenerator.Time;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class HouseTempSim  extends SimulationBase {
    private HashMap<Coordinate, Integer> heatMap = new HashMap<Coordinate, Integer>();


    private Graph<Room, DefaultEdge> houseSpace;
    private Time time = new DiscreteTime();

    public HouseTempSim(List<Coordinate> space) {
        for(Coordinate coordinate: space) {
            heatMap.put(coordinate, 20);
        }
    }


    @Override
    public void init() { // todo pridat do interface spojeneho s conductorem
        subscribe("tempDiff");
        time.setValue(1);
        registerAwake(time);
        for(Map.Entry<Coordinate, Integer> entry : heatMap.entrySet()) {
            publish(new Data(TempInfo.class, new TempInfo(entry.getKey(), entry.getValue())), "tempInfo");
        }
    }

    @Override
    public void awake() {
        time = eventBus.getTime();
        time.setValue(time.getValue() + 3);
        registerAwake(time);
        for(Map.Entry<Coordinate, Integer> entry : heatMap.entrySet()) {
            publish(new Data(TempInfo.class, new TempInfo(entry.getKey(), entry.getValue())), "tempInfo");
        }
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
                TempDiff tempDiff = message.get(TempDiff.class);
                Integer temp = heatMap.get(tempDiff.coordinate);
                heatMap.put(tempDiff.coordinate, temp + tempDiff.tempDiff);
        }
    }




}
