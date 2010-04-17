package com.github.srec.play.jemmy;

import com.github.srec.Utils;
import com.github.srec.play.Command;
import com.github.srec.play.exception.IllegalParametersException;

import java.util.Map;

import static com.github.srec.play.jemmy.JemmyDSL.comboBox;

public class SelectCommand implements Command {
    @Override
    public String getName() {
        return "select";
    }

    @Override
    public void run(String... params) {
        if (params.length != 2) throw new IllegalParametersException("Missing parameters to combo box select");
        Map<String, String> selectedItem = Utils.parseLocator(params[1]);
        if (selectedItem.containsKey("name")) comboBox(params[0]).select(params[1]);
        else if (selectedItem.containsKey("index")) comboBox(params[0]).select(Integer.parseInt(selectedItem.get("index")));
        else throw new IllegalParametersException("Illegal parameters " + Utils.asString(params) + " for select command");
    }

    @Override
    public String toString() {
        return getName();
    }
}