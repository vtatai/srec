package com.github.srec.play;

import com.github.srec.SRecException;

/**
 * @author Victor Tatai
 */
public class PlayerException extends SRecException {
    public PlayerException() {
    }

    public PlayerException(String message) {
        super(message);
    }

    public PlayerException(String message, Throwable cause) {
        super(message, cause);
    }

    public PlayerException(Throwable cause) {
        super(cause);
    }
}
