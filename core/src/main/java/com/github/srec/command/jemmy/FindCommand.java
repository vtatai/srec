package com.github.srec.command.jemmy;

import com.github.srec.command.ExecutionContext;
import com.github.srec.command.SRecCommand;
import com.github.srec.command.value.Type;
import com.github.srec.command.value.Value;

import java.util.Map;

import static com.github.srec.jemmy.JemmyDSL.find;

/**
 * Finds a component assigning and id to it. This id can later be used as a locator in the form "id=XXX".
 * 
 * @author Victor Tatai
 */
@SRecCommand
public class FindCommand extends JemmyEventCommand {
    public FindCommand() {
        super("find", createParametersDefinition("name", Type.STRING, "id", Type.STRING, "findComponentType", Type.STRING));
    }

    @Override
    public void runJemmy(ExecutionContext ctx, Map<String, Value> params) {
        find(coerceToString(params.get("name"), ctx), asString("id", params, ctx), asString("findComponentType", params, ctx));
    }
}