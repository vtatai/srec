package com.github.srec.command.jemmy;

import com.github.srec.command.ExecutionContext;
import com.github.srec.command.SRecCommand;
import com.github.srec.command.method.MethodParameter;
import com.github.srec.command.value.BooleanValue;
import com.github.srec.command.value.NumberValue;
import com.github.srec.command.value.Type;
import com.github.srec.command.value.Value;

import org.netbeans.jemmy.JemmyException;

import java.util.Map;

import static com.github.srec.jemmy.JemmyDSL.waitChecked;
import static com.github.srec.jemmy.JemmyDSL.waitCheckedSelected;

/**
 * @author Victor Tatai
 */
@SRecCommand
public class AssertCheckedCommand extends JemmyEventCommand {
    public static final String INDEX = "index";
    public AssertCheckedCommand() {
        
        super("assert_checked", new MethodParameter(LOCATOR, Type.STRING),
              new MethodParameter("checked", Type.BOOLEAN, true, BooleanValue.TRUE),
              param(INDEX, Type.NUMBER, true, null));
    }

    @Override
    protected void runJemmy(ExecutionContext ctx, Map<String, Value> params) throws JemmyException {
        if (params.get(INDEX) != null) {
            waitCheckedSelected(coerceToString(params.get(LOCATOR), ctx), coerceToBoolean(params.get("checked")), (asBigDecimal(INDEX, params).intValue()));
        } else {
            waitChecked(coerceToString(params.get(LOCATOR), ctx), coerceToBoolean(params.get("checked")));
        }
        
    }
}