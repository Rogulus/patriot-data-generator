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

import io.patriot_framework.generator.eventGenerator.eventBus.EventBus;
import io.patriot_framework.generator.eventGenerator.eventBus.EventBusImpl;

import java.util.HashSet;
import java.util.Set;

public class Conductor implements Runnable{
    private EventBus eventBus;
    private Set<SimulationBase> simulations = new HashSet<>();

    public Conductor() { // todo pridat moznost dat jinou implementaci
        this.eventBus = new EventBusImpl();
    }

    public void addSimulation(SimulationBase simulation) {
        simulation.setEventBus(eventBus);
        simulation.init();
        simulations.add(simulation);
    }

    // todo rizeni casu

    public void run() {
        eventBus.run();
    }
}
