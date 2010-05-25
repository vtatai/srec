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

import com.github.srec.Utils;
import com.github.srec.command.value.Value;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A method defined by a script.
 *
 * @author Victor Tatai
 */
public class MethodScriptCommand extends MethodCommand {
    private static final Logger logger = Logger.getLogger(MethodScriptCommand.class);
    protected List<Command> commands = new ArrayList<Command>();
    private File fileRead;

    public MethodScriptCommand(String name, File fileRead, String... parameters) {
        super(name, parameters);
        this.fileRead = fileRead;
    }

    @Override
    public Value internalCallMethod(ExecutionContext context, Value... params) {
        logger.debug("Call method " + name + "(" + Utils.asString(params) + ")");
        context.getPlayer().play(new NestedExecutionContext(context, context.getFile()), commands);
        return null;
    }

    /**
     * Adds a command inside this one.
     *
     * @param command The command to add
     */
    public void add(Command command) {
        commands.add(command);
    }

    public List<Command> getCommands() {
        return commands;
    }


    public File getFileRead() {
        return fileRead;
    }

    public void setFileRead(File fileRead) {
        this.fileRead = fileRead;
    }
}
