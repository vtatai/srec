package com.github.srec.command;

import com.github.srec.command.exception.CommandExecutionException;
import org.antlr.runtime.tree.CommonTree;

/**
 * A recorder name which represents a method call.
 *
 * @author Victor Tatai
 */
public class CallCommand extends BaseCommand {
    protected String[] parameters;

    public CallCommand(String name, CommonTree tree, String... parameters) {
        super(name, tree);
        this.parameters = parameters;
    }

    @Override
    public void run(ExecutionContext context) {
        MethodCommand method = context.getSymbol(name);
        if (method == null) throw new CommandExecutionException("Method " + name + " not found.");
        method.callMethod(context, parameters);
    }

    public String[] getParameters() {
        return parameters;
    }

    public String getParameter(int index) {
        return parameters == null || parameters.length - 1 < index ? null : parameters[index];
    }
}
