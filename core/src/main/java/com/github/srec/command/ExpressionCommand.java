/*
 * Copyright 2010 Victor Tatai
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
 * the specific language governing permissions and limitations under the License.
 */

package com.github.srec.command;

import com.github.srec.Location;
import com.github.srec.command.exception.CommandExecutionException;
import com.github.srec.command.value.Value;

import static com.github.srec.util.Utils.groovyEvaluateConvert;

/**
 * @author Victor Tatai
 */
public class ExpressionCommand extends BaseCommand implements ValueCommand {
    private String expression;

    public ExpressionCommand(String expression) {
        super(expression);
        this.expression = expression;
    }

    public ExpressionCommand(String expression, Location location) {
        super(expression, location);
        this.expression = expression;
    }

    @Override
    public Value getValue(ExecutionContext context) {
        return groovyEvaluateConvert(context, expression);
    }

    @Override
    public CommandFlow run(ExecutionContext context) throws CommandExecutionException {
        return CommandFlow.NEXT;
    }

    @Override
    public String toString() {
        return expression;
    }
}
