package com.github.srec.command.jemmy;

import com.github.srec.command.ExecutionContext;
import org.netbeans.jemmy.JemmyException;

import java.awt.*;

import static com.github.srec.jemmy.JemmyDSL.dialog;

/**
 * @author Victor Tatai
 */
public class DialogActivateCommand extends JemmyEventCommand {
    public DialogActivateCommand() {
        super("dialog_activate", "componentLocator");
    }

    @Override
    protected void runJemmy(ExecutionContext ctx, String... params) throws JemmyException {
        dialog(params[0]);
    }
}