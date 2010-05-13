package com.github.srec.command.jemmy;

import com.github.srec.command.ExecutionContext;
import com.github.srec.command.ExecutionContextCommand;

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
    public void runJemmy(ExecutionContext ctx, String... params) {
        find(params[0], params[1], params[2]);
    }
}