package com.github.srec.play.exception;

public class PlayerException extends RuntimeException {

    public PlayerException() {
    }

    public PlayerException(String message) {
        super(message);
    }

    public PlayerException(Throwable throwable) {
        super(throwable);
    }
}
