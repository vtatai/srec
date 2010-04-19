package com.github.srec.play.jemmy.command;

import com.github.srec.play.Command;
import com.github.srec.play.exception.IllegalParametersException;

import static com.github.srec.play.jemmy.JemmyDSL.dialog;
import static com.github.srec.play.jemmy.JemmyDSL.frame;

/**
 * @author Victor Tatai
 */
public class DialogActivateCommand implements Command {
    @Override
    public String getName() {
        return "dialog_activate";
    }

    @Override
    public void run(String... params) {
        if (params.length != 1) throw new IllegalParametersException("Missing parameters to dialog activate");
        dialog(params[0]);
    }

    @Override
    public String toString() {
        return getName();
    }
}