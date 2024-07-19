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

package io.patriot_framework.generator.eventGenerator;

public interface Time extends Comparable<Time>{

    void setValue(int time);// todo fix binding to one type
    Integer getValue();

    public boolean equals(Object o);

    int hashCode();

    static int compare(Time o, Time o2) {
        return Integer.compare(o.getValue(), o2.getValue());
    }

    public int compareTo(Time other);
    Object clone();

    public Time plus(Time other);
}
