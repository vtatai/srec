package com.github.srec.command.exception;

/**
 * @author Victor Tatai
 */
public class CommandExecutionException extends RuntimeException {

    public CommandExecutionException() {
    }

    public CommandExecutionException(String message) {
        super(message);
    }

    public CommandExecutionException(Throwable throwable) {
        super(throwable);
    }
}
