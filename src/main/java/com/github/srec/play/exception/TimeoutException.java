package com.github.srec.play.exception;

import com.github.srec.play.Command;

public class TimeoutException extends PlayerException {
    private Command command;
    private String[] parameters;

    public TimeoutException(Command command, String[] parameters) {
        super("Timeout exception for command: '" + command + "', parameters " + asString(parameters));
        this.command = command;
        this.parameters = parameters;
    }

    private static String asString(String[] parameters) {
        if (parameters == null || parameters.length == 0) return "";
        StringBuilder strb = new StringBuilder(parameters[0]);
        for (int i = 1; i < parameters.length; i++) {
            String parameter = parameters[i];
            strb.append(", ").append(parameter);
        }
        return strb.toString();
    }

    public Command getCommand() {
        return command;
    }

    public String[] getParameters() {
        return parameters;
    }
}