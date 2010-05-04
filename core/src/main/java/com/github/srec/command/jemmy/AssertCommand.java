package com.github.srec.command.jemmy;

import com.github.srec.command.ExecutionContext;
import org.netbeans.jemmy.JemmyException;

import static com.github.srec.jemmy.JemmyDSL.assertText;

/**
 * @author Victor Tatai
 */
public class AssertCommand extends JemmyEventCommand {
    public AssertCommand() {
        super("assert", "componentLocator");
    }

    @Override
    protected void runJemmy(ExecutionContext ctx, String... params) throws JemmyException {
        assertText(params[0], params[1]);
    }
}