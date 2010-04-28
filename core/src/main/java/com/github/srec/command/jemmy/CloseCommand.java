package com.github.srec.command.jemmy;

import com.github.srec.command.EventCommand;

import java.awt.*;

import static com.github.srec.jemmy.JemmyDSL.frame;

/**
 * @author Victor Tatai
 */
public class CloseCommand extends JemmyEventCommand {
    public CloseCommand(String componentLocator, Component component) {
        super("close", componentLocator, component);
    }

    @Override
    public void runJemmy() {
        frame(componentLocator).close();
    }
}