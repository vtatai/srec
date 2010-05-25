package com.github.srec.command.jemmy;

import com.github.srec.command.ExecutionContext;
import com.github.srec.command.ExecutionContextCommand;
import com.github.srec.command.value.BooleanValue;
import com.github.srec.command.value.Value;
import org.netbeans.jemmy.JemmyException;

import static com.github.srec.jemmy.JemmyDSL.waitEnabled;

/**
 * @author Victor Tatai
 */
@ExecutionContextCommand
public class AssertEnabledCommand extends JemmyEventCommand {
    public AssertEnabledCommand() {
        super("assert_enabled", new Parameter("componentLocator"), new Parameter("enabled", true, BooleanValue.TRUE));
    }

    @Override
    protected void runJemmy(ExecutionContext ctx, Value... params) throws JemmyException {
        waitEnabled(coerceToString(get("componentLocator", params)),
                coerceToBoolean(get("enabled", params)));
    }
}