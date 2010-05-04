package com.github.srec.command;

import com.github.srec.play.Player;

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
    /**
     * The player executing this context.
     */
    private Player player;
    /**
     * Path where this execution context was read. Might be null.
     */
    private String path;

    public ExecutionContext(String path) {
        this.path = path;
    }

    public ExecutionContext(Player player, String path) {
        this.player = player;
        this.path = path;
    }

    public MethodCommand getSymbol(String name) {
        return symbols.get(name);
    }
    
    public void addCommand(Command command) {
        commands.add(command);
    }

    public void addMethod(MethodCommand cmd) {
        symbols.put(cmd.getName(), cmd);
    }

    public void addAllSymbols(ExecutionContext context) {
        symbols.putAll(context.getSymbols());
    }

    public List<Command> getCommands() {
        return commands;
    }

    public Map<String, MethodCommand> getSymbols() {
        return symbols;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
