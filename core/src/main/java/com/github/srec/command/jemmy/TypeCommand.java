package com.github.srec.command.jemmy;

import com.github.srec.command.EventCommand;

import java.awt.*;

import static com.github.srec.jemmy.JemmyDSL.textField;

/**
 * @author Victor Tatai
 */
public class TypeCommand extends JemmyEventCommand {
    public TypeCommand(String componentLocator, Component component, String text) {
        super("type", componentLocator, component, true, text);
    }

    @Override
    public void runJemmy() {
        textField(componentLocator).type(params[0]);
    }
}