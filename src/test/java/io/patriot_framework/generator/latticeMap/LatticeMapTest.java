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

package io.patriot_framework.generator.latticeMap;

import io.patriot_framework.generator.coordinates.CartesianCoordinate;
import io.patriot_framework.generator.latticeMapFederation.MapFederate.LatticeMapFederate;
import io.patriot_framework.generator.latticeMapFederation.ParticipantFederate.ParticipantFederate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class LatticeMapTest {
    String federationName = "Lattice federation";
    @BeforeEach
    void setup () {

    }

    @Test
    public void poc(){
        LatticeMapFederate mapFed = new LatticeMapFederate("map", federationName);
        Thread thread = new Thread(mapFed);

        List<Integer> a = new ArrayList<>(3);
        a.add(1);
        a.add(4);
        a.add(6);


        CartesianCoordinate c1 = new CartesianCoordinate(3, a);
        CartesianCoordinate c2 = new CartesianCoordinate(3, a);
        System.out.println( c1.equals(c2));

        try {
            thread.start();
            Thread.sleep(2050);

            ParticipantFederate partFed = new ParticipantFederate("participant", federationName);
            Thread thread2 = new Thread(partFed);

            thread2.start();
            thread.join();
            thread2.join();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

}
