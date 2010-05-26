package com.github.srec.command.jemmy;

import com.github.srec.Utils;
import com.github.srec.command.ExecutionContext;
import com.github.srec.command.ExecutionContextCommand;
import com.github.srec.command.exception.IllegalParametersException;
import com.github.srec.command.value.NumberValue;
import com.github.srec.command.value.StringValue;
import com.github.srec.command.value.Type;
import com.github.srec.command.value.Value;
import org.netbeans.jemmy.JemmyException;

import java.util.Map;

import static com.github.srec.jemmy.JemmyDSL.comboBox;

/**
 * @author Victor Tatai
 */
@ExecutionContextCommand
public class SelectCommand extends JemmyEventCommand {
    public SelectCommand() {
        super("select", createParametersDefinition(LOCATOR, Type.STRING, "item", Type.STRING, "index", Type.NUMBER));
    }

    @Override
    protected void runJemmy(ExecutionContext ctx, Map<String, Value> params) throws JemmyException {
        if (params.containsKey("item")) comboBox(coerceToString(params.get(LOCATOR), ctx)).select(((StringValue) params.get("item")).get());
        else if (params.containsKey("index")) comboBox(coerceToString(params.get(LOCATOR), ctx)).select(((NumberValue) params.get("index")).get().intValue());
        else throw new IllegalParametersException("Illegal parameters " + Utils.asString(params) + " for select command");
    }
}