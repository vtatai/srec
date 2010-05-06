package com.github.srec.command;

import com.github.srec.play.Player;

import java.io.File;

/**
 * Represents an execution context (EC) which has a parent EC.
 *
 * @author Victor Tatai
 */
public class NestedExecutionContext extends ExecutionContext {
    private ExecutionContext parent;

    public NestedExecutionContext(ExecutionContext parent, File file) {
        super(parent.getPlayer(), file, parent.getLoadPath().toArray(new String[parent.getLoadPath().size()]));
        this.parent = parent;
    }

    @Override
    public CommandSymbol findSymbol(String name) {
        CommandSymbol s = super.findSymbol(name);
        if (s != null) return s;
        return parent.findSymbol(name);
    }

    public ExecutionContext getParent() {
        return parent;
    }
}
