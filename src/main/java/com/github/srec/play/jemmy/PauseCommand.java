package com.github.srec.play.jemmy;

import com.github.srec.play.Command;
import com.github.srec.play.exception.IllegalParametersException;
import com.github.srec.play.exception.PlayerException;

import static com.github.srec.play.jemmy.JemmyDSL.click;

public class PauseCommand implements Command {
    private static final int PAUSE_INTERVAL = 5000;

    @Override
    public String getName() {
        return "pause";
    }

    @Override
    public void run(String... params) {
        if (params.length != 0) throw new IllegalParametersException("Extra parameters for pause");
        try {
            Thread.sleep(PAUSE_INTERVAL);
        } catch (InterruptedException e) {
            throw new PlayerException(e);
        }
    }

    @Override
    public String toString() {
        return getName();
    }
}