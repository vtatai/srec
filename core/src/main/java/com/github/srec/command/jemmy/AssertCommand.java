package com.github.srec.command.jemmy;

import com.github.srec.command.ExecutionContext;
import com.github.srec.command.SRecCommand;
import com.github.srec.command.value.NumberValue;
import com.github.srec.command.value.Type;
import com.github.srec.command.value.Value;
import org.netbeans.jemmy.JemmyException;

import java.math.BigDecimal;
import java.util.Map;

import static com.github.srec.jemmy.JemmyDSL.textField;
import static com.github.srec.jemmy.JemmyDSL.listBox;

/**
 * @author Victor Tatai
 */
@SRecCommand
public class AssertCommand extends JemmyEventCommand {
public static final String INDEX = "index";
    
    public AssertCommand() {
        super("assert", params(LOCATOR, Type.STRING, "text", Type.STRING, INDEX, Type.NUMBER));
    }

    @Override
    protected void runJemmy(ExecutionContext ctx, Map<String, Value> params) throws JemmyException {
        if (params.get(INDEX) != null) {
            listBox(coerceToString(params.get(LOCATOR), ctx)).assertText(coerceToString(params.get("text"), ctx), (asBigDecimal(INDEX, params).intValue()));
        } else {
            textField(coerceToString(params.get(LOCATOR), ctx)).assertText(coerceToString(params.get("text"), ctx));
        }
    }
}