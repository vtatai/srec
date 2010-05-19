package com.github.srec.command;

/**
 * Base class for commands.
 * @author Victor Tatai
 */
public abstract class BaseCommand implements Command {
    protected String name;

    protected BaseCommand(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
