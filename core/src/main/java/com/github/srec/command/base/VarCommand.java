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
 * Represents a variable declaration.
 *
 * @author Victor Tatai
 */
public class VarCommand extends BaseCommand implements CommandSymbol, ValueCommand {
    private Value value;

    public VarCommand(String name, Value value) {
        super(name);
        this.value = value;
    }

    public VarCommand(String name, Location location, Value value) {
        super(name, location);
        this.value = value;
    }

    @Override
    public CommandFlow run(ExecutionContext context) throws CommandExecutionException {
        return CommandFlow.NEXT;
    }

    @Override
    public Value getValue(ExecutionContext context) {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return getName() + " = " + value.toString();
    }
}
