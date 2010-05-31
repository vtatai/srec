package com.github.srec;

/**
 * @author Victor Tatai
 */
public class UnsupportedFeatureException extends SRecException {
    public UnsupportedFeatureException(String message) {
        super(message);
    }

    public UnsupportedFeatureException(Throwable cause) {
        super(cause);
    }
}
