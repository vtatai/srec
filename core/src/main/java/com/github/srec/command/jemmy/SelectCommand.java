package com.github.srec.command.jemmy;

import com.github.srec.Utils;
import com.github.srec.play.exception.IllegalParametersException;

import java.awt.*;
import java.util.Map;

import static com.github.srec.jemmy.JemmyDSL.comboBox;

/**
 * @author Victor Tatai
 */
public class SelectCommand extends JemmyEventCommand {
    public SelectCommand(String componentLocator, Component component, String item) {
        super("select", componentLocator, component, item);
    }

    @Override
    public void runJemmy() {
        Map<String, String> selectedItem = Utils.parseLocator(params[0]);
        if (selectedItem.containsKey("name")) comboBox(componentLocator).select(selectedItem.get("name"));
        else if (selectedItem.containsKey("index")) comboBox(componentLocator).select(Integer.parseInt(selectedItem.get("index")));
        else throw new IllegalParametersException("Illegal parameters " + Utils.asString(params) + " for select command");
    }
}