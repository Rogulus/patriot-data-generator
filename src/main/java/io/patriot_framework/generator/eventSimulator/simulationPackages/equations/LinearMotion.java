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

package io.patriot_framework.generator.eventSimulator.simulationPackages.equations;

import io.patriot_framework.generator.Data;
import io.patriot_framework.generator.eventSimulator.Time.AbstractContinuousTime;
import io.patriot_framework.generator.eventSimulator.Time.ContinuousTimeSeconds;
import io.patriot_framework.generator.eventSimulator.coordinates.cartesian.CartesianCoordinate;
import io.patriot_framework.generator.eventSimulator.coordinates.cartesian.StandardCartesianCoordinate;
import io.patriot_framework.generator.eventSimulator.eventGenerator.eventBus.EventBusClientBase;

public class LinearMotion extends EventBusClientBase {
    private StandardCartesianCoordinate startPosition;
    private StandardCartesianCoordinate velocity;
    private final AbstractContinuousTime awakeInterval;
    private String id;

    public LinearMotion(StandardCartesianCoordinate velocity, StandardCartesianCoordinate startPosition, AbstractContinuousTime awakeInterval, String id) {
        this.startPosition = startPosition;
        this.velocity = velocity;
        this.awakeInterval = awakeInterval;
        this.id = id;
    }

    CartesianCoordinate getPositionInTime(AbstractContinuousTime time) {
        var currentPosition = new StandardCartesianCoordinate(startPosition.getCoordinateValues());
        var currentVelocity = new StandardCartesianCoordinate(velocity.getCoordinateValues());
        return currentPosition.plus(currentVelocity.multiply(time.getTimeInUnits()));
    }

    @Override
    public void init() {
        registerRecurringAwake(awakeInterval);
        subscribe("linearMotionStop:" + id);
    }

    @Override
    public void receive(Data message, String topic) {
        velocity.multiply(0);
        unregisterRecurringAwake();
    }

    @Override
    public void awake() {
        System.out.println("time in awake: " + eventBus.getTime());
        var time = new ContinuousTimeSeconds(eventBus.getTime());
        publish(new Data(StandardCartesianCoordinate.class, getPositionInTime(time)), "LinearMotionPosition:" + id);
    }
}
