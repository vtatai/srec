package com.github.srec.command.parser;

import com.github.srec.SRecException;

/**
 * @author Victor Tatai
 */
public class SRecParseException extends SRecException {
    public SRecParseException() {
    }

    public SRecParseException(String message) {
        super(message);
    }

    public SRecParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public SRecParseException(Throwable cause) {
        super(cause);
    }
}
