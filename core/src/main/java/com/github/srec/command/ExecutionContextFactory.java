package com.github.srec.command;

import java.io.File;

/**
 * Interface which defines a context execution factory.
 * 
 * @author Victor Tatai
 */
public interface ExecutionContextFactory {
    ExecutionContext create(File file);
}
