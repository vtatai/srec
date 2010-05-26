package com.github.srec.command.parser;

import com.github.srec.command.ExecutionContext;
import com.github.srec.command.TestSuite;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * A parser parses srec scripts.
 *
 * @author Victor Tatai
 */
public interface Parser {
    /**
     * Parses the given file.
     *
     * @param context The context
     * @param file The file being parsed
     * @return The test suite read
     */
    TestSuite parse(ExecutionContext context, File file);

    /**
     * Parses the given file.
     *
     * @param context The context
     * @param is The input stream
     * @param file The file being parsed
     * @return The test suite read
     */
    TestSuite parse(ExecutionContext context, InputStream is, File file);

    /**
     * Gets the errors.
     *
     * @return The errors
     */
    List<ParseError> getErrors();
}
