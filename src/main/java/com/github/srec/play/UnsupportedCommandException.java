package com.github.srec.play;

public class UnsupportedCommandException extends PlayerException {
    public UnsupportedCommandException(String command) {
        super("Command '" + command + "' not supported.");
    }
}
