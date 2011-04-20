package com.github.srec.command.jemmy;

import com.github.srec.command.ExecutionContext;
import com.github.srec.command.SRecCommand;
import com.github.srec.command.value.Type;
import com.github.srec.command.value.Value;
import org.netbeans.jemmy.JemmyException;

import java.util.Map;

import static com.github.srec.jemmy.JemmyDSL.waitHasFocus;

/**
 * @author Victor Tatai
 */
@SRecCommand
public class AssertHasFocusCommand extends JemmyEventCommand {
    public AssertHasFocusCommand() {
        super("assert_has_focus", param(LOCATOR, Type.STRING));
    }

    @Override
    protected void runJemmy(ExecutionContext ctx, Map<String, Value> params) throws JemmyException {
        waitHasFocus(coerceToString(params.get(LOCATOR), ctx));
    }
}