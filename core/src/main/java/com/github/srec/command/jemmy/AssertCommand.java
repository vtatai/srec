package com.github.srec.command.jemmy;

import com.github.srec.command.ExecutionContext;
import com.github.srec.command.ExecutionContextCommand;
import com.github.srec.command.value.Value;
import org.netbeans.jemmy.JemmyException;

import static com.github.srec.jemmy.JemmyDSL.textField;

/**
 * @author Victor Tatai
 */
@ExecutionContextCommand
public class AssertCommand extends JemmyEventCommand {
    public AssertCommand() {
        super("assert", "componentLocator", "text");
    }

    @Override
    protected void runJemmy(ExecutionContext ctx, Value... params) throws JemmyException {
        textField(coerceToString(get("componentLocator", params))).assertText(coerceToString(get("text", params)));
    }
}