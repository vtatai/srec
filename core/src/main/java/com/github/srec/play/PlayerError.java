package com.github.srec.play;

import java.io.PrintWriter;
import java.io.StringWriter;

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
        StringBuilder sb = new StringBuilder();
        sb.append("Script error on test suite '").append(testSuite).append("', test case '");
        sb.append(testCase).append("', at ").append(location).append(", message:\n").append(originatingException.getMessage());
        sb.append("\nOrignal stack trace:\n");
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        originatingException.printStackTrace(pw);
        sb.append(sw.toString());
        return sb.toString();
    }
}
