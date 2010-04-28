package com.github.srec.command.jemmy;

import com.github.srec.UnsupportedFeatureException;
import com.github.srec.command.EventCommand;
import com.github.srec.play.exception.IllegalParametersException;

import java.awt.*;

import static com.github.srec.jemmy.JemmyDSL.textField;

/**
 * @author Victor Tatai
 */
public class TypeSpecialCommand extends JemmyEventCommand {
    public TypeSpecialCommand(String componentLocator, Component component, String text) {
        super("type_special", componentLocator, component, text);
    }

    @Override
    public void runJemmy() {
        char key;
        if (params[0].equals("Tab")) key = '\t';
        else throw new UnsupportedFeatureException("Type special for " + params[0] + " not supported");
        textField(componentLocator).type(key);
    }
}
