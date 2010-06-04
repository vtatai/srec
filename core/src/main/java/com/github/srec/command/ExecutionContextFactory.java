package com.github.srec.command;

import com.github.srec.SRecException;
import com.github.srec.command.method.MethodCommand;
import com.github.srec.util.ClasspathScanner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Interface which defines a context execution factory.
 *
 * @author Victor Tatai
 */
public class ExecutionContextFactory {
    private static ExecutionContextFactory instance;
    public List<MethodCommand> builtinCommands = new ArrayList<MethodCommand>();

    private ExecutionContextFactory() {
    }

    private void init() throws Exception {
        ClasspathScanner scanner = new ClasspathScanner();
        Set<? extends Class> classes = scanner.scanPackage("com.github.srec.command.jemmy",
                new ClasspathScanner.AnnotatedClassSelector(SRecCommand.class));
        for (Class cl: classes) {
            builtinCommands.add((MethodCommand) cl.newInstance());
        }
    }

    public ExecutionContext create(TestSuite ts, TestCase tc, File file, String... loadPath) {
        ExecutionContext ctx = new ExecutionContext(ts, tc, file, loadPath);
        for (MethodCommand builtinCommand : builtinCommands) {
            ctx.addSymbol(builtinCommand);
        }
        return ctx;
    }

    public static ExecutionContextFactory getInstance() {
        if (instance == null) {
            instance = new ExecutionContextFactory();
            try {
                instance.init();
            } catch (Exception e) {
                throw new SRecException(e);
            }
        }
        return instance;
    }
}
