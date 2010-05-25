package com.github.srec.command.jemmy;

import com.github.srec.command.ExecutionContext;
import com.github.srec.command.ExecutionContextCommand;
import com.github.srec.command.value.Value;
import org.netbeans.jemmy.JemmyException;

import static com.github.srec.jemmy.JemmyDSL.frame;

/**
 * @author Victor Tatai
 */
@ExecutionContextCommand
public class WindowActivateCommand extends JemmyEventCommand {
    public WindowActivateCommand() {
        super("window_activate", "componentLocator");
    }

    @Override
    protected void runJemmy(ExecutionContext ctx, Value... params) throws JemmyException {
        frame(coerceToString(params[0])).activate();
    }
}
