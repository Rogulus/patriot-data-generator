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

package io.patriot_framework.generator.device.passive.actuators.stateMachine;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class StateMachineTest {
    StateMachine stateMachine =  new StateMachine.Builder()
            .from("Off")
            .to("On", "start", "Starting", 500)
            .from("On")
            .to("Off", "stop", "Stopping", 500)
            .build();

    @Test
    public void stateHistoryTest() throws InterruptedException {
        assertEquals("[Off]", stateMachine.getStateHistory().toString());
        stateMachine.transition("start");
        assertEquals("[Off, Starting]", stateMachine.getStateHistory().toString());
        stateMachine.transition("stop");
        assertEquals("[Off, Starting]", stateMachine.getStateHistory().toString());
        Thread.sleep(600);
        assertEquals("[Off, Starting, On]", stateMachine.getStateHistory().toString());
        stateMachine.transition("stop");
        assertEquals("[Off, Starting, On, Stopping]", stateMachine.getStateHistory().toString());
        stateMachine.clearStateHistory();
        assertEquals("[Stopping]", stateMachine.getStateHistory().toString());
        Thread.sleep(600);
        stateMachine.transition("start");
        Thread.sleep(600);
        stateMachine.transition("stop");
        Thread.sleep(600);
        assertEquals("[Stopping, Off, Starting, On, Stopping, Off]", stateMachine.getStateHistory().toString());

    }
}