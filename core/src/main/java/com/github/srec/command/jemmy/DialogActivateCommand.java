package com.github.srec.command.jemmy;

import com.github.srec.command.ExecutionContext;
import com.github.srec.command.ExecutionContextCommand;
import com.github.srec.command.value.Value;
import org.netbeans.jemmy.JemmyException;

import static com.github.srec.jemmy.JemmyDSL.dialog;

/**
 * @author Victor Tatai
 */
@ExecutionContextCommand
public class DialogActivateCommand extends JemmyEventCommand {
    public DialogActivateCommand() {
        super("dialog_activate", "componentLocator");
    }

    @Override
    protected void runJemmy(ExecutionContext ctx, Value... params) throws JemmyException {
        dialog(coerceToString(params[0]));
    }
}