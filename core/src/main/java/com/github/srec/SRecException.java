package com.github.srec;

/**
 * Base exception class for SRec. It's a subclass of RuntimeException.
 * 
 * @author Victor Tatai
 */
@SuppressWarnings("serial")
public class SRecException extends RuntimeException {
	
	/**
	 * Constructs a new SRec exception with null as its detail message.
	 */
    public SRecException() {}

    /**
     * Constructs a new SRec exception with the specified detail message.<p/>
     * 
     * @param message the detail message.
     */
    public SRecException(String message) {
        super(message);
    }

    /**
     * Constructs a new SRec exception with the specified detail 
     * message and cause. <p/>
     * 
     * @param message the detail message.
     * @param cause the cause that can be null for nonexistent or 
     * unknown causes.
     */
    public SRecException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new SRec exception with the specified cause and 
     * a detail message of (cause==null ? null : cause.toString()) 
     * (which typically contains the class and detail message of cause). 
     * <br/><br/>
     * This constructor is useful for SRec exceptions that are 
     * little more than wrappers for other throwables. <p/>
     * 
     * @param cause the cause that can be null for nonexistent or 
     * unknown causes.
     */
    public SRecException(Throwable cause) {
        super(cause);
    }
}
