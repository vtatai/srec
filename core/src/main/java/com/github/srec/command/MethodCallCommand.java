package com.github.srec.command;

import com.github.srec.command.exception.CommandExecutionException;
import com.github.srec.command.parser.ParseLocation;
import com.github.srec.command.value.Value;

import java.util.ArrayList;
import java.util.List;

/**
 * A recorder name which represents a method call.
 *
 * @author Victor Tatai
 */
public class MethodCallCommand extends BaseCommand implements ValueCommand {
    protected List<ValueCommand> parameters = new ArrayList<ValueCommand>();

    public MethodCallCommand(String name, ParseLocation tree) {
        super(name, tree);
    }

    @Override
    public void run(ExecutionContext context) {
        getValue(context);
    }

    @Override
    public Value getValue(ExecutionContext context) {
        MethodCommand method = (MethodCommand) context.findSymbol(name);
        if (method == null) throw new CommandExecutionException("Method " + name + " not found.");
        return method.callMethod(context, convertParameters(context));
    }

    private Value[] convertParameters(ExecutionContext context) {
        Value[] ret = new Value[parameters.size()];
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
        ValueCommand command = getParameter(index);
        return command == null ? null : command.getName();
    }

    @Override
    public String toString() {
        return getName();
    }
}
