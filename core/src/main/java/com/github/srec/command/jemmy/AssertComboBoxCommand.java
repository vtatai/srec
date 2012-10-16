package com.github.srec.command.jemmy;

import com.github.srec.command.ExecutionContext;
import com.github.srec.command.SRecCommand;
import com.github.srec.command.value.Type;
import com.github.srec.command.value.Value;
import com.github.srec.jemmy.IllegalParametersException;
import com.github.srec.jemmy.JemmyDSL;
import org.netbeans.jemmy.JemmyException;

import java.util.Map;

/**
 * @author Victor Tatai
 */
@SRecCommand
public class AssertComboBoxCommand extends JemmyEventCommand {
    public AssertComboBoxCommand() {
        super("assert_combobox", param(LOCATOR, Type.STRING),
                                  param("text", Type.STRING, true, null),
                                  param("index", Type.NUMBER, true, null));
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected void runJemmy(ExecutionContext ctx, Map<String, Value> params) throws JemmyException {
        if (params.containsKey("text"))
            JemmyDSL.comboBox(coerceToString(params.get(LOCATOR), ctx)).assertSelected(coerceToString(params.get("text"), ctx));
        else
            JemmyDSL.comboBox(coerceToString(params.get(LOCATOR), ctx)).assertSelected(coerceToBigDecimal(params.get("index")));
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected void validateParameters(Map<String, Value> params) {
        super.validateParameters(params);
        if (!params.containsKey("text") && !params.containsKey("index")
                || params.containsKey("text") && params.containsKey("index"))
            throw new IllegalParametersException("assert_combobox should receive exactly one parameter from (text, index)");
    }
}