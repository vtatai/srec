package com.github.srec.command;

import com.github.srec.command.exception.CommandExecutionException;
import org.antlr.runtime.tree.CommonTree;

import java.util.ArrayList;
import java.util.List;

/**
 * A recorder name which represents a method call.
 *
 * @author Victor Tatai
 */
public class CallCommand extends BaseCommand implements ValueCommand {
    protected List<ValueCommand> parameters = new ArrayList<ValueCommand>();

    public CallCommand(String name, CommonTree tree) {
        super(name, tree);
    }

    @Override
    public void run(ExecutionContext context) {
        getValue(context);
    }

    @Override
    public String getValue(ExecutionContext context) {
        MethodCommand method = (MethodCommand) context.findSymbol(name);
        if (method == null) throw new CommandExecutionException("Method " + name + " not found.");
        return method.callMethod(context, convertParameters(context));
    }

    private String[] convertParameters(ExecutionContext context) {
        String[] ret = new String[parameters.size()];
        int i = 0;
        for (ValueCommand parameter : parameters) {
            ret[i++] = parameter.getValue(context);
        }
        return ret;
    }

    public void addParameter(ValueCommand v) {
        parameters.add(v);
    }

    public List<ValueCommand> getParameters() {
        return parameters;
    }

    public void setParameters(List<ValueCommand> parameters) {
        this.parameters = parameters;
    }

    public ValueCommand getParameter(int index) {
        return parameters == null || parameters.size() - 1 < index ? null : parameters.get(index);
    }

    public String getParameterString(int index) {
        return getParameter(index).getName();
    }
}
