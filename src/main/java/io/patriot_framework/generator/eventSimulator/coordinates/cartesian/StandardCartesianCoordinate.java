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

package io.patriot_framework.generator.eventSimulator.coordinates.cartesian;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class StandardCartesianCoordinate extends CartesianCoordinateBase {
    private int dimensions;
    private ArrayList<Double> coordinates;

    private boolean isValidDimension(int dimension) {
        return dimension > 0 && dimension <= dimensions;
    }

    public StandardCartesianCoordinate(int dimensions) {
        if(dimensions < 1) {
            throw new IllegalArgumentException("Cartesian coordinates must have at least one dimension");
        }
        this.dimensions = dimensions;
        this.coordinates = new ArrayList<Double>(dimensions);
        for (int i = 0; i < dimensions; i++) {
            this.coordinates.add(0.0);
        }
    }

    public StandardCartesianCoordinate(List<Double> coordinates) {
        this(coordinates.size());
        Collections.copy(this.coordinates, coordinates);
    }

    public StandardCartesianCoordinate(Double ... coordinates) {
        this.coordinates = new ArrayList<>();
        Collections.addAll(this.coordinates, coordinates);
        if(this.coordinates.isEmpty()) {
            throw new IllegalArgumentException("Cartesian coordinates must have at least one dimension");
        }
        dimensions = this.coordinates.size();
    }

    public Double getCoordinate(int dimension) {
        if (!isValidDimension(dimension)) {
            return null;
        }
        return coordinates.get(dimension);
    }
    
    
    /**
     * Translates the coordinate in a specific dimension by a given distance.
     *
     * @param distance   The distance to translate the coordinate by
     * @param dimension  The dimension of the coordinate to translate
     * @return true if the translation was successful, false if the dimension is invalid
     */
    public boolean translateCoordinate(Double distance, int dimension) {
        if (!isValidDimension(dimension)) {
            return false;
        }
        coordinates.set(dimension, coordinates.get(dimension) + distance);
        return true;
    }

    public StandardCartesianCoordinate plus(CartesianCoordinate other) {
        if(! isValidDimension(other.getDimension())) {
            return null;
        }

        var myIt = coordinates.iterator();
        var otherIt = other.getCoordinateValues().iterator();
        for (var idx = 0; idx < dimensions; idx++){
            coordinates.set(idx,  otherIt.next() + myIt.next());
        }
        return this;
    }

    public StandardCartesianCoordinate multiply(double multiplier) {
        var myIt = coordinates.iterator();
        for (var idx = 0; idx < dimensions; idx++){
            coordinates.set(idx, myIt.next() * multiplier);
        }
        return this;
    }

    public StandardCartesianCoordinate minus(CartesianCoordinate other) {
        return multiply(-1).plus(other).multiply(-1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StandardCartesianCoordinate that = (StandardCartesianCoordinate) o;
        return getCoordinateValues().equals(that.getCoordinateValues());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCoordinateValues());
    }

    @Override
    public List<Double> getCoordinateValues() {
        return new ArrayList<>(coordinates);
    }

    @Override
    public void setCoordinateValues(List<Double> coordinateValues) {
        Collections.copy(this.coordinates, coordinates);
    }
}
