package com.github.srec.command;

import com.github.srec.command.exception.CommandExecutionException;

/**
 * @author Victor Tatai
 */
public class UnsupportedCommandException extends CommandExecutionException {
    public UnsupportedCommandException(String command) {
        super("Command '" + command + "' not supported.");
    }
}
