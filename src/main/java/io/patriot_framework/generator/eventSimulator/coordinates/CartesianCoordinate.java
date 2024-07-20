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

package io.patriot_framework.generator.eventSimulator.coordinates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CartesianCoordinate implements Coordinate<CartesianCoordinate> {
    private int dimensions;
    private List<Integer> coordinates;

    private int x;

    public CartesianCoordinate(int dimensions) {
        if(dimensions < 1) {
            throw new IllegalArgumentException("Cartesian coordinates must have at least dimension 1");
        }
        this.dimensions = dimensions;
        this.coordinates = new ArrayList<Integer>(dimensions);
        for (int i = 0; i < dimensions; i++) {
            this.coordinates.add(0);
        }
    }

    public CartesianCoordinate(int dims, List<Integer> coordinates) {
        this(dims);
        Collections.copy(this.coordinates, coordinates);
    }

    public double distance(CartesianCoordinate  other) {
        if (this.dimensions != other.dimensions) {
            throw new IllegalArgumentException("Both coordinates must be in the same dimension.");
        }

        double sumOfSquares = 0.0;
        for (int i = 0; i < this.dimensions; i++) {
            int difference = this.coordinates.get(i) - other.coordinates.get(i);
            sumOfSquares += difference * difference;
        }

        return Math.sqrt(sumOfSquares);
    }

    public List<Integer> getCoordinates() {
        return new ArrayList<Integer>(coordinates);
    }

    public Integer getCoordinate(int dimension) {
        return coordinates.get(dimension);
    }

    public void shiftCoordinate(int shift, int dimension) {

        coordinates.set(dimension, coordinates.get(dimension) + shift);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartesianCoordinate that = (CartesianCoordinate) o;
        return getCoordinates().equals(that.getCoordinates());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCoordinates());
    }
}
