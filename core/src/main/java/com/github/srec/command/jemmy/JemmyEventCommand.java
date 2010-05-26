package com.github.srec.command.jemmy;

import com.github.srec.UnsupportedFeatureException;
import com.github.srec.command.CommandSymbol;
import com.github.srec.command.ExecutionContext;
import com.github.srec.command.VarCommand;
import com.github.srec.command.exception.CommandExecutionException;
import com.github.srec.command.exception.IllegalParametersException;
import com.github.srec.command.exception.TimeoutException;
import com.github.srec.command.method.MethodCommand;
import com.github.srec.command.method.MethodParameter;
import com.github.srec.command.value.*;
import com.github.srec.command.value.StringValue;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import org.netbeans.jemmy.JemmyException;
import org.netbeans.jemmy.TimeoutExpiredException;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.Map;

/**
 * @author Victor Tatai
 */
public abstract class JemmyEventCommand extends MethodCommand {
    public static final String LOCATOR = "locator";
    static {
        try {
            Velocity.init();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected JemmyEventCommand(String name, MethodParameter... parameters) {
        super(name, parameters);
    }

    @Override
    public Value internalCallMethod(ExecutionContext context, Map<String, Value> params) {
        try {
            runJemmy(context, params);
        } catch (JemmyException e) {
            if (e instanceof TimeoutExpiredException) throw new TimeoutException(this, params, e);
            throw new UnsupportedFeatureException(e.getMessage());
        }
        return null;
    }

    /**
     * Method which should be overridden by subclasses which run Jemmy commands. It is called by this class run method
     * and handles the exception translation.
     *
     * @param ctx The execution context
     * @param params The parameters to the method
     * @throws JemmyException The exception which may need to be translated
     */
    protected abstract void runJemmy(ExecutionContext ctx, Map<String, Value> params) throws JemmyException;

    @Override
    public boolean isNative() {
        return true;
    }

    /**
     * Coerces a value to String. Throws a {@link com.github.srec.command.exception.CommandExecutionException} if value
     * is not of the expected type.
     *
     * @param value The value
     * @return The converted value
     */
    protected String coerceToString(Value value, ExecutionContext context) {
        if (!(value instanceof StringValue)) throw new CommandExecutionException("Value is not a string");
        String str = value.toString();
        if (str.indexOf("$") == -1) return str;
        
        // Interpret as a velocity template
        Context velocityContext = new ContextAdapter(context);
        Writer writer = new StringWriter();
        try {
            Velocity.evaluate(velocityContext, writer, "VELOCITY", str);
        } catch (IOException e) {
            throw new CommandExecutionException(e);
        }
        return writer.toString();
    }

    /**
     * Coerces a value to BigDecimal. Throws a {@link com.github.srec.command.exception.CommandExecutionException} if value
     * is not of the expected type.
     *
     * @param value The value
     * @return The converted value
     */
    protected BigDecimal coerceToBigDecimal(Value value) {
        if (!(value instanceof NumberValue)) throw new CommandExecutionException("Value is not a number");
        return ((NumberValue) value).get();
    }

    /**
     * Coerces a value to Boolean. Throws a {@link com.github.srec.command.exception.CommandExecutionException} if value
     * is not of the expected type.
     *
     * @param value The value
     * @return The converted value
     */
    protected Boolean coerceToBoolean(Value value) {
        if (!(value instanceof BooleanValue)) throw new CommandExecutionException("Value is not a boolean");
        return ((BooleanValue) value).get();
    }

    /**
     * Shortcut for creating an array of parameters. The values passed should be the parameter name and the parameter
     * type, such as "text", Type.STRING, "index", Type.NUMBER. If only one item is passed it is assumed to be of type
     * STRING.
     *
     * @param params The array of parameter names and types
     * @return The array of created MethodParameters
     */
    protected static MethodParameter[] createParametersDefinition(Object... params) {
        if (params.length == 0) return null;
        if (params.length != 1 && params.length % 2 != 0) throw new IllegalParametersException("Incorrect number of params");
        if (params.length == 1) {
            MethodParameter[] ret = new MethodParameter[1];
            ret[0] = new MethodParameter(params[0].toString(), Type.STRING);
            return ret;
        }
        MethodParameter[] ret = new MethodParameter[params.length / 2];
        for (int i = 0; i < ret.length; i++) {
            String name = (String) params[i * 2];
            Type type = (Type) params[i * 2 + 1];
            ret[i] = new MethodParameter(name, type);
        }
        return ret;
    }

    private class ContextAdapter implements Context {
        private ExecutionContext executionContext;

        private ContextAdapter(ExecutionContext executionContext) {
            this.executionContext = executionContext;
        }

        @Override
        public Object put(String s, Object o) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object get(String name) {
            CommandSymbol s = executionContext.findSymbol(name);
            if (s == null) throw new CommandExecutionException("Variable '" + name + "' not found");
            if (!(s instanceof VarCommand)) throw new CommandExecutionException("Only variables allowed inside expressions");
            return ((VarCommand) s).getValue(executionContext);
        }

        @Override
        public boolean containsKey(Object o) {
            return executionContext.findSymbol(o.toString()) != null;
        }

        @Override
        public Object[] getKeys() {
            return executionContext.getSymbols().keySet().toArray();
        }

        @Override
        public Object remove(Object o) {
            throw new UnsupportedOperationException();
        }
    }
}
