package com.github.srec.command.exception;

import com.github.srec.Utils;
import com.github.srec.command.Command;
import com.github.srec.command.value.Value;

/**
 * @author Victor Tatai
 */
public class TimeoutException extends CommandExecutionException {
    private Command command;
    private Value[] parameters;

    public TimeoutException(Command command, Value[] parameters) {
        super("Timeout exception for command: '" + command + "', parameters " + Utils.asString(parameters));
        this.command = command;
        this.parameters = parameters;
    }

    public TimeoutException(Command command, Value[] parameters, Throwable throwable) {
        super(throwable);
        this.command = command;
        this.parameters = parameters;
    }

    public Command getCommand() {
        return command;
    }

    public Value[] getParameters() {
        return parameters;
    }
}