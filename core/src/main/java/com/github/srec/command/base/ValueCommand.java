package com.github.srec.command.base;

import com.github.srec.command.ExecutionContext;
import com.github.srec.command.value.Value;

/**
 * @author Victor Tatai
 */
public interface ValueCommand extends Command {
    Value getValue(ExecutionContext context);
}
