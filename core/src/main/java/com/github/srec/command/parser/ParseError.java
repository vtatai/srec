package com.github.srec.command.parser;

import org.antlr.runtime.tree.CommonTree;

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
    private CommonTree tree;
    private String message;
    private String file;

    public ParseError(Severity severity, String file, CommonTree tree, String message) {
        this.severity = severity;
        this.file = file;
        this.tree = tree;
        this.message = message;
    }

    public Severity getSeverity() {
        return severity;
    }

    public CommonTree getTree() {
        return tree;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return severity + " on " + file + ", line " + tree.getLine() + ": " + message;
    }
}
