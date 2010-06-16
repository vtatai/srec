package com.github.srec.command.jemmy;

import com.github.srec.command.ExecutionContext;
import com.github.srec.command.SRecCommand;
import com.github.srec.command.value.NumberValue;
import com.github.srec.command.value.Type;
import com.github.srec.command.value.Value;
import org.netbeans.jemmy.JemmyException;

import java.util.Map;

import static com.github.srec.jemmy.JemmyDSL.click;
import static com.github.srec.jemmy.JemmyDSL.textField;

/**
 * @author Victor Tatai
 */
@SRecCommand
public class ClickCommand extends JemmyEventCommand {
    public static final String COUNT = "count";
    public static final String TEXT_COLUMN = "textColumn";
    public static final String MODIFIERS = "modifiers";

    public ClickCommand() {
        super("click", param(LOCATOR), param(MODIFIERS, Type.STRING, true, null),
                param(TEXT_COLUMN, Type.NUMBER, true, null),
                param(COUNT, Type.NUMBER, true, new NumberValue("1")));
    }

    @Override
    protected void runJemmy(ExecutionContext ctx, Map<String, Value> params) throws JemmyException {
        if (params.get(TEXT_COLUMN) == null)  {
            click(coerceToString(params.get(LOCATOR), ctx), asBigDecimal(COUNT, params).intValue(),
                    asString(MODIFIERS, params, ctx));
        } else {
            textField(coerceToString(params.get(LOCATOR), ctx)).clickCharPosition(asBigDecimal(TEXT_COLUMN, params).intValue(), 
                    asString(MODIFIERS, params, ctx));
        }
    }
}
