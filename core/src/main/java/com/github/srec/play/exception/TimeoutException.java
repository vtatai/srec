package com.github.srec.play.exception;

import com.github.srec.Utils;
import com.github.srec.play.Command;

/**
 * @author Victor Tatai
 */
public class TimeoutException extends PlayerException {
    private Command command;
    private String[] parameters;

    public TimeoutException(Command command, String[] parameters) {
        super("Timeout exception for command: '" + command + "', parameters " + Utils.asString(parameters));
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