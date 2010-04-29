package com.github.srec.command.jemmy;

import com.github.srec.command.EventCommand;
import com.github.srec.play.exception.PlayerException;
import org.apache.log4j.Logger;

/**
 * @author Victor Tatai
 */
public class PauseCommand extends JemmyEventCommand {
    private static final Logger logger = Logger.getLogger(PauseCommand.class);
    private static final int PAUSE_INTERVAL = 5000;

    public PauseCommand() {
        super("pause", null, null);
    }

    @Override
    public void runJemmy() {
        logger.debug("Pausing execution for " + PAUSE_INTERVAL + "ms");
        try {
            Thread.sleep(PAUSE_INTERVAL);
        } catch (InterruptedException e) {
            throw new PlayerException(e);
        }
    }
}