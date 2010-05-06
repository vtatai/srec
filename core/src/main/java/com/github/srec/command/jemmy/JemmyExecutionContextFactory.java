package com.github.srec.command.jemmy;

import com.github.srec.command.ExecutionContext;
import com.github.srec.command.ExecutionContextFactory;

import java.io.File;

/**
 * @author Victor Tatai
 */
public class JemmyExecutionContextFactory implements ExecutionContextFactory {
    @Override
    public ExecutionContext create(File file) {
        ExecutionContext ctx = new ExecutionContext(file);
        ctx.addSymbol(new AssertCommand());
        ctx.addSymbol(new ClickCommand());
        ctx.addSymbol(new CloseCommand());
        ctx.addSymbol(new DialogActivateCommand());
        ctx.addSymbol(new FindCommand());
        ctx.addSymbol(new PauseCommand());
        ctx.addSymbol(new SelectCommand());
        ctx.addSymbol(new TypeCommand());
        ctx.addSymbol(new TypeSpecialCommand());
        ctx.addSymbol(new WindowActivateCommand());
        return ctx;
    }
}
