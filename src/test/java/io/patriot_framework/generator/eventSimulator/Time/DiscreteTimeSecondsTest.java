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

package io.patriot_framework.generator.eventSimulator.Time;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class DiscreteTimeSecondsTest {

    private DiscreteTimeSeconds discreteTime;

    @BeforeEach
    public void setUp() {
        discreteTime = new DiscreteTimeSeconds();
    }

    @Test
    public void test() {
        var minutes = new DiscreteTimeMinutes(1L);
        var seconds = new DiscreteTimeSeconds(20L);

        assertEquals(70000, seconds.plus(minutes).minus(new DiscreteTimeSeconds(10)).getMillis());
        assertEquals("Discrete time: 2 min", minutes.setMillis(120000).toString());
    }

    @Test
    public void testDefaultConstructor() {
        assertEquals(0, discreteTime.timeInUnits);
    }

    @Test
    public void testTimeInUnitsConstructor() {
        DiscreteTimeSeconds time = new DiscreteTimeSeconds(10);
        assertEquals(10, time.timeInUnits);
    }

    @Test
    public void testTimeConstructor() {
        DiscreteTimeSeconds time = new DiscreteTimeSeconds(new DiscreteTimeSeconds(10));
        assertEquals(10, time.timeInUnits);
    }

    @Test
    public void testSetValue() {
        discreteTime.setMillis(5000);
        assertEquals(5, discreteTime.timeInUnits);
    }

    @Test
    public void testGetMillis() {
        discreteTime.timeInUnits = 10;
        assertEquals(10000, discreteTime.getMillis());
    }

    @Test
    public void testPlus() {
        DiscreteTimeSeconds time1 = new DiscreteTimeSeconds(10);
        DiscreteTimeSeconds time2 = new DiscreteTimeSeconds(5);
        AbstractDiscreteTime result = DiscreteTimeSeconds.plus(time1, time2);
        assertEquals(15, result.timeInUnits);
    }

    @Test
    public void testPlusInstanceMethod() {
        DiscreteTimeSeconds time1 = new DiscreteTimeSeconds(10);
        time1.plus(new DiscreteTimeSeconds(5));
        assertEquals(15, time1.timeInUnits);
    }

    @Test
    public void testMinus() {
        DiscreteTimeSeconds time1 = new DiscreteTimeSeconds(10);
        DiscreteTimeSeconds time2 = new DiscreteTimeSeconds(5);
        AbstractDiscreteTime result = DiscreteTimeSeconds.minus(time1, time2);
        assertEquals(5000, result.getMillis());
    }

    @Test
    public void testMinusInstanceMethod() {
        DiscreteTimeSeconds time1 = new DiscreteTimeSeconds(10);
        time1.minus(new DiscreteTimeSeconds(5));
        assertEquals(5, time1.timeInUnits);
    }

    @Test
    public void testEquals() {
        DiscreteTimeSeconds time1 = new DiscreteTimeSeconds(10);
        DiscreteTimeSeconds time2 = new DiscreteTimeSeconds(10);
        DiscreteTimeSeconds time3 = new DiscreteTimeSeconds(5);
        assertTrue(time1.equals(time2));
        assertFalse(time1.equals(time3));
    }

    @Test
    public void testHashCode() {
        DiscreteTimeSeconds time1 = new DiscreteTimeSeconds(10);
        DiscreteTimeSeconds time2 = new DiscreteTimeSeconds(10);
        assertEquals(time1.hashCode(), time2.hashCode());
    }

    @Test
    public void testCompareTo() {
        DiscreteTimeSeconds time1 = new DiscreteTimeSeconds(10);
        DiscreteTimeSeconds time2 = new DiscreteTimeSeconds(5);
        DiscreteTimeSeconds time3 = new DiscreteTimeSeconds(10);
        assertTrue(time1.compareTo(time2) > 0);
        assertTrue(time2.compareTo(time1) < 0);
        assertTrue(time1.compareTo(time3) == 0);
    }

    @Test
    public void testClone() {
        DiscreteTimeSeconds time1 = new DiscreteTimeSeconds(10);
        AbstractDiscreteTime time2 = time1.clone();
        assertEquals(time1, time2);
        assertNotSame(time1, time2);
    }

    @Test
    public void testToString() {
        DiscreteTimeSeconds time = new DiscreteTimeSeconds(10);
        assertEquals("Discrete time: 10 sec", time.toString());
    }
}