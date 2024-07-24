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

import io.patriot_framework.generator.eventSimulator.Time.DiscreteTimeSeconds;
import io.patriot_framework.generator.eventSimulator.Time.Time;
import io.patriot_framework.generator.eventSimulator.eventGenerator.eventBus.EventBus;
import io.patriot_framework.generator.eventSimulator.eventGenerator.eventBus.EventBusImpl;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Conductor implements Runnable{
    private EventBus eventBus;
    private Set<SimulationBase> simulations = new HashSet<>();
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final Object shutdownLock = new Object();
    private boolean running;

    public Conductor() { // todo pridat moznost dat jinou implementaci
        this.eventBus = new EventBusImpl(new DiscreteTimeSeconds(0)); // todo moza zacit od jineho casu?
    }

    public void addSimulation(SimulationBase simulation) {
        simulation.setEventBus(eventBus);
        simulation.init();
        simulations.add(simulation);
    }

    long startRealTime;

    public void run() {
        running = true;
        eventBus.run();
        running = false;
    }

    public void runFor(Time duration) {
        Time busTime = eventBus.getTime();
        eventBus.runUntil(busTime.plus(duration));
    }

    public void runUntil(Time endTime) {
        running = true;
        eventBus.runUntil(endTime);
        running = false;
    }

    public void runRealTimeFor(Time duration) {
        if(duration.getMillis() < 0) {
            //todo log
            return;
        }
        runRealTime();
        scheduler.schedule(this::pause, duration.getMillis(), TimeUnit.MILLISECONDS);
    }

    public void runRealTimeUntil(Time endTime) {
        Time busTime = eventBus.getTime();
        Time duration = endTime.minus(busTime);
        runRealTimeFor(duration);
    }


    public void runRealTime() {
        startRealTime = System.currentTimeMillis();
        long startSimulationTime = eventBus.getTime().getMillis();
        scheduler = Executors.newScheduledThreadPool(1);
        running = true;
        Runnable task = new Runnable() {

            public void run() {
                synchronized (shutdownLock) {
                    if( ! eventBus.tick()) {
                        pause();
                        return;
                    }
                }

                Time nextStepSimulationTime = eventBus.getNextStepTime();
                long nextStepRealTime = startRealTime + nextStepSimulationTime.getMillis() - startSimulationTime;
                long delay = nextStepRealTime - System.currentTimeMillis();  // real time interval (in milliseconds) between now and the time when next step should be executed

                if(delay < 0) {
                    delay = 0;
                    System.out.println("WARNING: simulation too slow"); // todo log
                }
                scheduler.schedule(this, delay, TimeUnit.MILLISECONDS);
            }
        };

        scheduler.schedule(task, 0, TimeUnit.MILLISECONDS); // start the event bus
    }

    public void pause() {
        System.out.println("ZAVOLAL SE STOP");
        synchronized (shutdownLock) {
            System.out.println("in stop");
            running = false;
            scheduler.shutdownNow();
            try {
                if(! scheduler.awaitTermination(1,  TimeUnit.SECONDS)) {
                    System.out.println("Log ze se nepodarilo sejmout thread");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean isRunning() {
        return running;
    }
}
