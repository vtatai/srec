package com.github.srec.command;

import com.github.srec.SRecException;
import com.github.srec.command.method.MethodCommand;
import com.github.srec.util.ClasspathScanner;
import org.apache.log4j.Logger;

import java.io.File;
import java.net.URL;
import java.util.*;

/**
 * Interface which defines a context execution factory.
 *
 * @author Victor Tatai
 */
public class ExecutionContextFactory {
    private static final Logger log = Logger.getLogger(ExecutionContextFactory.class);
    public static final String PACKAGES_TO_SCAN_PROPERTY_NAME = "packages_to_scan";
    private static ExecutionContextFactory instance;
    public List<MethodCommand> builtinCommands = new ArrayList<MethodCommand>();

    private ExecutionContextFactory() {
    }

    private void init() throws Exception {
        Enumeration<URL> urls = ClassLoader.getSystemResources("srec.properties");
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            Properties prop = new Properties();
            prop.load(url.openStream());
            if (!prop.containsKey(PACKAGES_TO_SCAN_PROPERTY_NAME)) continue;
            String[] packagesToScan = prop.getProperty(PACKAGES_TO_SCAN_PROPERTY_NAME).split("[ |,]+");
            for (String packageName : packagesToScan) {
                scanPackage(new ClasspathScanner(), packageName);
            }
        }
    }

    private void scanPackage(ClasspathScanner scanner, String packageName) throws Exception {
        log.debug("Scanning package: " + packageName);
        Set<? extends Class> classes = scanner.scanPackage(packageName,
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
