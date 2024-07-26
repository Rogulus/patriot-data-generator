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

import io.patriot_framework.generator.eventSimulator.Time.Time;

public interface EventBus extends EventDistributorService, Runnable{
    /**
     * Returns current time on the bus.
     * @return current time on the bus, if the bus is paused, it returns last processed time
     */
    Time getTime();

    /**
     * Processes all the events in the next smallest time in which are planned some events.
     * @return true if EventBus is able to do next step (some events are planned in the future)
     */
    boolean tick();

    /**
     * Process all events from the start time, until some events are planned.
     */
    void run();


    /**
     * Process all events which are planned before or on the given time.
     * @param until time until which all events should be processed
     * @return true if the simulation can continue
     */
    public boolean runUntil(Time until);

    /**
     * Returns time of the next step
     * @return time of the next step, null if there are no other events to process
     */
    public Time getNextStepTime();

}
