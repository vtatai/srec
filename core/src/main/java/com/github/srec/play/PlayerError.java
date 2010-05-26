package com.github.srec.play;

import com.github.srec.command.exception.CommandExecutionException;

/**
 * An error occurred during script execution.
 * 
* @author Victor Tatai
*/
public class PlayerError {
    /**
     * The line number.
     */
    private int line;
    /**
     * The line of text where the error occurred.
     */
    private String text;
    /**
     * The original exception.
     */
    private CommandExecutionException originatingException;

    public PlayerError(int line, String text, CommandExecutionException originatingException) {
        this.line = line;
        this.text = text;
        this.originatingException = originatingException;
    }

    public int getLine() {
        return line;
    }

    public String getText() {
        return text;
    }

    public CommandExecutionException getOriginatingException() {
        return originatingException;
    }

    @Override
    public String toString() {
        return "Script error on line " + line + ", message:\n" + originatingException.getMessage();
    }

    public void printStackTrace() {
        System.err.println(toString());
        originatingException.printStackTrace();
    }
}
