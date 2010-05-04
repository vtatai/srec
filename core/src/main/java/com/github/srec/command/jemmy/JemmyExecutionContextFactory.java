package com.github.srec.command.jemmy;

import com.github.srec.command.ExecutionContext;
import com.github.srec.command.ExecutionContextFactory;

/**
 * @author Victor Tatai
 */
public class JemmyExecutionContextFactory implements ExecutionContextFactory {
    @Override
    public ExecutionContext create(String currentPath) {
        ExecutionContext ctx = new ExecutionContext(currentPath);
        ctx.addMethod(new AssertCommand());
        ctx.addMethod(new ClickCommand());
        ctx.addMethod(new CloseCommand());
        ctx.addMethod(new DialogActivateCommand());
        ctx.addMethod(new FindCommand());
        ctx.addMethod(new PauseCommand());
        ctx.addMethod(new SelectCommand());
        ctx.addMethod(new TypeCommand());
        ctx.addMethod(new TypeSpecialCommand());
        ctx.addMethod(new WindowActivateCommand());
        return ctx;
    }
}
