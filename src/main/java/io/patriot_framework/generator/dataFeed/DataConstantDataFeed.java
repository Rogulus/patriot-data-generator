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

package io.patriot_framework.generator.dataFeed;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.patriot_framework.generator.Data;

import java.util.Objects;

public class DataConstantDataFeed implements DataFeed {
    private String label;

    @JsonProperty
    private Data constant;

    @JsonCreator
    public DataConstantDataFeed(@JsonProperty("data") Data constant) {
        this.constant = constant;
    }

    @Override
    public Data getNextValue(Object... params) {
        return new Data(constant);
    }

    @Override
    public Data getPreviousValue() {
        return new Data(constant);
    }

    @Override
    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataConstantDataFeed that)) return false;
        return constant.equals(that.constant) && label.equals(that.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label, constant);
    }
}
