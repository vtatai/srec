package com.github.srec.command.jemmy;

import com.github.srec.command.EventCommand;

import java.awt.*;

import static com.github.srec.jemmy.JemmyDSL.click;

/**
 * @author Victor Tatai
 */
public class ClickCommand extends JemmyEventCommand {
    public ClickCommand(String componentLocator, Component component) {
        super("click", componentLocator, component);
    }

    @Override
    public void runJemmy() {
        click(componentLocator);
    }
}
