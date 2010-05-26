package com.github.srec.command.jemmy;

import com.github.srec.command.ExecutionContext;
import com.github.srec.command.ExecutionContextCommand;
import com.github.srec.command.exception.CommandExecutionException;
import com.github.srec.command.method.MethodParameter;
import com.github.srec.command.value.NumberValue;
import com.github.srec.command.value.Type;
import com.github.srec.command.value.Value;
import org.apache.log4j.Logger;
import org.netbeans.jemmy.JemmyException;

import java.util.Map;

/**
 * @author Victor Tatai
 */
@ExecutionContextCommand
public class PauseCommand extends JemmyEventCommand {
    private static final Logger logger = Logger.getLogger(PauseCommand.class);
    private static final String DEFAULT_PAUSE_INTERVAL = "5000";

    public PauseCommand() {
        super("pause", new MethodParameter("interval", Type.NUMBER, true, new NumberValue(DEFAULT_PAUSE_INTERVAL)));
    }

    @Override
    protected void runJemmy(ExecutionContext ctx, Map<String, Value> params) throws JemmyException {
        long pause = coerceToBigDecimal(params.get("interval")).longValue();
        logger.debug("Pausing execution for " + pause + "ms");
        try {
            Thread.sleep(pause);
        } catch (InterruptedException e) {
            throw new CommandExecutionException(e);
        }
    }
}