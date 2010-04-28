package com.github.srec.command;

/**
 * Represents a generic command recorded.
 *
 * @author Victor Tatai
 */
public interface Command {
    String getName();
    void run();
}
