package com.github.srec.play.jemmy;

import com.github.srec.play.Command;
import com.github.srec.play.IllegalParametersException;
import org.netbeans.jemmy.operators.JButtonOperator;

import static com.github.srec.play.jemmy.JemmyDSL.button;

public class ClickCommand implements Command {
    @Override
    public String getName() {
        return "click";
    }

    @Override
    public void run(String... params) {
        if (params.length != 1) throw new IllegalParametersException("Missing button locator");
        button(params[0]).click();
    }
}
