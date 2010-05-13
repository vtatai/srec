package com.github.srec.command.jemmy;

import com.github.srec.command.ExecutionContext;
import com.github.srec.command.ExecutionContextCommand;
import com.github.srec.command.exception.CommandExecutionException;
import org.apache.log4j.Logger;
import org.netbeans.jemmy.JemmyException;

/**
 * @author Victor Tatai
 */
@ExecutionContextCommand
public class PauseCommand extends JemmyEventCommand {
    private static final Logger logger = Logger.getLogger(PauseCommand.class);
    private static final long DEFAULT_PAUSE_INTERVAL = 5000;

    public PauseCommand() {
        super("pause");
    }

    @Override
    protected void runJemmy(ExecutionContext ctx, String... params) throws JemmyException {
        long pause;
        if (params.length == 1) {
            pause = Long.parseLong(params[0]);
        } else {
            pause = DEFAULT_PAUSE_INTERVAL;
        }
        logger.debug("Pausing execution for " + pause + "ms");
        try {
            Thread.sleep(pause);
        } catch (InterruptedException e) {
            throw new CommandExecutionException(e);
        }
    }
}