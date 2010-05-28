package com.github.srec.play;

import com.github.srec.Location;
import com.github.srec.command.exception.CommandExecutionException;

/**
 * An error occurred during script execution.
 * 
* @author Victor Tatai
*/
public class PlayerError {
    private Location location;
    /**
     * The original exception.
     */
    private CommandExecutionException originatingException;
    private String testSuite;
    private String testCase;

    public PlayerError(String testSuite, String testCase, Location location, CommandExecutionException originatingException) {
        this.testSuite = testSuite;
        this.testCase = testCase;
        this.location = location;
        this.originatingException = originatingException;
    }

    public int getLineNumber() {
        return location.getLineNumber();
    }

    public String getLine() {
        return location.getLine();
    }

    public CommandExecutionException getOriginatingException() {
        return originatingException;
    }

    public Location getLocation() {
        return location;
    }

    public String getTestSuite() {
        return testSuite;
    }

    public String getTestCase() {
        return testCase;
    }

    @Override
    public String toString() {
        return "Script error on test suite '" + testSuite + "', test case '" + testCase + "', at " + location + ", message:\n" + originatingException.getMessage();
    }
}
