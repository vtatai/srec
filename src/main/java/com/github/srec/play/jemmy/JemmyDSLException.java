package com.github.srec.play.jemmy;

public class JemmyDSLException extends RuntimeException {
    public JemmyDSLException(String message) {
        super(message);
    }

    public JemmyDSLException(Throwable throwable) {
        super(throwable);
    }
}
