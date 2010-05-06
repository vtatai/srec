package com.github.srec.command.parser;

import com.github.srec.SRecException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Victor Tatai
 */
public class ParseException extends SRecException {
    private List<ParseError> errors = new ArrayList<ParseError>();
    public ParseException(String message) {
        super(message);
    }

    public ParseException(List<ParseError> errors) {
        this.errors = errors;
    }

    public ParseException(Throwable cause) {
        super(cause);
    }

    public List<ParseError> getErrors() {
        return errors;
    }

    public void printErrors() {
        for (ParseError error : errors) {
            System.err.println(error);
        }
    }
}
