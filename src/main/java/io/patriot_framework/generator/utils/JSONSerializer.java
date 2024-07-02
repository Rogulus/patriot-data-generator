/*
 * Copyright 2021 Patriot project
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

package io.patriot_framework.generator.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.patriot_framework.generator.dataFeed.DataFeed;
import io.patriot_framework.generator.device.Device;
import io.patriot_framework.generator.device.active.Active;

import java.io.File;
import java.io.IOException;

/**
 * Wrapper class for Device and Active serialization to JSON
 *
 * @author <a href="mailto:jakub.smadis@gmail.com">Jakub Smadis</a>
 */
public class JSONSerializer {


    /**
     * Serializes the Device to the file using Jackson serialization.
     * The file will contain the JSON representation of the Device.
     *
     * @param device Device to serialize
     * @param file   File where to write the serialized device data
     */
    public static void serialize(Device device, File file) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(file, device);
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    /**
     * Serializes the device and returns the string containing serialized Device in JSON format.
     *
     * @param device Device to serialize
     * @return String containing JSON representing the serialized device
     */
    public static String serialize(Device device) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(device);
        } catch (JsonProcessingException e) {
            throw new SerializationException(e);
        }
    }

    /**
     * Serializes the Active (device) to the file.
     * The file will contain the JSON representation of the Active.
     *
     * @param active Active to serialize
     * @param file   file where to write the serialized Active data
     */
    public static void serialize(Active active, File file) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(file, active);
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    /**
     * Serializes the Active and returns the String representation of serialized device.
     *
     * @param active Active to serialize
     * @return String containing JSON representation of the Active (device)
     */
    public static String serialize(Active active) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(active);
        } catch (JsonProcessingException e) {
            throw new SerializationException(e);
        }
    }

    /**
     * Deserializes the file containing serialized Device to the Device object.
     *
     * @param file File containing serialized Device in the JSON format
     * @return Device object of given file
     */
    public static Device deserializeDevice(File file) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(file, Device.class);
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    /**
     * Deserializes the String containing serialized Device to the Device object.
     *
     * @param content String containing serialized Device in the JSON format
     * @return Device object of given String
     */
    public static Device deserializeDevice(String content) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(content, Device.class);
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    /**
     * Deserializes the file containing serialized Active to the Active object.
     *
     * @param file File containing serialized Active object in the JSON format
     * @return Active object of given file
     */
    public static Active deserializeActive(File file) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(file, Active.class);
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    /**
     * Deserializes the String containing serialized Active to the Active object.
     *
     * @param content String containing serialized Active in the JSON format
     * @return Active object of given String
     */
    public static Active deserializeActive(String content) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(content, Active.class);
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    /**
     * Serializes the DataFeed and returns the String representation of serialized data feed.
     *
     * @param dataFeed data feed to serialize
     * @return String containing JSON representation of the DataFeed
     */
    public static String serializeDataFeed(DataFeed dataFeed) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(dataFeed);
        } catch (JsonProcessingException e) {
            throw new SerializationException(e);
        }
    }

    /**
     * Serializes the DataFeed and returns the String representation of serialized data feed.
     *
     * @param dataFeed data feed to serialize
     * @param file   File where to write the serialized device data
     */
    public static void serializeDataFeed(DataFeed dataFeed, File file) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(file, dataFeed);
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    /**
     * Deserializes the file containing serialized data feed to the DataFeed object.
     *
     * @param file File containing serialized data feed in the JSON format
     * @return DataFeed object of given file
     */
    public static DataFeed deserializeDataFeed(File file) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(file, DataFeed.class);
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    /**
     * Deserializes string containing serialized data feed to the DataFeed object..
     *
     * @param content String containing serialized data feed in the JSON format
     * @return DataFeed object of given String
     */
    public static DataFeed deserializeDataFeed(String content) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(content, DataFeed.class);
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }
}
