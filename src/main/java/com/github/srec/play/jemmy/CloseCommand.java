package com.github.srec.play.jemmy;

import com.github.srec.play.Command;
import com.github.srec.play.IllegalParametersException;

import static com.github.srec.play.jemmy.JemmyDSL.frame;

public class CloseCommand implements Command {
    @Override
    public String getName() {
        return "close";
    }

    @Override
    public void run(String... params) {
        if (params.length != 1) throw new IllegalParametersException("Missing parameter to close");
        frame(params[0]).close();
    }
}