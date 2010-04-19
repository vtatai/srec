package com.github.srec.play.jemmy.command;

import com.github.srec.play.Command;
import com.github.srec.play.exception.IllegalParametersException;

import static com.github.srec.play.jemmy.JemmyDSL.frame;

/**
 * @author Victor Tatai
 */
public class WindowActivateCommand implements Command {
    @Override
    public String getName() {
        return "window_activate";
    }

    @Override
    public void run(String... params) {
        if (params.length != 1) throw new IllegalParametersException("Missing parameters to window activate");
        frame(params[0]);
    }

    @Override
    public String toString() {
        return getName();
    }
}
