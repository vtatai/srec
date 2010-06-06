package com.github.srec.command.jemmy;

import com.github.srec.UnsupportedFeatureException;
import com.github.srec.command.ExecutionContext;
import com.github.srec.command.exception.TimeoutException;
import com.github.srec.command.method.MethodCommand;
import com.github.srec.command.method.MethodParameter;
import com.github.srec.command.value.Value;
import org.netbeans.jemmy.JemmyException;
import org.netbeans.jemmy.TimeoutExpiredException;

import java.util.Map;

/**
 * @author Victor Tatai
 */
public abstract class JemmyEventCommand extends MethodCommand {
    public static final String LOCATOR = "locator";

    protected JemmyEventCommand(String name, MethodParameter... parameters) {
        // Jemmy defined commands have no location information as they are native
        super(name, parameters);
    }

    @Override
    public Value internalCallMethod(ExecutionContext context, Map<String, Value> params) {
        try {
            runJemmy(context, params);
        } catch (JemmyException e) {
            if (e instanceof TimeoutExpiredException) throw new TimeoutException(this, params, e);
            throw new UnsupportedFeatureException(e);
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
}
