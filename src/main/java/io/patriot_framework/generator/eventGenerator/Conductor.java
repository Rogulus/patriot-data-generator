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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Conductor implements Runnable{
    private EventBus eventBus;
    private Set<SimulationBase> simulations = new HashSet<>();
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public Conductor() { // todo pridat moznost dat jinou implementaci
        this.eventBus = new EventBusImpl();
    }

    public void addSimulation(SimulationBase simulation) {
        simulation.setEventBus(eventBus);
        simulation.init();
        simulations.add(simulation);
    }

    long startRealTime;

    public void run() {
        eventBus.run();
    }

    public void runFor(Time duration) {
        Time busTime = eventBus.getTime();
        eventBus.run(busTime.plus(duration));
    }

    public void runUntil(Time endTime) {
        eventBus.run(endTime);
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

        Runnable task = new Runnable() {

            public void run() {
                eventBus.run(eventBus.getNextStepTime());

                Time nextStepSimulationTime = eventBus.getNextStepTime(); // todo toto pocita ze se vraci vteriny, ale o todo kontorla jestli se cas posunul
                long nextStepRealTime = startRealTime + nextStepSimulationTime.getMillis() - startSimulationTime; // predpoklad ze simulace zacina od 0 - uz reseno ale funguje to
                long delay = nextStepRealTime - System.currentTimeMillis();

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
        eventBus.pause();
        while(! eventBus.readyToShutdown()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("CONDUCTOR VOLA SHUTDOWN");

        scheduler.shutdown();
        scheduler.shutdownNow();
        scheduler = Executors.newScheduledThreadPool(1);
        eventBus.unPause();
    }
}
