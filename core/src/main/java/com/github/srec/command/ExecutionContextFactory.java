package com.github.srec.command;

import com.github.srec.SRecException;
import org.apache.log4j.Logger;
import org.scannotation.AnnotationDB;
import org.scannotation.ClasspathUrlFinder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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

    private void init() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        scanPackage("com.github.srec.command.jemmy");
        InputStream is = ClassLoader.getSystemResourceAsStream("custom_commands.properties");
        if (is != null) {
            Properties props = new Properties();
            props.load(is);
            for (Object obj : props.keySet()) {
                scanPackage(obj.toString());
            }
        }
    }

    private void scanPackage(String packageName) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        AnnotationDB ann = new AnnotationDB();
        ann.setScanClassAnnotations(true);
        URL[] urls = ClasspathUrlFinder.findClassPaths();
        ann.scanArchives(urls);
        Set<String> classes = ann.getAnnotationIndex().get(ExecutionContextCommand.class.getCanonicalName());
        for (String clName : classes) {
            Class cl = Class.forName(clName);
            builtinCommands.add((MethodCommand) cl.newInstance());
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
            } catch (ClassNotFoundException e) {
                throw new SRecException(e);
            }
        }
        return instance;
    }
}
