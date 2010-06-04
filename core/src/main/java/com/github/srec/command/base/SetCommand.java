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
import com.github.srec.command.value.Value;

/**
 * A set statement.
 *
 * @author Victor Tatai
 */
public class SetCommand extends BaseCommand {
    private ExpressionCommand expression;
    private String varName;

    public SetCommand(Location location, String varName, ExpressionCommand expression) {
        super("set", location);
        this.expression = expression;
        this.varName = varName;
    }

    @Override
    public CommandFlow run(ExecutionContext context) throws CommandExecutionException {
        Value value = expression.getValue(context);
        if (context.findSymbol(varName) == null) {
            VarCommand var = new VarCommand(varName, location, value);
            context.addSymbol(var);
        } else {
            CommandSymbol s = context.findSymbol(varName);
            if (!(s instanceof VarCommand)) throw new CommandExecutionException("set statement should refer to a variable");
            ((VarCommand) s).setValue(value);
        }
        return CommandFlow.NEXT;
    }

    @Override
    public String toString() {
        return "set " + varName + " = " + expression;
    }
}