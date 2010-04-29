package com.github.srec.command.jemmy;

import com.github.srec.command.EventCommand;
import com.github.srec.play.exception.IllegalParametersException;

import java.awt.*;

import static com.github.srec.jemmy.JemmyDSL.assertText;

/**
 * @author Victor Tatai
 */
public class AssertCommand extends JemmyEventCommand {
    public AssertCommand(String componentLocator, Component component, String param) {
        super("assert", componentLocator, component, param);
    }

    @Override
    public void runJemmy() {
        assertText(componentLocator, params[0]);
    }
}