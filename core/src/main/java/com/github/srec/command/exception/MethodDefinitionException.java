package com.github.srec.command.exception;

import com.github.srec.SRecException;

/**
 * Exception thrown when there was a problem in a method definition.
 * 
 * @author Victor Tatai
 */
public class MethodDefinitionException extends SRecException {
    public MethodDefinitionException(String message) {
        super(message);
    }
}
