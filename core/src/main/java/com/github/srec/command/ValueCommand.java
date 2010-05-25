package com.github.srec.command;

import com.github.srec.command.value.Value;

/**
 * @author Victor Tatai
 */
public interface ValueCommand extends Command {
    Value getValue(ExecutionContext context);
}
