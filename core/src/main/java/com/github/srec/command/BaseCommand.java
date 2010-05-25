package com.github.srec.command;

import com.github.srec.command.parser.ParseLocation;

/**
 * Base class for commands.
 * 
 * @author Victor Tatai
 */
public abstract class BaseCommand implements Command {
    protected String name;
    protected ParseLocation location;

    protected BaseCommand(String name) {
        this.name = name;
    }

    protected BaseCommand(String name, ParseLocation location) {
        this(name);
        this.location = location;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ParseLocation getLocation() {
        return location;
    }
}
