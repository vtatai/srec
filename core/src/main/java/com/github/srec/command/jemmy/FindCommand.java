package com.github.srec.command.jemmy;

import com.github.srec.command.EventCommand;

import java.awt.*;

import static com.github.srec.jemmy.JemmyDSL.find;

/**
 * @author Victor Tatai
 */
public class FindCommand extends JemmyEventCommand {
    public FindCommand(String componentLocator, Component component, String id, String findComponentType) {
        super("find", componentLocator, component, id, findComponentType);
    }

    @Override
    public void runJemmy() {
        find(componentLocator, params[0], params[1]);
    }
}