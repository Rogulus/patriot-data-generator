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
import io.patriot_framework.generator.coordinates.UndirectedGraphCoordinate;
import io.patriot_framework.generator.coordinates.UndirectedGraphSpace;
import io.patriot_framework.generator.eventGenerator.DiscreteTime;
import io.patriot_framework.generator.eventGenerator.SimulationBase;
import io.patriot_framework.generator.eventGenerator.Time;

import java.util.HashSet;

public class TemperatureDiffuser extends SimulationBase {
    private Time time = new DiscreteTime();
    private UndirectedGraphSpace space;

    public TemperatureDiffuser(UndirectedGraphSpace space) {
        this.space = space;

        space.getAll().stream().forEach(coordinate -> {
            Integer temp = coordinate.getData("temperature").get(Integer.class);
            coordinate.setData("new-temp", new Data(Integer.class, 0));
        });
    }

    public void init() {
        registerRecurringAwake(new DiscreteTime(1));
    }

    @Override
    public void awake() {

        space.getAll().forEach((coordinate) -> {
            HashSet<UndirectedGraphCoordinate> neighbors = coordinate.getNeighbors();

            int sum = neighbors.stream().mapToInt(x -> x.getData("temperature").get(Integer.class)).sum();
            Integer nSum = sum / neighbors.size();
            Integer myTemp = coordinate.getData("temperature").get(Integer.class);
            int newTemp = (myTemp * 10 + nSum) / 11;
            coordinate.setData("new-temp", new Data(Integer.class, newTemp));
        });

        space.getAll().forEach(coordinate -> {
            coordinate.setData("temperature", coordinate.getData("new-temp"));
        });
    }

    @Override
    public void receive(Data message, String topic){
    }
}



