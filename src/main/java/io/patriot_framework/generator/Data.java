/*
 * Copyright 2019 Patriot project
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

package io.patriot_framework.generator;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Objects;

/**
 * Class provides simple tool to store data in different data types.
 * To give users freedom in designing Devices and DataFeeds library offers
 * Data structure to save and safety manipulate with generated data.
 */
//@JsonIgnoreType
@JsonSerialize
@JsonDeserialize
public class Data {

    /**
     * Class representing data
     */
    private Class<?> dataClazz;

    /**
     * Actual object containing data
     */
    private Object data;

    /**
     * Constructor
     * @param dataClazz class of stored object
     * @param data stored data
     */
    @JsonCreator
    public Data(@JsonProperty("dataClazz")Class<?> dataClazz,@JsonProperty("data") Object data) {
        this.dataClazz = dataClazz;
        this.data = data;
    }

    /**
     * Constructor
     * @param dataClazz class of data stored in object
     */
    public Data(@JsonProperty("dataClazz") Class<?> dataClazz) {
        this.dataClazz = dataClazz;
    }

    /**
     * Copy Constructor
     * @param data data to be copied
     */
    public Data(Data data) {
        this.dataClazz = data.dataClazz;
        this.data = data.data;
    }

    /**
     * Gets data saved within Data instance with desired type. If their type is not castable to provided Class,
     * method throws IllegalCastException.
     *
     * @param clazz class used to retype the object to
     * @param <T> type of class to be used
     * @return retyped stored value to new class
     */
    public <T> T get(Class<T> clazz) {
        if(dataClazz.equals(clazz)) {
            return clazz.cast(data);
        }
        return null;
    }

    /**
     * Setter
     *
     * @param obj object to be set
     * @param clazz class of object to be set
     * @param <T> type of class
     * @return retyped object, which was set to target class
     */
    public <T> T set(T obj, Class<T> clazz) {
        if(dataClazz.equals(clazz)) {
            data = obj;
            return clazz.cast(data);
        }

        return null;
    }

    @JsonProperty("dataClazz")
    public Class<?> getDataClazz() {
        return dataClazz;
    }

    @JsonProperty("data")
    public Object getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Data{" +
                "data type=" + dataClazz +
                ", data=" + data.toString() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Data data1)) return false;
        return Objects.equals(dataClazz, data1.dataClazz) && Objects.equals(data, data1.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataClazz, data);
    }
}
