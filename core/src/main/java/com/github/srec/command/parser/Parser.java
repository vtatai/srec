package com.github.srec.command.parser;

import com.github.srec.command.ExecutionContext;

import java.io.File;

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
     * @param file The file
     */
    void parse(ExecutionContext context, File file);
}
