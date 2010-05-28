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
import com.github.srec.command.value.BooleanValue;
import com.github.srec.command.value.NilValue;
import com.github.srec.command.value.Value;
import com.github.srec.play.PlayerException;

/**
 * A while statement.
 *
 * @author Victor Tatai
 */
public class WhileCommand extends AbstractBlockCommand {
    private ValueCommand condition;

    public WhileCommand(ValueCommand condition) {
        super("while");
        this.condition = condition;
    }

    public WhileCommand(Location location, ValueCommand condition) {
        super("while", location);
        this.condition = condition;
    }

    @Override
    public CommandFlow run(ExecutionContext context) throws CommandExecutionException {
        boolean breakingFlow = false;
        while (!breakingFlow && checkCondition(context)) {
            for (Command command : commands) {
                CommandFlow flow = command.run(context);
                if (flow == CommandFlow.NEXT) {
                } else if (flow == CommandFlow.BREAK) {
                    breakingFlow = true;
                    break;
                } else if (flow == CommandFlow.EXIT || flow == CommandFlow.RETURN) {
                    return flow;
                } else throw new PlayerException("Flow management from command not supported");
            }
        }
        return CommandFlow.NEXT;
    }

    private boolean checkCondition(ExecutionContext context) {
        Value v = condition.getValue(context);
        return !(v instanceof NilValue) && (v instanceof BooleanValue && ((BooleanValue) v).get());
    }

    @Override
    public String toString() {
        return "while " + condition;
    }
}