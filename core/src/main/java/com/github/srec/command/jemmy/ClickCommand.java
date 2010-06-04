package com.github.srec.command.jemmy;

import com.github.srec.command.ExecutionContext;
import com.github.srec.command.SRecCommand;
import com.github.srec.command.value.Value;
import org.netbeans.jemmy.JemmyException;

import java.util.Map;

import static com.github.srec.jemmy.JemmyDSL.click;

/**
 * @author Victor Tatai
 */
@SRecCommand
public class ClickCommand extends JemmyEventCommand {
    public ClickCommand() {
        super("click", createParametersDefinition(LOCATOR));
    }

    @Override
    protected void runJemmy(ExecutionContext ctx, Map<String, Value> params) throws JemmyException {
        click(coerceToString(params.get(LOCATOR), ctx));
    }
}
