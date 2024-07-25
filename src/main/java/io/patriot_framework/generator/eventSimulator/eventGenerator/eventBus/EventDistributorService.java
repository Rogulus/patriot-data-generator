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

public interface EventDistributorService {
    Time getTime();
    void registerAwake(EventDistributorClient simulation , Time time);
    void registerRecurringAwake(EventDistributorClient simulation, Time interval);
    void registerRecurringAwake(EventDistributorClient simulation, Time interval, Time startTime);

    // Deletes all recurring awakes
    void unregisterRecurringAwake(EventDistributorClient simulation);
    void publish(Data message, String topic);
    void publishOnTime(Data message, String topic, Time time);
    void subscribe(EventDistributorClient simulation, String topic);
}
