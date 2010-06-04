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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Victor Tatai
 */
public abstract class AbstractBlockCommand extends BaseCommand implements BlockCommand {
    protected List<Command> commands = new ArrayList<Command>();

    protected AbstractBlockCommand(String name) {
        super(name);
    }

    protected AbstractBlockCommand(String name, Location location) {
        super(name, location);
    }

    @Override
    public void addCommand(Command c) {
        commands.add(c);
    }

    @Override
    public List<Command> getCommands() {
        return commands;
    }
}
