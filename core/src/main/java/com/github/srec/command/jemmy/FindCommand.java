package com.github.srec.command.jemmy;

import com.github.srec.command.ExecutionContext;
import com.github.srec.command.ExecutionContextCommand;
import com.github.srec.command.value.Value;

import static com.github.srec.jemmy.JemmyDSL.find;

/**
 * @author Victor Tatai
 */
@ExecutionContextCommand
public class FindCommand extends JemmyEventCommand {
    public FindCommand() {
        super("find", "componentLocator", "id", "findComponentType");
    }

    @Override
    public void runJemmy(ExecutionContext ctx, Value... params) {
        find(coerceToString(params[0]), coerceToString(params[1]), coerceToString(params[2]));
    }
}