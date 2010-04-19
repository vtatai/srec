package com.github.srec.play.jemmy;

/**
 * @author Victor Tatai
 */
public class JemmyDSLException extends RuntimeException {
    public JemmyDSLException(String message) {
        super(message);
    }

    public JemmyDSLException(Throwable throwable) {
        super(throwable);
    }
}
