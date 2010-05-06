package com.github.srec.command;

/**
 * @author Victor Tatai
 */
public interface ValueCommand extends Command {
    String getValue(ExecutionContext context);
}
