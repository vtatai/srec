package com.github.srec.play.jemmy;

import com.github.srec.play.Command;
import com.github.srec.play.exception.IllegalParametersException;

import static com.github.srec.play.jemmy.JemmyDSL.comboBox;

public class SelectCommand implements Command {
    @Override
    public String getName() {
        return "select";
    }

    @Override
    public void run(String... params) {
        if (params.length != 2) throw new IllegalParametersException("Missing parameters to combo box select");
        comboBox(params[0]).select(params[1]);
    }

    @Override
    public String toString() {
        return getName();
    }
}