package com.github.srec.play.exception;

/**
 * @author Victor Tatai
 */
public class UnsupportedCommandException extends PlayerException {
    public UnsupportedCommandException(String command) {
        super("Command '" + command + "' not supported.");
    }
}
