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

public abstract class CartesianCoordinateBase implements CartesianCoordinate{
    @Override
    public Double distance(CartesianCoordinate coordinate) {

        var myValues = this.getCoordinateValues();
        var otherValues = coordinate.getCoordinateValues();

        if (myValues.size() != otherValues.size()) {
            throw new IllegalArgumentException("Both coordinates must be in the same dimension.");
        }

        double sumOfSquares = 0.0;
        var otherIter = otherValues.iterator();
        for(var myCoordinateValue: myValues) {
            Double difference = myCoordinateValue - otherIter.next();
            sumOfSquares += difference * difference;
        }

        return Math.sqrt(sumOfSquares);
    }

    @Override
    public int getDimension() {
        return this.getCoordinateValues().size();
    }
}
