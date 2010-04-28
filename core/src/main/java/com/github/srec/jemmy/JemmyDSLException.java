package com.github.srec.jemmy;

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
