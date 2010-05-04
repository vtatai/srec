package com.github.srec.command.exception;

import com.github.srec.Utils;
import com.github.srec.command.Command;

/**
 * @author Victor Tatai
 */
public class TimeoutException extends CommandExecutionException {
    private Command command;
    private String[] parameters;

    public TimeoutException(Command command, String[] parameters) {
        super("Timeout exception for command: '" + command + "', parameters " + Utils.asString(parameters));
        this.command = command;
        this.parameters = parameters;
    }

    public TimeoutException(Command command, String[] parameters, Throwable throwable) {
        super(throwable);
        this.command = command;
        this.parameters = parameters;
    }

    public Command getCommand() {
        return command;
    }

    public String[] getParameters() {
        return parameters;
    }
}