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

package io.patriot_framework.generator.dataFeed;

import io.patriot_framework.generator.Data;
import net.objecthunter.exp4j.Expression;

import java.util.Set;

/**
 * Feed that uses Expression library to calculate data streams.
 */
public class ExpressionDataFeed implements DataFeed {

    private String label;

    private Expression expression;

    private String variable;

    private double previousValue;

    public ExpressionDataFeed(Expression expression) {
        this.expression = expression;
        validate();
    }

    private void validate() {
        if(expression.validate().isValid()) {
            Set<String> variables = expression.getVariableNames();
            if(variables.size() != 1)
                throw new IllegalArgumentException("Expression contains more than one variable");

            variable = (String) variables.toArray()[0];
        } else {
            throw new IllegalArgumentException("Expression is not valid");
        }
    }

    @Override
    public Data getNextValue(Object... params) {
        double time = (double) params[0];
        expression.setVariable(variable, time);
        double result = expression.evaluate();
        previousValue = result;

        return new Data(Double.class, result);
    }

    @Override
    public Data getPreviousValue() {
        return new Data(Double.class, previousValue);
    }

    @Override
    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String getLabel() {
        return label;
    }
}
