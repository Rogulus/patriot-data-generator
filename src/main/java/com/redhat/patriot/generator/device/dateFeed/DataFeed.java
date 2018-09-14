/*
 * Copyright 2018 Patriot project
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

package com.redhat.patriot.generator.device.dateFeed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jsmolar on 9/9/18.
 */
public abstract class DataFeed {

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

    abstract public double getValue(double time);

}
