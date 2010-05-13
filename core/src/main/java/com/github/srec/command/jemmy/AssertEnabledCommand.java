package com.github.srec.command.jemmy;

import com.github.srec.command.ExecutionContext;
import com.github.srec.command.ExecutionContextCommand;
import org.netbeans.jemmy.JemmyException;

import static com.github.srec.jemmy.JemmyDSL.waitEnabled;

/**
 * @author Victor Tatai
 */
@ExecutionContextCommand
public class AssertEnabledCommand extends JemmyEventCommand {
    public AssertEnabledCommand() {
        super("assert_enabled", new Parameter("componentLocator"), new Parameter("enabled", true, "true"));
    }

    @Override
    protected void runJemmy(ExecutionContext ctx, String... params) throws JemmyException {
        waitEnabled(get("componentLocator", params), Boolean.parseBoolean(get("enabled", params)));
    }
}