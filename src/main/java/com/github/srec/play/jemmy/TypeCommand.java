package com.github.srec.play.jemmy;

import com.github.srec.play.Command;
import com.github.srec.play.exception.IllegalParametersException;

import static com.github.srec.play.jemmy.JemmyDSL.textField;

public class TypeCommand implements Command {
    @Override
    public String getName() {
        return "type";
    }

    @Override
    public void run(String... params) {
        if (params.length != 2) throw new IllegalParametersException("Missing params to text field type");
        textField(params[0]).type(params[1]);
    }

    @Override
    public String toString() {
        return getName();
    }
}