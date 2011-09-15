package com.github.srec;

/**
 * A subclass of SRecException which indicates that a feature is not 
 * supported. This exception is thrown to indicate that an SRec operation 
 * detected a request for an unsupported feature.
 * 
 * @author Victor Tatai
 */
@SuppressWarnings("serial")
public class UnsupportedFeatureException extends SRecException {

	/**
     * Constructs a new unsupported feature exception with the 
     * specified detail message. <p/>
     * 
     * @param message the detail message.
     */
    public UnsupportedFeatureException(String message) {
        super(message);
    }

    /**
     * Constructs a new unsupported feature exception with the specified 
     * cause and a detail message of (cause==null ? null : cause.toString()) 
     * (which typically contains the class and detail message of cause). 
     * <br/><br/>
     * This constructor is useful for SRec exceptions that are 
     * little more than wrappers for other throwables. <p/>
     * 
     * @param cause the cause that can be null for nonexistent or 
     * unknown causes.
     */
    public UnsupportedFeatureException(Throwable cause) {
        super(cause);
    }
}
