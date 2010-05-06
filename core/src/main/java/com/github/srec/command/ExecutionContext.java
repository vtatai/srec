package com.github.srec.command;

import com.github.srec.play.Player;

import java.io.File;
import java.io.IOException;
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
    private Map<String, CommandSymbol> symbols = new HashMap<String, CommandSymbol>();
    /**
     * The player executing this context.
     */
    private Player player;
    /**
     * File from where this EC was read from. May be null.
     */
    private File file;

    public ExecutionContext(File file) {
        this.file = file;
    }

    public ExecutionContext(Player player, File file) {
        this.player = player;
        this.file = file;
    }

    /**
     * Method that should be used to effectively locate symbols.
     *
     * @param name The symbol name
     * @return The symbol
     */
    public CommandSymbol findSymbol(String name) {
        return symbols.get(name);
    }
    
    public void addCommand(Command command) {
        commands.add(command);
    }

    public void addSymbol(CommandSymbol cmd) {
        symbols.put(cmd.getName(), cmd);
    }

    public void addAllSymbols(ExecutionContext context) {
        symbols.putAll(context.symbols);
    }

    public List<Command> getCommands() {
        return commands;
    }

    public String getPath() throws IOException {
        return file.getParentFile().getCanonicalPath();
    }

    public File getFile() {
        return file;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
