package com.github.srec.play.jemmy;

import com.github.srec.UnsupportedFeatureException;
import com.github.srec.play.Command;
import com.github.srec.play.exception.IllegalParametersException;

import java.awt.event.KeyEvent;

import static com.github.srec.play.jemmy.JemmyDSL.textField;

public class TypeSpecialCommand implements Command {
    @Override
    public String getName() {
        return "type_special";
    }

    @Override
    public void run(String... params) {
        if (params.length != 2) throw new IllegalParametersException("Missing params to text field type special");
        char key;
        if (params[1].equals("Tab")) key = '\t';
        else throw new UnsupportedFeatureException("Type special for " + params[1] + " not supported");
        textField(params[0]).type(key);
    }

    @Override
    public String toString() {
        return getName();
    }
}
