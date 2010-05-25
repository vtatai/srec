package com.github.srec.command.jemmy;

import com.github.srec.command.ExecutionContext;
import com.github.srec.command.ExecutionContextCommand;
import com.github.srec.command.value.Value;
import org.netbeans.jemmy.JemmyException;

import static com.github.srec.jemmy.JemmyDSL.click;

/**
 * @author Victor Tatai
 */
@ExecutionContextCommand
public class ClickCommand extends JemmyEventCommand {
    public ClickCommand() {
        super("click", "componentLocator");
    }

    @Override
    protected void runJemmy(ExecutionContext ctx, Value... params) throws JemmyException {
        click(coerceToString(params[0]));
    }
}
