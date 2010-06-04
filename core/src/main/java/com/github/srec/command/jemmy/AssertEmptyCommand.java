package com.github.srec.command.jemmy;

import com.github.srec.command.ExecutionContext;
import com.github.srec.command.SRecCommand;
import com.github.srec.command.value.Value;
import org.netbeans.jemmy.JemmyException;

import java.util.Map;

import static com.github.srec.jemmy.JemmyDSL.textField;

/**
 * @author Victor Tatai
 */
@SRecCommand
public class AssertEmptyCommand extends JemmyEventCommand {
    public AssertEmptyCommand() {
        super("assert_empty", createParametersDefinition(LOCATOR));
    }

    @Override
    protected void runJemmy(ExecutionContext ctx, Map<String, Value> params) throws JemmyException {
        textField(coerceToString(params.get(LOCATOR), ctx)).assertEmpty();
    }
}