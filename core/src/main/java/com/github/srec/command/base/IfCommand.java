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

package com.github.srec.command.base;

import com.github.srec.Location;
import com.github.srec.command.ExecutionContext;
import com.github.srec.command.exception.CommandExecutionException;
import com.github.srec.command.value.BooleanValue;
import com.github.srec.command.value.NilValue;
import com.github.srec.command.value.Value;
import com.github.srec.play.PlayerException;

import java.util.ArrayList;
import java.util.List;

/**
 * An if statement.
 *
 * @author Victor Tatai
 */
public class IfCommand extends AbstractBlockCommand {
    private ValueCommand condition;
    private List<ElsifCommand> elsifs = new ArrayList<ElsifCommand>();
    private ElseCommand elseCommand;

    public IfCommand(ValueCommand condition) {
        super("if");
        this.condition = condition;
    }

    public IfCommand(Location location, ValueCommand condition) {
        super("if", location);
        this.condition = condition;
    }

    @Override
    public CommandFlow run(ExecutionContext context) throws CommandExecutionException {
        boolean cond = evaluateCondition(context);
        if (cond) {
            for (Command command : commands) {
                CommandFlow flow = command.run(context);
                if (flow == CommandFlow.NEXT) {}
                else if (flow == Command.CommandFlow.BREAK
                        || flow == Command.CommandFlow.EXIT
                        || flow == Command.CommandFlow.RETURN) return flow;
                else throw new PlayerException("Flow management from command not supported");
            }
        } else if (!runElsif(context) && elseCommand != null) {
            elseCommand.run(context);
        }
        return CommandFlow.NEXT;
    }

    private boolean runElsif(ExecutionContext context) {
        for (ElsifCommand elsif : elsifs) {
            if (elsif.evaluateCondition(context)) {
                elsif.run(context);
                return true;
            }
        }
        return false;
    }

    /**
     * Evaluates the elsif condition.
     *
     * @param context The EC
     * @return true if this block should run, false otherwise
     */
    public boolean evaluateCondition(ExecutionContext context) {
        Value v = condition.getValue(context);
        return !(v instanceof NilValue || (v instanceof BooleanValue && !((BooleanValue) v).get()));
    }

    public void addElsif(ElsifCommand elsif) {
        elsifs.add(elsif);
    }

    public List<ElsifCommand> getElsifs() {
        return elsifs;
    }

    public ElseCommand getElseCommand() {
        return elseCommand;
    }

    public void setElseCommand(ElseCommand elseCommand) {
        this.elseCommand = elseCommand;
    }

    @Override
    public String toString() {
        return "if " + condition;
    }
}
