package com.github.srec.command.jemmy;

import com.github.srec.command.ExecutionContext;
import com.github.srec.command.SRecCommand;
import com.github.srec.command.method.MethodParameter;
import com.github.srec.command.value.BooleanValue;
import com.github.srec.command.value.Type;
import com.github.srec.command.value.Value;

import org.netbeans.jemmy.JemmyException;

import java.util.Map;

import static com.github.srec.jemmy.JemmyDSL.waitEnabled;

/**
 * @author Victor Tatai
 */
@SRecCommand
public class AssertCheckedCommand extends JemmyEventCommand {
    public AssertCheckedCommand() {
        super("assert_checked", new MethodParameter(LOCATOR, Type.STRING),
              new MethodParameter("checked", Type.BOOLEAN, true, BooleanValue.TRUE));
    }

    @Override
    protected void runJemmy(ExecutionContext ctx, Map<String, Value> params) throws JemmyException {
        waitEnabled(coerceToString(params.get(LOCATOR), ctx), coerceToBoolean(params.get("checked")));
    }
}