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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import static java.lang.Math.addExact;


/**
 * uni of this
 */
public abstract class AbstractContinuousTime implements Time, Cloneable {

    public double timeInUnits;
    private long millisInUnit;
    public static final Logger LOGGER = LoggerFactory.getLogger(AbstractContinuousTime.class);

    protected abstract long millisecondsInUnit();
    protected abstract String unitName();

    public AbstractContinuousTime() {
        millisInUnit = millisecondsInUnit();
        if(millisInUnit <= 0) {
            throw new IllegalArgumentException("Unit of time must last at least 1 millisecond");
        }
    }

    public AbstractContinuousTime(double timeInUnits) {
        this();
        this.timeInUnits = timeInUnits;
    }

    public AbstractContinuousTime(Time time) {
        this();
        this.timeInUnits = time.getMillis()/((double)(millisecondsInUnit())); // todo toto chce zkust
    }


    public AbstractContinuousTime setTimeInUnits(Double timeInUnits) {
        this.timeInUnits = timeInUnits;
        return this;
    }

    @Override
    public AbstractContinuousTime setMillis(long millis) {
        this.timeInUnits = millis/((double)millisInUnit);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractContinuousTime that = (AbstractContinuousTime) o;
        return Objects.equals(getMillis(), that.getMillis());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMillis());
    }

    public static int compare(AbstractContinuousTime o, AbstractContinuousTime o2) {
        return Long.compare(o.getMillis(), o2.getMillis());
    }

    @Override
    public int compareTo(Time other) {
        return Long.compare(this.getMillis(), other.getMillis());
    }

    @Override
    public AbstractContinuousTime clone() {
        try {
            return (AbstractContinuousTime) super.clone();
        } catch (CloneNotSupportedException exception) {
            LOGGER.warn(exception.toString());
            return null;
        }
    }

    public static AbstractContinuousTime plus(AbstractContinuousTime t1, Time t2) {
        long millis = addExact(t1.getMillis(), t2.getMillis());
        return t1.clone().setMillis(millis);
    }

    public AbstractContinuousTime plus(double timeInUnits) {
        this.timeInUnits = this.timeInUnits + timeInUnits;
        return this;
    }

    public AbstractContinuousTime plus(Time other) {
        return plus((other.getMillis()/((double)millisInUnit)));
    }


    public static AbstractContinuousTime minus(AbstractContinuousTime t1, Time t2) {
        long millis = t1.getMillis() - t2.getMillis();
        if(millis < 0) {
            LOGGER.warn("Creating time with negative value.");
        }
        return t1.clone().setMillis(millis);
    }

    public AbstractContinuousTime minus(double timeInUnits) {
        this.timeInUnits = this.timeInUnits - timeInUnits;
        if(this.timeInUnits < 0) {
            LOGGER.warn("Creating time with negative value.");
        }
        return this;
    }

    public AbstractContinuousTime minus(Time other) {
        return minus(other.getMillis()/((double)millisInUnit));
    }

    public long getMillis() {
        return (long)timeInUnits * millisInUnit;
    }

    @Override
    public String toString() {
        return "Continuous time: " + timeInUnits + " " + unitName();
    }
}
