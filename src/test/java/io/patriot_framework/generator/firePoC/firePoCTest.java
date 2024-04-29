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

package io.patriot_framework.generator.firePoC;

import io.patriot_framework.generator.hlaPoC.pocFederation.FireFederate.FireFederate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



public class firePoCTest {

    @BeforeEach
    void setup () {

    }

    @Test
    public void poc(){
        FireFederate fireFederate = new FireFederate("fire federate");
        FireFederate fireFederate2 = new FireFederate("fire federate2");

        Thread thread = new Thread(fireFederate);
        Thread thread2 = new Thread(fireFederate2);


        try {
            thread.start();
            Thread.sleep(1000);
            thread2.start();
            thread.join();
            thread2.join();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

}
