package com.github.srec.command;

import java.util.ArrayList;
import java.util.List;

/**
 * A method defined by a script.
 *
 * @author Victor Tatai
 */
public class MethodScriptCommand extends MethodCommand {
    protected List<Command> commands = new ArrayList<Command>();

    public MethodScriptCommand(String name, String... parameters) {
        super(name, parameters);
    }

    @Override
    public void callMethod(ExecutionContext context, String... params) {
        // TODO
        throw new UnsupportedCommandException("Def command unsupported.");
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
}
