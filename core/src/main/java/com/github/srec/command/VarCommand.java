package com.github.srec.command;

import com.github.srec.command.exception.CommandExecutionException;
import org.antlr.runtime.tree.CommonTree;

/**
 * Represents a variable declaration.
 *
 * @author Victor Tatai
 */
public class VarCommand extends BaseCommand implements CommandSymbol, ValueCommand {
    private String value;

    public VarCommand(String name, String value) {
        super(name);
        this.value = value;
    }

    public VarCommand(String name, CommonTree tree, String value) {
        super(name, tree);
        this.value = value;
    }

    @Override
    public void run(ExecutionContext context) throws CommandExecutionException {
        // Do nothing for now
    }

    @Override
    public String getValue(ExecutionContext context) {
        return value;
    }
}
