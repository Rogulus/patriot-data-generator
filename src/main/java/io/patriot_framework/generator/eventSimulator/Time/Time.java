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

public interface Time extends Comparable<Time>, Cloneable{

    Time setMillis(long millis);

    static Time plus(Time t1, Time t2) {
        Time newTime = (Time)t1.clone();
        return newTime.plus(t2);
    }

    static Time minus(Time t1, Time t2) {
        Time newTime = (Time)t1.clone();
        return newTime.minus(t2);
    }

    static int compare(Time o, Time o2) {
        return Long.compare(o.getMillis(), o2.getMillis());
    }

    Time plus(Time other);
    Time minus(Time other);
    long getMillis();
    int compareTo(Time other);
    Object clone();
}
