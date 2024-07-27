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

package io.patriot_framework.generator.eventSimulator.coordinates.graph;


import io.patriot_framework.generator.Data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

// todo udelat vahovy graf a toto bude specialni pripad


// todo pridat vertex name and data
public class UndirectedGraphCoordinate implements GraphCoordinate<UndirectedGraphCoordinate> {
    private GraphSpace<UndirectedGraphCoordinate> space;
    private String coorinateName;
    private HashMap<String, Data> data;


    public UndirectedGraphCoordinate(UndirectedGraphSpace graphSpace, String coordinateName) {
        data = new HashMap<>();
        this.space = graphSpace;
        this.coorinateName = coordinateName;
    }


    @Override
    public Double distance(UndirectedGraphCoordinate other) {
        if (! space.equals(other.space)) {
            throw new IllegalArgumentException("TODO"); // todo
        }
        return space.distance(coorinateName, other.coorinateName);
    }


    public UndirectedGraphCoordinate getCoordinate(String coordinateName) {
        return space.getCoordinate(coordinateName);
    }


    public HashSet<UndirectedGraphCoordinate> getNeighbors() {
        return space.getNeighbors(coorinateName);
    }

    public Data getData(String dataName) { // todo osetrit neplatne vstupy
        return data.get(dataName);
    }

    public void setData(String dataName, Data data) {
        this.data.put(dataName, data);
    }

    public void deleteData(String dataName) {
        data.remove(dataName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UndirectedGraphCoordinate)) return false;
        UndirectedGraphCoordinate that = (UndirectedGraphCoordinate) o;
        return space.equals(that.space) && coorinateName.equals(that.coorinateName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(space, coorinateName);
    }

    @Override
    public String toString() {

        return "Coordinate: " + coorinateName + "\n" + mapToString(data);
    }

    public String getName() {
        return coorinateName;
    }

    public static String mapToString(Map<String, Data> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Data> entry : map.entrySet()) {
            sb.append(entry.getKey())
                    .append(": ")
                    .append(entry.getValue())
                    .append(System.lineSeparator());
        }
        return sb.toString();
    }
}

