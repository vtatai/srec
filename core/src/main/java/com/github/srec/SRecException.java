package com.github.srec;

/**
 * Base exception class for srec.
 * @author Victor Tatai
 */
public class SRecException extends RuntimeException {
    public SRecException() {
    }

    public SRecException(String message) {
        super(message);
    }

    public SRecException(String message, Throwable cause) {
        super(message, cause);
    }

    public SRecException(Throwable cause) {
        super(cause);
    }
}
