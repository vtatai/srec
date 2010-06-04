package com.github.srec.command.jemmy;

import com.github.srec.command.ExecutionContext;
import com.github.srec.command.SRecCommand;
import com.github.srec.command.exception.CommandExecutionException;
import com.github.srec.command.method.MethodParameter;
import com.github.srec.command.value.Type;
import com.github.srec.command.value.Value;
import org.apache.log4j.Logger;
import org.netbeans.jemmy.JemmyException;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Map;

/**
 * @author Victor Tatai
 */
@SRecCommand
public class PauseCommand extends JemmyEventCommand implements KeyEventDispatcher {
    private static final Logger logger = Logger.getLogger(PauseCommand.class);
    private volatile boolean waiting;

    public PauseCommand() {
        super("pause", new MethodParameter("interval", Type.NUMBER, true, null));
    }

    @Override
    protected void runJemmy(ExecutionContext ctx, Map<String, Value> params) throws JemmyException {
        if (params.get("interval") != null) {
            long pause = coerceToBigDecimal(params.get("interval")).longValue();
            logger.debug("Pausing execution for " + pause + "ms");
            try {
                Thread.sleep(pause);
            } catch (InterruptedException e) {
                throw new CommandExecutionException(e);
            }
        } else {
            // Pause until Ctrl-P is pressed
            KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
            waiting = true;
            logger.info("Pausing execution until Ctrl-P is pressed");
            try {
                synchronized (this) {
                    while (waiting) {
                        wait();
                    }
                }
            } catch (InterruptedException e) {
                throw new CommandExecutionException(e);
            }
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        if (keyEvent.getID() != KeyEvent.KEY_PRESSED
                || !keyEvent.isControlDown()
                || keyEvent.getKeyCode() != KeyEvent.VK_P) {
            return false;
        }
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(this);
        synchronized (this) {
            waiting = false;
            notify();
        }
        return false;
    }
}