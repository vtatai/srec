package com.github.srec.command.jemmy;

import com.github.srec.command.ExecutionContext;
import com.github.srec.command.SRecCommand;
import com.github.srec.command.exception.AssertionFailedException;
import com.github.srec.command.value.Type;
import com.github.srec.command.value.Value;
import com.github.srec.jemmy.JemmyDSL;
import com.github.srec.jemmy.JemmyDSL.Dialog;

import org.netbeans.jemmy.JemmyException;

import java.util.Map;

/**
 * Asserts that a dialog exists and that has a subcomponent showing the text passed as a parameter.
 *
 * @author Roberto Trentini Jr.
 */
@SRecCommand
public class AssertDialogCommand extends JemmyEventCommand {
    public AssertDialogCommand() {
        super("assert_dialog", params("id", Type.STRING, "text", Type.STRING));
    }

    @Override
    protected void runJemmy(ExecutionContext ctx, Map<String, Value> params) throws JemmyException {
        String id = coerceToString(params.get("id"), ctx);
        Dialog dialog = JemmyDSL.getDialogById(id);
        if (dialog == null) throw new AssertionFailedException("assert_dialog: id " + id + " is null or does not refer to a dialog");
        String text = coerceToString(params.get("text"), ctx);
        System.err.println(text);
		dialog.assertText(text);
    }
}