package com.github.srec.command;

import com.github.srec.command.exception.CommandExecutionException;
import org.antlr.runtime.tree.CommonTree;

/**
 * Represents a literal value.
 * 
 * @author Victor Tatai
 */
public class LiteralCommand extends BaseCommand implements ValueCommand {
    private String value;

    public LiteralCommand(String value) {
        super(value);
        this.value = value;
    }

    public LiteralCommand(CommonTree tree, String value) {
        super(value, tree);
        this.value = value;
    }

    @Override
    public void run(ExecutionContext context) throws CommandExecutionException {
    }

    @Override
    public String getValue(ExecutionContext context) {
        return value;
    }
}
