package com.github.srec.command.jemmy;

import com.github.srec.command.ExecutionContext;
import com.github.srec.command.SRecCommand;
import com.github.srec.command.value.Type;
import com.github.srec.command.value.Value;

import java.util.Map;

import static com.github.srec.jemmy.JemmyDSL.find;

/**
 * Finds a component assigning an id to it. This id can later be used as a locator in the form "id=XXX". Notice that if
 * the component is not found no error is thrown, and the id is assigned a null value.
 * 
 * @author Victor Tatai
 */
@SRecCommand
public class FindCommand extends JemmyEventCommand {
    public FindCommand() {
        super("find", param(LOCATOR), param("id"), param("findComponentType", Type.STRING, false, null));
    }

    @Override
    public void runJemmy(ExecutionContext ctx, Map<String, Value> params) {
        String locator = coerceToString(params.get(LOCATOR), ctx);
        find(locator, asString("id", params, ctx), asString("findComponentType", params, ctx));
    }
}