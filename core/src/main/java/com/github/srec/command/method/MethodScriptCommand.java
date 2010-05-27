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

package com.github.srec.command.method;

import com.github.srec.command.*;
import com.github.srec.command.value.Value;
import com.github.srec.util.Utils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A method defined by a script.
 *
 * @author Victor Tatai
 */
public class MethodScriptCommand extends MethodCommand implements BlockCommand {
    private static final Logger logger = Logger.getLogger(MethodScriptCommand.class);
    protected List<Command> commands = new ArrayList<Command>();
    private String fileRead;

    public MethodScriptCommand(String name, String fileRead, String... parameters) {
        super(name, parameters);
        this.fileRead = fileRead;
    }

    @Override
    public Value internalCallMethod(ExecutionContext context, Map<String, Value> params) {
        logger.debug("Call method " + name + "(" + Utils.asString(params) + ")");
        final NestedExecutionContext nestedContext = new NestedExecutionContext(context, context.getFile());
        for (Map.Entry<String, Value> entry : params.entrySet()) {
            nestedContext.addSymbol(new VarCommand(entry.getKey(), location, entry.getValue()));
        }
        nestedContext.getCommands().addAll(commands);
        context.getPlayer().play(nestedContext);
        return null;
    }

    /**
     * Adds a command inside this one.
     *
     * @param command The command to add
     */
    public void addCommand(Command command) {
        commands.add(command);
    }

    public List<Command> getCommands() {
        return commands;
    }


    public String getFileRead() {
        return fileRead;
    }

    public void setFileRead(String fileRead) {
        this.fileRead = fileRead;
    }
}
