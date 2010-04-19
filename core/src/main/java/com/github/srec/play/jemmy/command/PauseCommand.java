package com.github.srec.play.jemmy.command;

import com.github.srec.play.Command;
import com.github.srec.play.exception.IllegalParametersException;
import com.github.srec.play.exception.PlayerException;
import org.apache.log4j.Logger;

/**
 * @author Victor Tatai
 */
public class PauseCommand implements Command {
    private static final Logger logger = Logger.getLogger(PauseCommand.class);
    private static final int PAUSE_INTERVAL = 5000;

    @Override
    public String getName() {
        return "pause";
    }

    @Override
    public void run(String... params) {
        if (params.length != 0) throw new IllegalParametersException("Extra parameters for pause");
        logger.debug("Pausing execution for " + PAUSE_INTERVAL + "ms");
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