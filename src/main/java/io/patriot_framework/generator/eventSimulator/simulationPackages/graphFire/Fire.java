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

package io.patriot_framework.generator.eventSimulator.simulationPackages.graphFire;


import io.patriot_framework.generator.Data;
import io.patriot_framework.generator.dataFeed.NormalDistVariateDataFeed;
import io.patriot_framework.generator.eventSimulator.Time.DiscreteTimeSeconds;
import io.patriot_framework.generator.eventSimulator.Time.Time;
import io.patriot_framework.generator.eventSimulator.coordinates.UndirectedGraphCoordinate;
import io.patriot_framework.generator.eventSimulator.coordinates.UndirectedGraphSpace;
import io.patriot_framework.generator.eventSimulator.eventGenerator.eventBus.EventBusClientBase;

public class Fire extends EventBusClientBase {
    private Time time = new DiscreteTimeSeconds();
    private int ignitionTemp;
    private UndirectedGraphSpace space;

    public Fire(UndirectedGraphSpace space, Integer ignitionTemp) {
        this.space = space;
        this.ignitionTemp = ignitionTemp;

        NormalDistVariateDataFeed df = new NormalDistVariateDataFeed(100, 25);

        space.getAll().stream().forEach(coordinate -> {
            coordinate.setData("is-on-fire", new Data(Boolean.class, Boolean.FALSE));
//            coordinate.setData("fuel", new Data(Integer.class, Math.round(df.getNextValue().get(Double.class))));
            coordinate.setData("fuel", new Data(Integer.class, 200));
        });
    }

    public void init() {
        registerRecurringAwake(new DiscreteTimeSeconds(1L));
        subscribe("ignite-fire");
    }

    @Override
    public void awake() {

        space.getAll().forEach((coordinate) -> {
                    Data isOnFire = coordinate.getData("is-on-fire");
                    int fuel = coordinate.getData("fuel").get(Integer.class);
                    if (isOnFire.get(Boolean.class)) {
                        if(fuel >= 10) {
                            fuel -= 10;
                            int temp = coordinate.getData("temperature").get(Integer.class);
                            coordinate.getData("temperature").set(temp + 50, Integer.class);
                            coordinate.getData("fuel").set(fuel, Integer.class);
                        } else {
                            isOnFire.set(false, Boolean.class);
                        }
                    } else {
                        if (coordinate.getData("temperature").get(Integer.class) >= ignitionTemp) {
                            tryToIgnite(coordinate);
                        }
                    }
                }
        );
    }

    private void tryToIgnite(UndirectedGraphCoordinate coordinate) {
        int fuel = coordinate.getData("fuel").get(Integer.class);
        Data isOnFire = coordinate.getData("is-on-fire");
        if(fuel >= 10) {
            isOnFire.set(true, Boolean.class);
        }
    }

    @Override
    public void receive(Data message, String topic){
        UndirectedGraphCoordinate coordinate = message.get(UndirectedGraphCoordinate.class);
        tryToIgnite(coordinate);
    }
}
