package com.github.srec.command.jemmy;

import com.github.srec.command.ExecutionContext;
import com.github.srec.command.SRecCommand;
import com.github.srec.command.value.Type;
import com.github.srec.command.value.Value;
import com.github.srec.jemmy.JemmyDSL;
import org.netbeans.jemmy.JemmyException;

import java.util.Map;

/**
 * @author Victor Tatai
 */
@SRecCommand
public class AssertComboBoxCommand extends JemmyEventCommand {
    public AssertComboBoxCommand() {
        super("assert_combobox", params(LOCATOR, Type.STRING, "text", Type.STRING));
    }

    @Override
    protected void runJemmy(ExecutionContext ctx, Map<String, Value> params) throws JemmyException {
        JemmyDSL.comboBox(coerceToString(params.get(LOCATOR), ctx)).assertSelected(coerceToString(params.get("text"), ctx));
    }
}