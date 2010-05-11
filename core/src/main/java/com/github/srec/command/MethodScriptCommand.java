package com.github.srec.command;

import com.github.srec.Utils;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A method defined by a script.
 *
 * @author Victor Tatai
 */
public class MethodScriptCommand extends MethodCommand {
    private static final Logger logger = Logger.getLogger(MethodScriptCommand.class);
    protected List<Command> commands = new ArrayList<Command>();
    private File fileRead;

    public MethodScriptCommand(String name, File fileRead, String... parameters) {
        super(name, parameters);
        this.fileRead = fileRead;
    }

    @Override
    public String callMethod(ExecutionContext context, String... params) {
        logger.debug("Call method " + name + "(" + Utils.asString(params) + ")");
        context.getPlayer().play(new NestedExecutionContext(context, context.getFile()), commands);
        return null;
    }

    /**
     * Adds a command inside this one.
     *
     * @param command The command to add
     */
    public void add(Command command) {
        commands.add(command);
    }

    public List<Command> getCommands() {
        return commands;
    }


    public File getFileRead() {
        return fileRead;
    }

    public void setFileRead(File fileRead) {
        this.fileRead = fileRead;
    }
}
