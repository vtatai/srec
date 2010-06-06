package com.github.srec.command.exception;

import com.github.srec.SRecException;

/**
 * @author Victor Tatai
 */
public class CommandExecutionException extends SRecException {

    public CommandExecutionException() {
    }

    public CommandExecutionException(String message) {
        super(message);
    }

    public CommandExecutionException(Throwable throwable) {
        super(throwable);
    }
}
