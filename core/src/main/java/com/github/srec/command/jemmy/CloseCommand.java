package com.github.srec.command.jemmy;

import com.github.srec.command.ExecutionContext;
import com.github.srec.command.ExecutionContextCommand;
import org.netbeans.jemmy.JemmyException;

import static com.github.srec.jemmy.JemmyDSL.frame;

/**
 * @author Victor Tatai
 */
@ExecutionContextCommand
public class CloseCommand extends JemmyEventCommand {
    public CloseCommand() {
        super("close", "componentLocator");
    }

    @Override
    protected void runJemmy(ExecutionContext ctx, String... params) throws JemmyException {
        frame(params[0]).close();
    }
}