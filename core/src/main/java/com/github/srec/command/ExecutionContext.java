package com.github.srec.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a script execution context, containing commands to be executed and a very simple symbol table which
 * contains method definitions.
 * 
 * @author Victor Tatai
 */
public class ExecutionContext {
    private List<Command> commands = new ArrayList<Command>();
    private Map<String, MethodCommand> symbols = new HashMap<String, MethodCommand>();

    public MethodCommand getSymbol(String name) {
        return symbols.get(name);
    }
    
    public void addCommand(Command command) {
        commands.add(command);
    }

    public void addMethod(MethodCommand cmd) {
        symbols.put(cmd.getName(), cmd);
    }

    public List<Command> getCommands() {
        return commands;
    }

    public Map<String, MethodCommand> getSymbols() {
        return symbols;
    }
}
