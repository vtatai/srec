package com.github.srec.command.jemmy;

import com.github.srec.command.ExecutionContext;
import org.netbeans.jemmy.JemmyException;

import static com.github.srec.jemmy.JemmyDSL.frame;

/**
 * @author Victor Tatai
 */
public class WindowActivateCommand extends JemmyEventCommand {
    public WindowActivateCommand() {
        super("window_activate", "componentLocator");
    }

    @Override
    protected void runJemmy(ExecutionContext ctx, String... params) throws JemmyException {
        frame(params[0]).activate();
    }
}
