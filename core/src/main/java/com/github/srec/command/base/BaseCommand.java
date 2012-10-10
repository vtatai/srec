package com.github.srec.command.base;

import com.github.srec.Location;
import com.github.srec.command.ExecutionContext;
import com.github.srec.command.exception.CommandExecutionException;
import com.github.srec.command.value.*;
import com.github.srec.command.value.StringValue;
import com.github.srec.util.Utils;
import org.netbeans.jemmy.operators.JComponentOperator;

import java.math.BigDecimal;

/**
 * Base class for commands.
 * 
 * @author Victor Tatai
 */
public abstract class BaseCommand implements Command {
	protected String name;
	protected Location location;

	protected BaseCommand(String name) {
		this.name = name;
	}

	protected BaseCommand(String name, Location location) {
		this(name);
		this.location = location;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Location getLocation() {
		return location;
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// UTILITIES METHODS //
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Coerces a value to String. Throws a
	 * {@link com.github.srec.command.exception.CommandExecutionException} if
	 * value is not of the expected type.
	 * 
	 * @param value
	 *            The value
	 * @param context
	 *            The context used to evaluate the text, required if there is
	 *            string interpolation
	 * @return The converted value
	 */
	protected static String coerceToString(Value value, ExecutionContext context) {
		if (value == null)
			return null;
		if (!(value instanceof StringValue))
			throw new CommandExecutionException("Value " + value + ", class "
					+ value.getClass().getCanonicalName() + " is not a string");
		String str = value.toString();
		if (str.indexOf("$") == -1 || context == null)
			return str;
		return (String) Utils.groovyEvaluate(context, "\"" + str + "\"");
	}

	/**
	 * Coerces a value to BigDecimal. Throws a
	 * {@link com.github.srec.command.exception.CommandExecutionException} if
	 * value is not of the expected type.
	 * 
	 * 
	 * @param value
	 *            The value
	 * @return The converted value
	 */
	protected BigDecimal coerceToBigDecimal(Value value) {
		
		if (!(value instanceof NumberValue))
			throw new CommandExecutionException("Value is not a number");
		return ((NumberValue) value).get();
	}

	protected static <T extends Enum<T>> T coerceToEnum(Value<?> value,
			ExecutionContext context, Class<T> enumClass) {
		String result = coerceToString(value, context);
		return Enum.valueOf(enumClass, result.toUpperCase());
	}

	/**
	 * Coerces a value to Boolean. Throws a
	 * {@link com.github.srec.command.exception.CommandExecutionException} if
	 * value is not of the expected type.
	 * 
	 * @param value
	 *            The value
	 * @return The converted value
	 */
	protected Boolean coerceToBoolean(Value value) {
		if (!(value instanceof BooleanValue))
			throw new CommandExecutionException("Value is not a boolean");
		return ((BooleanValue) value).get();
	}

    protected static String[] coerceToArray(Value<?> value, ExecutionContext context) {
        String list = coerceToString(value, context);
        return list.split(" *, *");
    }

	/**
	 * Finds a symbol which should wrap a Java object.
	 * 
	 * @param name
	 *            The symbol name
	 * @param context
	 *            The EC
	 * @return The object, if it was found by Jemmy returns the source
	 */
	protected Object findObjectSymbol(String name, ExecutionContext context) {
		CommandSymbol s = context.findSymbol(name);
		if (s == null)
			throw new CommandExecutionException("Symbol cannot be null");
		if (!(s instanceof VarCommand))
			throw new CommandExecutionException("Symbol must be a variable");
		Value value = ((VarCommand) s).getValue(null);
		if (value == null)
			throw new CommandExecutionException("Symbol cannot be null");
		if (value.getType() != Type.OBJECT)
			throw new CommandExecutionException(
					"Symbol must refer to a Java object");
		Object obj = value.get();
		if (obj instanceof JComponentOperator) {
			// In case it was found by Jemmy, we want to get the source
			// TODO make this Jemmy independent
			obj = ((JComponentOperator) obj).getSource();
		}
		return obj;
	}
}
