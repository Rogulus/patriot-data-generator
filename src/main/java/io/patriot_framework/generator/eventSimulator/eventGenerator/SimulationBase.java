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

package io.patriot_framework.generator.eventSimulator.eventGenerator;

import io.patriot_framework.generator.Data;
import io.patriot_framework.generator.eventSimulator.Time.Time;
import io.patriot_framework.generator.eventSimulator.eventGenerator.eventBus.EventBus;

public abstract class SimulationBase implements Simulation {
    protected EventBus eventBus;

    @Override
    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
        eventBus.registerSimulation(this);
    }

    protected void subscribe(String topic) {
        eventBus.subscribe(this, topic);
    }

    protected void registerAwake(Time time) {
        eventBus.registerAwake(this, time);
    }

    protected void registerRecurringAwake(Time interval) {
        eventBus.registerRecurringAwake(this, interval);
    }

    protected void publish(Data message, String topic) {
        eventBus.publish(message, topic);
    }

    protected void publishOnTime(Data message, String topic, Time time) {
        eventBus.publishOnTime(message, topic, time);
    }

    public abstract void init();
    public abstract void receive(Data message, String topic);
    public abstract void awake();

}
