package com.github.srec.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.util.Arrays.asList;
import static java.util.Arrays.copyOfRange;

/**
 * Abstract command factory which supports methods and method calling.
 *
 * @author Victor Tatai
 */
public abstract class BaseCommandFactory implements CommandFactory {
    @Override
    public Command buildCommand(String command, String... params) {
        if ("def".equals(command)) return buildDefCommand(params);
        if ("end".equals(command)) return new EndCommand();
        EventCommand eventCommand = buildEventCommand(command, params);
        if (eventCommand != null) return eventCommand;
        return buildCallCommand(command, params);
    }

    private DefCommand buildDefCommand(String... params) {
        return new DefCommand(params[0], asList(copyOfRange(params, 1, params.length)));
    }

    private CallCommand buildCallCommand(String command, String[] params) {
        return new CallCommand(command, asList(params));
    }

    /**
     * Builds an event command.
     *
     * @param command The command
     * @param params The params
     * @return The command, null if non existent
     */
    public abstract EventCommand buildEventCommand(String command, String... params);
}
