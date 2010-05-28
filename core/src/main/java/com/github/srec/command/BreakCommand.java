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

/**
 * A break statement.
 *
 * @author Victor Tatai
 */
public class BreakCommand extends AbstractBlockCommand {
    public BreakCommand() {
        super("break");
    }

    public BreakCommand(Location location) {
        super("break", location);
    }

    @Override
    public CommandFlow run(ExecutionContext context) throws CommandExecutionException {
        return CommandFlow.BREAK;
    }

    @Override
    public String toString() {
        return "break";
    }
}