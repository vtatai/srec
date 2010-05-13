package com.github.srec.command.jemmy;

import com.github.srec.command.ExecutionContext;
import com.github.srec.command.ExecutionContextCommand;
import org.netbeans.jemmy.JemmyException;

import static com.github.srec.jemmy.JemmyDSL.textField;

/**
 * @author Victor Tatai
 */
@ExecutionContextCommand
public class AssertCommand extends JemmyEventCommand {
    public AssertCommand() {
        super("assert", "componentLocator");
    }

    @Override
    protected void runJemmy(ExecutionContext ctx, String... params) throws JemmyException {
        textField(params[0]).assertText(params[1]);
    }
}