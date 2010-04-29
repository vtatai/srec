package com.github.srec.command;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines a method.
 * 
 * @author Victor Tatai
 */
public class DefCommand implements AggregateCommand {
    private String command;
    private List<String> parameters = new ArrayList<String>();
    private List<Command> commands = new ArrayList<Command>();

    public DefCommand(String command, List<String> parameters) {
        this.command = command;
        this.parameters = parameters;
    }

    public DefCommand(String command) {
        this.command = command;
    }

    @Override
    public void run() {
        // a def command by definition does not run
    }

    @Override
    public boolean add(Command command) {
        if (command instanceof EndCommand) return false;
        commands.add(command);
        return true;
    }

    public String getName() {
        return command;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public List<Command> getCommands() {
        return commands;
    }
}
