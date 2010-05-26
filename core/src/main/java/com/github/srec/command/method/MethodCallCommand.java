package com.github.srec.command.method;

import com.github.srec.command.BaseCommand;
import com.github.srec.command.ExecutionContext;
import com.github.srec.command.ValueCommand;
import com.github.srec.command.exception.CommandExecutionException;
import com.github.srec.command.parser.ParseLocation;
import com.github.srec.command.value.Value;

import java.util.HashMap;
import java.util.Map;

/**
 * A recorder name which represents a method call.
 *
 * @author Victor Tatai
 */
public class MethodCallCommand extends BaseCommand implements ValueCommand {
    protected Map<String, ValueCommand> parameters = new HashMap<String, ValueCommand>();

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

    private Map<String, Value> convertParameters(ExecutionContext context) {
        Map<String, Value> ret = new HashMap<String, Value>();
        for (Map.Entry<String, ValueCommand> entry : parameters.entrySet()) {
            ret.put(entry.getKey(), entry.getValue().getValue(context));
        }
        return ret;
    }

    public void addParameter(String name, ValueCommand v) {
        parameters.put(name, v);
    }

    public Map<String, ValueCommand> getParameters() {
        return parameters;
    }

    public ValueCommand getParameter(String name) {
        return parameters == null ? null : parameters.get(name);
    }

    public String getParameterString(String name) {
        ValueCommand command = getParameter(name);
        return command == null ? null : command.getName();
    }

    @Override
    public String toString() {
        return getName();
    }
}
