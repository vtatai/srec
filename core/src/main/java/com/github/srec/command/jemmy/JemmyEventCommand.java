package com.github.srec.command.jemmy;

import com.github.srec.UnsupportedFeatureException;
import com.github.srec.command.ExecutionContext;
import com.github.srec.command.MethodCommand;
import com.github.srec.command.exception.TimeoutException;
import org.netbeans.jemmy.JemmyException;
import org.netbeans.jemmy.TimeoutExpiredException;

/**
 * @author Victor Tatai
 */
public abstract class JemmyEventCommand extends MethodCommand {

    protected JemmyEventCommand(String name, String... params) {
        super(name, params);
    }

    @Override
    public void callMethod(ExecutionContext context, String... params) {
        try {
            runJemmy(context, params);
        } catch (JemmyException e) {
            if (e instanceof TimeoutExpiredException) throw new TimeoutException(this, parameters, e);
            throw new UnsupportedFeatureException(e.getMessage());
        }
    }

    /**
     * Method which should be overridden by subclasses which run Jemmy commands. It is called by this class run method
     * and handles the exception translation.
     *
     * @param ctx The execution context
     * @param params The parameters to the method
     * @throws JemmyException The exception which may need to be translated
     */
    protected abstract void runJemmy(ExecutionContext ctx, String... params) throws JemmyException;

    @Override
    public boolean isNative() {
        return true;
    }
}
