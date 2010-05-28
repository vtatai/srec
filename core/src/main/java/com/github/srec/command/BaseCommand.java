package com.github.srec.command;

import com.github.srec.Location;

/**
 * Base class for commands.
 * 
 * @author Victor Tatai
 */
public abstract class BaseCommand implements Command {
    protected String name;
    protected Location location;

    protected BaseCommand(String name) {
        this.name = name;
    }

    protected BaseCommand(String name, Location location) {
        this(name);
        this.location = location;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Location getLocation() {
        return location;
    }
}
