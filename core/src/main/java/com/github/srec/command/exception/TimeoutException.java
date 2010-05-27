package com.github.srec.command.exception;

import com.github.srec.command.Command;
import com.github.srec.command.value.Value;
import com.github.srec.util.Utils;

import java.util.Map;

/**
 * @author Victor Tatai
 */
public class TimeoutException extends CommandExecutionException {
    private Command command;
    private Map<String, Value> parameters;

    public TimeoutException(Command command, Map<String, Value> parameters) {
        super("Timeout exception for command: '" + command + "', parameters " + Utils.asString(parameters));
        this.command = command;
        this.parameters = parameters;
    }

    public TimeoutException(Command command, Map<String, Value> parameters, Throwable throwable) {
        super(throwable);
        this.command = command;
        this.parameters = parameters;
    }

    public Command getCommand() {
        return command;
    }

    public Map<String, Value> getParameters() {
        return parameters;
    }
}