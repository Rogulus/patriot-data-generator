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

public class SimulationImpl implements Simulation{
    EventBus eventBus;
    Time time = new DiscreteTime();
    Integer temperature = 0;


    @Override
    public void init(EventBus eventBus) {
        this.eventBus = eventBus;
        eventBus.registerSimulation(this);
        eventBus.subscribe(this, "temperature");
        time.setValue(3);
        eventBus.registerAwake(this, time);
        eventBus.publishOnTime(temperature, "temperature", time);
    }

    @Override
    public void awake() {
        time = eventBus.getTime();
        time.setValue(time.getValue() + 1);
        eventBus.registerAwake(this, time);
        System.out.println("probuzeni! cas: " + eventBus.getTime().getValue());
        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }

    @Override
    public void receive(Object message, String topic) {
        switch (topic) {
            case "temperature":
                 temperature = (Integer) message;
                 DiscreteTime newTime = (DiscreteTime)time.clone();
                 newTime.setValue(time.getValue() + 3);
                 eventBus.publishOnTime(temperature + 10, "temperature", newTime);
                 System.out.println("temperature: " + temperature);
        }
    }
}
