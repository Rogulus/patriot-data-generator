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

package io.patriot_framework.generator.eventGenerator.eventBus;

import io.patriot_framework.generator.Data;
import io.patriot_framework.generator.eventGenerator.DiscreteTime;
import io.patriot_framework.generator.eventGenerator.Simulation;
import io.patriot_framework.generator.eventGenerator.Time;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;



public class EventBusImpl implements EventBus, Runnable{
    private Set<Simulation> simulations = new HashSet<>();
    private TreeMap<Time, TimeActions> actionsQueue = new TreeMap<>();
    private Hashtable<String, Set<Simulation>> topicSubscribers = new Hashtable<>();  // topic to subscribers
    private Time currentTime = new DiscreteTime();

    public EventBusImpl() {
        currentTime.setValue(0);
    }

    @Override
    public Time getTime() {
        return (DiscreteTime)currentTime.clone();
    }

    @Override
    public void registerSimulation(Simulation simulation) {  // todo k cemu je to dobre? proc nestaci subscribe
        simulations.add(simulation);
    }

    @Override
    public void unregister(Simulation simulation) {
        simulations.remove(simulation);
    }

    @Override
    public void registerAwake(Simulation simulation , Time time) {
        TimeActions actions = actionsQueue.computeIfAbsent(time, k -> new TimeActions());
        actions.awakeApplicants.add(simulation);
    }

    @Override
    public void registerRecurringAwake(Simulation simulation, Time interval) {
        TimeActions actions = actionsQueue.computeIfAbsent(currentTime.plus(interval), k -> new TimeActions());
        actions.recurringAwakeApplicants.add(new ImmutablePair<>(interval, simulation));
    }

    public void registerRecurringAwake(Simulation simulation, Time interval, Time startTime) {
        if(startTime.getMillis() <= currentTime.getMillis()) return;
        TimeActions actions = actionsQueue.computeIfAbsent(startTime, k -> new TimeActions());
        actions.recurringAwakeApplicants.add(new ImmutablePair<>(interval, simulation));
    }

    public void unregisterRecurringAwake(Simulation simulation) {
        for(var timeActions: actionsQueue.entrySet()) {
            timeActions.getValue().recurringAwakeApplicants.removeIf(
                    recurringAwakeApplicant -> recurringAwakeApplicant.getRight().equals(simulation)
            );
        }
    }

    @Override
    public void subscribe(Simulation simulation, String topic) {
        Set<Simulation> topicSubscribers = this.topicSubscribers.computeIfAbsent(topic, k -> new LinkedHashSet<>());  // todo equeals se nesmi menit po vlozeni
        topicSubscribers.add(simulation);
    }

    @Override
    public void publish(Data message, String topic) {
        publishOnTime(message, topic, currentTime);
    }

    @Override
    public void publishOnTime(Data message, String topic, Time time) {
        TimeActions actions = actionsQueue.computeIfAbsent(time, k -> new TimeActions());
        actions.events.add(new Event(message, topic));
    }


    private void awakeSimulations(Set<Simulation> simulations) {
        for (Simulation simulation: simulations) {
            simulation.awake();
        }
    }

    private void awakeRecurringSimulations(Set<ImmutablePair<Time, Simulation>> simulations) {
        for (var recurringAwake: simulations) {
            recurringAwake.getRight().awake();
            registerRecurringAwake(recurringAwake.getRight(), recurringAwake.getLeft());
        }
    }


    private void deliverEvents(Set<Event> events) {
        for(Event event: events) {
            if(topicSubscribers.get(event.topic) != null) {  // todo udelat to nejak hezci
                for (Simulation receiver : topicSubscribers.get(event.topic)) {
                    receiver.receive(event.message, event.topic);
                }
            }
        }
    }


    /**
     *
     */
    @Override
    public void run() {
        boolean canContinue = true;
        while(canContinue) {
            canContinue = tick();
        }
    }


    public boolean runUntil(Time until) {
        Map.Entry<Time, TimeActions> entry = actionsQueue.firstEntry();
        if(entry == null) {
            return false;
        }

        while(entry.getKey().getMillis() <= until.getMillis()) { // warning or equals matter!
            if(! tick()) {
                return false;
            }
            entry = actionsQueue.firstEntry();
        }
        return true;
    }


    public boolean tick() {
        Map.Entry<Time, TimeActions> entry = actionsQueue.firstEntry();
        if(entry == null) {
            return false;
        }
        currentTime = entry.getKey();
        processTimeStep(entry.getValue());
        actionsQueue.pollFirstEntry();
        return !actionsQueue.isEmpty();
    }


    public Time getNextStepTime() {
        if(actionsQueue.isEmpty()) {
            return null;
        }
        return actionsQueue.firstEntry().getKey();
    }


    private void processTimeStep(TimeActions timeActions) {
        awakeRecurringSimulations(timeActions.recurringAwakeApplicants);
        awakeSimulations(timeActions.awakeApplicants);
        deliverEvents(timeActions.events);
    }


    private class TimeActions {
        public Set<ImmutablePair<Time, Simulation>> recurringAwakeApplicants = new LinkedHashSet<>();
        public Set<Simulation> awakeApplicants = new LinkedHashSet<>();
        public Set<Event> events = new LinkedHashSet<>();
    }


    public class Event {
        public Data message;
        public String topic;
        public Event(Data message, String topic) {
            this.message = message;
            this.topic = topic;
        }
    }
}
