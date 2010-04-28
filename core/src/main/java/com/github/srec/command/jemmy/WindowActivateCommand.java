package com.github.srec.command.jemmy;

import com.github.srec.command.EventCommand;
import com.github.srec.play.exception.IllegalParametersException;

import java.awt.*;

import static com.github.srec.jemmy.JemmyDSL.frame;

/**
 * @author Victor Tatai
 */
public class WindowActivateCommand extends JemmyEventCommand {
    public WindowActivateCommand(String componentLocator, Component component) {
        super("window_activate", componentLocator, component);
    }

    @Override
    public void runJemmy() {
        frame(componentLocator);
    }
}
