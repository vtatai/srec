package com.github.srec.command;

import com.github.srec.play.exception.PlayerException;

/**
 * @author Victor Tatai
 */
public class UnsupportedCommandException extends PlayerException {
    public UnsupportedCommandException(String command) {
        super("Command '" + command + "' not supported.");
    }
}
