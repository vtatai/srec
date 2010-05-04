package com.github.srec.command;

/**
 * Interface which defines a context execution factory.
 * 
 * @author Victor Tatai
 */
public interface ExecutionContextFactory {
    ExecutionContext create(String currentPath);
}
