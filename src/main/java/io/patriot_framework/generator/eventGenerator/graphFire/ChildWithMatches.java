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
import io.patriot_framework.generator.eventGenerator.DiscreteTime;
import io.patriot_framework.generator.eventGenerator.SimulationBase;
import io.patriot_framework.generator.eventGenerator.Time;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class ChildWithMatches extends SimulationBase {
    private UndirectedGraphCoordinate coordinate;
    private Time time = new DiscreteTime();
    private Random random = new Random();

    public ChildWithMatches(UndirectedGraphCoordinate coordinate) {
        this.coordinate = coordinate;
    }

    @Override
    public void init() {
        registerAwake(new DiscreteTime(0));
    }

    @Override
    public void awake() {
        HashSet<UndirectedGraphCoordinate> neighborRooms = this.coordinate.getNeighbors();
        neighborRooms.add(coordinate);
        List<UndirectedGraphCoordinate> nList= new ArrayList<>(neighborRooms);
        coordinate = nList.get(random.nextInt(nList.size()));
        System.out.println("Dite jde do: " + coordinate.getName());

        if(random.nextInt(3) == 1) {
            publish(new Data(UndirectedGraphCoordinate.class, coordinate), "ignite-fire");
            System.out.println("Dite zapaluje: " + coordinate.getName());
        } else {
            time = eventBus.getTime();
            time.setValue(time.getMillis() + 5000L);
            registerAwake(time);
        }
    }

    @Override
    public void receive(Data message, String topic) {
    }
}
