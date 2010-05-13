package com.github.srec.command.jemmy;

import com.github.srec.command.ExecutionContext;
import com.github.srec.command.ExecutionContextCommand;
import com.github.srec.jemmy.JemmyDSL;
import org.netbeans.jemmy.JemmyException;

/**
 * @author Victor Tatai
 */
@ExecutionContextCommand
public class AssertEnabledCommand extends JemmyEventCommand {
    public AssertEnabledCommand() {
        super("assert_enabled", "componentLocator", "enabled");
    }

    @Override
    protected void runJemmy(ExecutionContext ctx, String... params) throws JemmyException {
        boolean enabled = true;
        if (params.length == 2) {
            enabled = Boolean.parseBoolean(params[1]);
        }
        JemmyDSL.waitEnabled(params[0], enabled);
    }
}