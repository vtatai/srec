package com.github.srec.command.exception;

/**
 * @author Victor Tatai
 */
public class UnsupportedCommandException extends CommandExecutionException {
    public UnsupportedCommandException(String command) {
        super("Command '" + command + "' not supported.");
    }
}
