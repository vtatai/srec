package com.github.srec.command.parser;

import com.github.srec.Location;

/**
 * Represents an error while parsing.
 *
 * @author Victor Tatai
 */
public class ParseError {
    public enum Severity {
        FATAL, ERROR, WARN
    }

    private Severity severity;
    private Location location;
    private String message;

    public ParseError(Severity severity, Location location, String message) {
        this.severity = severity;
        this.location = location;
        this.message = message;
    }

    public Severity getSeverity() {
        return severity;
    }

    public Location getLocation() {
        return location;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return severity + ": " + message + "\n" + location;
    }
}
