package com.github.srec.command;

/**
 * A command which aggregates several others.
 *
 * @author Victor Tatai
 */
public interface AggregateCommand extends Command {
    /**
     * Adds a command to this command.
     *
     * @param command The child command
     * @return true if the next command should still be considered a child command
     */
    boolean add(Command command);
}
