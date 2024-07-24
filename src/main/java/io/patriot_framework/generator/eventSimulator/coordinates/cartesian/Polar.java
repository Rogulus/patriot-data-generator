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
import java.util.List;

import static java.lang.Math.PI;
import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.signum;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;


public class Polar extends CartesianCoordinateBase{
    private double radius;
    private double angle;

    private void setAngle(double angle) {
        this.angle = angle % (2*PI);
    }

    public Polar(double radius, double angle) {
        this.radius = radius;
        setAngle(angle);
    }

    public Polar(List<Double> coordinateValues) {
        setCoordinateValues(coordinateValues);
    }

    public void rotate(double angle) {
        setAngle(this.angle + angle);
    }

    public void shiftRadius(double delta) {
        double newRadius = radius + delta;
        if (newRadius < 0) {
            radius = -1.0 * radius;
            setAngle(angle + PI);
        }
    }

    @Override
    public List<Double> getCoordinateValues() {
        var values = new ArrayList<Double>(2);
        values.add(radius * cos(angle));
        values.add(radius * sin(angle));
        return values;
    }

    @Override
    public void setCoordinateValues(List<Double> coordinateValues) {
        if(coordinateValues.size() != 2) {
            throw new IllegalArgumentException("Polar coordinates can be created only from 2D Cartesian coordinates");
        }
        var iterator = coordinateValues.iterator();
        Double x = iterator.next();
        Double y = iterator.next();

        radius = sqrt(x*x + y*y);
        if(radius == 0) {  // we cannot divide by 0
            angle = 0.0;
            return;
        }
        setAngle(signum(y) * acos(x/radius));
    }
}
