package com.github.srec.command;

import com.github.srec.SRecException;
import com.google.common.base.Predicate;
import org.apache.log4j.Logger;
import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * Interface which defines a context execution factory.
 *
 * @author Victor Tatai
 */
public class ExecutionContextFactory {
    private static final Logger log = Logger.getLogger(ExecutionContextFactory.class);
    private static ExecutionContextFactory instance;
    public List<MethodCommand> builtinCommands = new ArrayList<MethodCommand>();

    private ExecutionContextFactory() {
    }

    private void init() throws IOException, InstantiationException, IllegalAccessException {
        scanPackage("com.github.srec.command.jemmy.*");
        InputStream is = ClassLoader.getSystemResourceAsStream("custom_commands.properties");
        if (is != null) {
            Properties props = new Properties();
            props.load(is);
            for (Object obj : props.keySet()) {
                scanPackage(obj.toString());
            }
        }
    }

    private void scanPackage(String pattern) throws IllegalAccessException, InstantiationException {
        Predicate<String> filter = new FilterBuilder().include(pattern);
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .filterInputsBy(filter)
                .setScanners(
                        new TypeAnnotationsScanner())
                .setUrls(ClasspathHelper.getUrlsForCurrentClasspath()));
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(ExecutionContextCommand.class);
        
        for (Class<?> aClass : annotated) {
            if (!MethodCommand.class.isAssignableFrom(aClass)) {
                log.error("Annotated type " + aClass + " is not a MethodCommand, ignoring");
                continue;
            }
            MethodCommand instance = (MethodCommand) aClass.newInstance();
            log.debug("Adding scanned method command: " + aClass.getCanonicalName());
            builtinCommands.add(instance);
        }
    }

    public ExecutionContext create(File file, String... loadPath) {
        ExecutionContext ctx = new ExecutionContext(file, loadPath);
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
            } catch (IOException e) {
                throw new SRecException(e);
            } catch (InstantiationException e) {
                throw new SRecException(e);
            } catch (IllegalAccessException e) {
                throw new SRecException(e);
            }
        }
        return instance;
    }
}
