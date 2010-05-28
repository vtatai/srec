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
import com.github.srec.command.value.NumberValue;
import com.github.srec.command.value.Value;

import java.math.BigDecimal;

/**
 * An inc statement.
 *
 * @author Victor Tatai
 */
public class IncCommand extends BaseCommand {
    private String varName;

    public IncCommand(Location location, String varName) {
        super("inc", location);
        this.varName = varName;
    }

    @Override
    public CommandFlow run(ExecutionContext context) throws CommandExecutionException {
        CommandSymbol s = context.findSymbol(varName);
        if (!(s instanceof VarCommand)) throw new CommandExecutionException("Cannot inc " + varName + " since it is not a variable");
        VarCommand var = (VarCommand) s;
        Value value = var.getValue(context);
        if (!(value instanceof NumberValue)) throw new CommandExecutionException("Cannot inc " + varName + " since it is not a number");
        NumberValue number = (NumberValue) value;
        number.set(number.get().add(BigDecimal.ONE));
        return CommandFlow.NEXT;
    }

    @Override
    public String toString() {
        return "inc " + varName;
    }
}