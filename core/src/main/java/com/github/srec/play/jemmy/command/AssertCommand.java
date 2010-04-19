package com.github.srec.play.jemmy.command;

import com.github.srec.play.Command;
import com.github.srec.play.exception.IllegalParametersException;

import static com.github.srec.play.jemmy.JemmyDSL.assertText;

/**
 * @author Victor Tatai
 */
public class AssertCommand implements Command {
    @Override
    public String getName() {
        return "assert";
    }

    @Override
    public void run(String... params) {
        if (params.length != 2) throw new IllegalParametersException("Missing parameters to assert");
        assertText(params[0], params[1]);
    }

    @Override
    public String toString() {
        return getName();
    }
}