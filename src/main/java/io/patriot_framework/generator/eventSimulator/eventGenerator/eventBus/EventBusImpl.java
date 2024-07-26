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

package io.patriot_framework.generator.eventSimulator.eventGenerator.eventBus;

import io.patriot_framework.generator.Data;
import io.patriot_framework.generator.eventSimulator.Time.Time;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;



public class EventBusImpl implements EventBus {
    private TreeMap<Time, TimeActions> actionsQueue = new TreeMap<>();
    private Hashtable<String, Set<EventDistributorClient>> topicSubscribers = new Hashtable<>();  // topic to subscribers
    private Time currentTime;


    /**
     * Constructor of the class EventBusImpl
     * @param time denotes start time of processing the messages
     */
    public EventBusImpl(Time time) {
        currentTime = time;
    }


    /**
     *
     * @return
     */
    @Override
    public Time getTime() {
        return (Time)currentTime.clone();
    }


    @Override
    public void registerAwake(EventDistributorClient simulation , Time time) {
        TimeActions actions = actionsQueue.computeIfAbsent(time, k -> new TimeActions());
        actions.awakeApplicants.add(simulation);
    }


    @Override
    public void registerRecurringAwake(EventDistributorClient simulation, Time interval) {
        TimeActions actions = actionsQueue.computeIfAbsent(Time.plus(currentTime, interval), k -> new TimeActions());
        actions.recurringAwakeApplicants.add(new ImmutablePair<>(interval, simulation));
    }


    public void registerRecurringAwake(EventDistributorClient simulation, Time interval, Time startTime) {
        if(startTime.getMillis() <= currentTime.getMillis()) return;
        TimeActions actions = actionsQueue.computeIfAbsent(startTime, k -> new TimeActions());
        actions.recurringAwakeApplicants.add(new ImmutablePair<>(interval, simulation));
    }


    public void unregisterRecurringAwake(EventDistributorClient simulation) {
        for(var timeActions: actionsQueue.entrySet()) {
            timeActions.getValue().recurringAwakeApplicants.removeIf(
                    recurringAwakeApplicant -> recurringAwakeApplicant.getRight().equals(simulation)
            );
        }
    }


    @Override
    public void subscribe(EventDistributorClient simulation, String topic) {
        Set<EventDistributorClient> topicSubscribers = this.topicSubscribers.computeIfAbsent(topic, k -> new LinkedHashSet<>());  // todo equeals se nesmi menit po vlozeni
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


    private void awakeSimulations(Set<EventDistributorClient> simulations) {
        for (EventDistributorClient simulation: simulations) {
            simulation.awake();
        }
    }


    private void awakeRecurringSimulations(Set<ImmutablePair<Time, EventDistributorClient>> simulations) {
        for (var recurringAwake: simulations) {
            recurringAwake.getRight().awake();
            registerRecurringAwake(recurringAwake.getRight(), recurringAwake.getLeft());
        }
    }


    private void deliverEvents(Set<Event> events) {
        for(Event event: events) {
            if(topicSubscribers.get(event.topic) != null) {  // todo udelat to nejak hezci
                for (EventDistributorClient receiver : topicSubscribers.get(event.topic)) {
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
        public Set<ImmutablePair<Time, EventDistributorClient>> recurringAwakeApplicants = new LinkedHashSet<>();
        public Set<EventDistributorClient> awakeApplicants = new LinkedHashSet<>();
        public Set<Event> events = new LinkedHashSet<>();
    }


    /**
     * This class holds information about ...
     */
    public class Event {
        public Data message;
        public String topic;
        public Event(Data message, String topic) {
            this.message = message;
            this.topic = topic;
        }
    }
}
