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

public abstract class SimulationBase implements Simulation{
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

    protected void publish(Object message, String topic) {
        eventBus.publish(message, topic);
    }

    protected void publishOnTime(Object message, String topic, Time time) {
        eventBus.publishOnTime(message, topic, time);
    }

    protected abstract void init();
    public abstract void receive(Object message, String topic);
    public abstract void awake();

}
