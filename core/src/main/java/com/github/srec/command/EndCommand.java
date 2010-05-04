package com.github.srec.command;

import org.antlr.runtime.tree.CommonTree;

/**
 * @author Victor Tatai
 */
public class EndCommand extends BaseCommand {
    public EndCommand(CommonTree tree) {
        super("end", tree);
    }
    
    @Override
    public void run(ExecutionContext context) {
    }
}
