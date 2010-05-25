package com.github.srec.command.jemmy;

import com.github.srec.UnsupportedFeatureException;
import com.github.srec.command.ExecutionContext;
import com.github.srec.command.MethodCommand;
import com.github.srec.command.exception.CommandExecutionException;
import com.github.srec.command.exception.TimeoutException;
import com.github.srec.command.value.BooleanValue;
import com.github.srec.command.value.NumberValue;
import com.github.srec.command.value.StringValue;
import com.github.srec.command.value.Value;
import org.netbeans.jemmy.JemmyException;
import org.netbeans.jemmy.TimeoutExpiredException;

import java.math.BigDecimal;

/**
 * @author Victor Tatai
 */
public abstract class JemmyEventCommand extends MethodCommand {

    protected JemmyEventCommand(String name, String... parameters) {
        super(name, parameters);
    }

    protected JemmyEventCommand(String name, Parameter... parameters) {
        super(name, parameters);
    }

    @Override
    public Value internalCallMethod(ExecutionContext context, Value... params) {
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
    protected abstract void runJemmy(ExecutionContext ctx, Value... params) throws JemmyException;

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
    protected String coerceToString(Value value) {
        if (!(value instanceof StringValue)) throw new CommandExecutionException();
        return value.toString();
    }

    /**
     * Coerces a value to BigDecimal. Throws a {@link com.github.srec.command.exception.CommandExecutionException} if value
     * is not of the expected type.
     *
     * @param value The value
     * @return The converted value
     */
    protected BigDecimal coerceToBigDecimal(Value value) {
        if (!(value instanceof NumberValue)) throw new CommandExecutionException();
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
        if (!(value instanceof BooleanValue)) throw new CommandExecutionException();
        return ((BooleanValue) value).get();
    }
}
