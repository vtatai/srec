package com.github.srec.command;

import com.github.srec.command.exception.CommandExecutionException;

/**
 * Defines a method (not a method call).
 * 
 * @author Victor Tatai
 */
public abstract class MethodCommand extends BaseCommand implements CommandSymbol {
    protected String[] parameters;

    protected MethodCommand(String name, String... parameters) {
        super(name);
        this.parameters = parameters;
    }

    /**
     * Executes the method call.
     *
     * @param context The execution context
     * @param params The parameters to run the method
     * @return The return value from the method call
     */
    public abstract String callMethod(ExecutionContext context, String... params);

    @Override
    public void run(ExecutionContext context) {
        // a def command by definition does not run
    }

    /**
     * Validates the parameters based only on count.
     *
     * @param params The runtime (call) params
     */
    protected void validateParameters(String... params) {
        int p1 = params == null ? 0 : params.length;
        int p2 = this.parameters == null ? 0 : this.parameters.length;
        if (p1 != p2) throw new CommandExecutionException("Different argument counts");
    }

    /**
     * Return true if the implementation for this command is native, ie, implemented directly in Java.
     *
     * @return true if native, false otherwise
     */
    public boolean isNative() {
        return false;
    }

    public String getName() {
        return name;
    }

    public String[] getParameters() {
        return parameters;
    }

    public void setParameters(String[] parameters) {
        this.parameters = parameters;
    }
}
