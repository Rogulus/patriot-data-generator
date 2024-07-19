/*
 * Copyright 2019 Patriot project
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

package io.patriot_framework.generator.device.impl.basicActuators;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.patriot_framework.generator.device.passive.actuators.AbstractActuator;
import io.patriot_framework.generator.device.passive.actuators.stateMachine.StateMachine;

/**
 * Implementation of Actuator which simulates linear movement like hydraulic press.
 */
public class LinearActuator extends AbstractActuator {

    @JsonCreator
    public LinearActuator(@JsonProperty("label") String label,
                          @JsonProperty("duration") double duration) {
        super(label);
        setStateMachine(
                new StateMachine.Builder()
                        .from("Retracted")
                            .to("Extended", "extend", "extending", duration)
                        .from("Extended")
                            .to("Retracted", "retract", "retracting", duration)
                        .build()
        );
    }
}
