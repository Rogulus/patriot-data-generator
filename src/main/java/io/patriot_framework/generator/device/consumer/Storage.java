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

package io.patriot_framework.generator.device.consumer;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Storage for consumer.
 */
public class Storage {

    /**
     * Storaging data in ConcurrentLinkedQueue for thread-safety.
     */
    private final ConcurrentLinkedQueue<ConsumerData> dataList = new ConcurrentLinkedQueue<>();

    /**
     * Append input data to the ConcurrentLinkedQueue.
     *
     * @param message input
     */
    public void write(ConsumerData message) {
        if (message == null) {
            return;
        }
        dataList.offer(message);
    }

    /**
     * @return first element of the ConcurrentLinkedQueue
     */
    public ConsumerData get() {
        return dataList.poll();
    }

    /**
     * @param n number of elements to be returned
     * @return first n elements of the ConcurrentLinkedQueue
     */
    public ArrayList<ConsumerData> get(int n) {
        ArrayList<ConsumerData> res = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            ConsumerData element = get();
            if (element == null) {
                break;
            }
            res.add(element);
        }
        return res;
    }

    public int size() {
        return dataList.size();
    }

    /**
     * @return Storage content
     */
    public ArrayList<ConsumerData> getAll() {
        return get(size());
    }
}
