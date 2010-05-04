package com.github.srec.command.jemmy;

import com.github.srec.command.ExecutionContext;
import com.github.srec.command.exception.CommandExecutionException;
import org.apache.log4j.Logger;
import org.netbeans.jemmy.JemmyException;

/**
 * @author Victor Tatai
 */
public class PauseCommand extends JemmyEventCommand {
    private static final Logger logger = Logger.getLogger(PauseCommand.class);
    private static final int PAUSE_INTERVAL = 5000;

    public PauseCommand() {
        super("pause");
    }

    @Override
    protected void runJemmy(ExecutionContext ctx, String... params) throws JemmyException {
        logger.debug("Pausing execution for " + PAUSE_INTERVAL + "ms");
        try {
            Thread.sleep(PAUSE_INTERVAL);
        } catch (InterruptedException e) {
            throw new CommandExecutionException(e);
        }
    }
}