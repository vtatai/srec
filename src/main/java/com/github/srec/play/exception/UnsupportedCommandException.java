package com.github.srec.play.exception;

public class UnsupportedCommandException extends PlayerException {
    public UnsupportedCommandException(String command) {
        super("Command '" + command + "' not supported.");
    }
}
