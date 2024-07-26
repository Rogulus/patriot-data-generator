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
import io.patriot_framework.generator.eventSimulator.Time.ContinuousTimeSeconds;
import io.patriot_framework.generator.eventSimulator.Time.DiscreteTimeSeconds;
import io.patriot_framework.generator.eventSimulator.coordinates.cartesian.StandardCartesianCoordinate;
import io.patriot_framework.generator.eventSimulator.eventGenerator.conductor.Conductor;
import io.patriot_framework.generator.eventSimulator.eventGenerator.eventBus.EventBusClientBase;
import org.junit.jupiter.api.Test;

class LinearMotionTest { //todo deleteme

//    @BeforeAll
//    public void setup() throws PropertiesNotLoadedException {
//        vshp1IP = PatriotHub.getInstance().getApplication("smarthome1").getIPAddress();
//        vshpClient = new VirtualSmartHomePlusHttpClient(vshp1IP, 8080);
//
//        doorDTO1 = new DoorDTO();
//        doorDTO1.setLabel("garageDoor");
//
//        vshpClient.putDevice("door", doorDTO1);
//
//    }

    @Test
    public void test() {


        var conductor = new Conductor(new ContinuousTimeSeconds(0));

        var car = new LinearMotion(
                new StandardCartesianCoordinate(15.0),
                new StandardCartesianCoordinate(-50.0),
                new ContinuousTimeSeconds(0.5),
                "car1"
        );


        conductor.addSimulation(car);
        conductor.addSimulation(new DoorAdapter());
        conductor.runFor(new DiscreteTimeSeconds(10));


    }

    class DoorAdapter extends EventBusClientBase {

        @Override
        public void init() {
            subscribe("LinearMotionPosition:" + "car1"); //todo car parametr
        }

        @Override
        public void receive(Data message, String topic) {
            System.out.println("door adapter dostal recieve:");
            System.out.println(message.get(StandardCartesianCoordinate.class).getCoordinateValues());
        }

        @Override
        public void awake() {

        }
    }
}
