package com.github.srec.command.jemmy;

import com.github.srec.command.ExecutionContext;
import com.github.srec.command.ExecutionContextCommand;
import com.github.srec.command.exception.CommandExecutionException;
import com.github.srec.command.value.NumberValue;
import com.github.srec.command.value.Value;
import org.apache.log4j.Logger;
import org.netbeans.jemmy.JemmyException;

/**
 * @author Victor Tatai
 */
@ExecutionContextCommand
public class PauseCommand extends JemmyEventCommand {
    private static final Logger logger = Logger.getLogger(PauseCommand.class);
    private static final String DEFAULT_PAUSE_INTERVAL = "5000";

    public PauseCommand() {
        super("pause", new Parameter("interval", true, new NumberValue(DEFAULT_PAUSE_INTERVAL)));
    }

    @Override
    protected void runJemmy(ExecutionContext ctx, Value... params) throws JemmyException {
        long pause = coerceToBigDecimal(get("interval", params)).longValue();
        logger.debug("Pausing execution for " + pause + "ms");
        try {
            Thread.sleep(pause);
        } catch (InterruptedException e) {
            throw new CommandExecutionException(e);
        }
    }
}