package com.github.srec.command;

/**
 * Factory for commands.
 *
 * @author Victor Tatai
 */
public interface CommandFactory {
    Command buildCommand(String command, String... params);
}
